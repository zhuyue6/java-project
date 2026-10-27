# 第 6 课：登录态：JWT 与拦截器

时长：90 分钟。

上一课：[MyBatis 与数据表](./05-MyBatis与数据表.md)

下一课：[模块串讲，并改一个接口](./07-模块串讲与改一个接口.md)

## 这节课结束时要能做到

- 讲清 token 的签发、携带、校验、作废。
- 对照前端该在哪保存 token、怎样加请求头、哪些 `code` 要清登录态。
- 说出普通用户和管理员为什么不能互调接口。

## 课前要打开的文件

- `modules/auth/service/impl/AuthServiceImpl.java`
- `modules/admin/service/impl/AdminAuthServiceImpl.java`
- `common/authentication/JwtService.java`
- `common/authentication/BlackListService.java`
- `common/authentication/IgnoreJwt.java`
- `common/interceptor/GlobalAuthInterceptor.java`
- `common/interceptor/LoginUser.java`
- `common/config/WebMvcConfig.java`

## 讲解

### 1. 前端已经在用的那一半

浏览器侧的登录态通常是：

1. 登录接口返回 token，放进内存或 `localStorage`。
2. axios 请求拦截器给后续请求加上 `Authorization: Bearer <token>`。
3. 响应里出现「未登录 / 过期」时清掉 token，跳到登录页。

这一课讲服务器上的另一半：token 里有什么、谁检查、登出之后旧 token 为什么立刻失效、停用用户为什么下一次请求就进不去。

### 2. 登录成功时 token 怎么产生

C 端登录在 `AuthServiceImpl.login`。用户存在、密码字符串相等、`status != 0` 之后：

```java
userService.updateUserStatusInRedis(userCredentialDto.getId(), userCredentialDto.getStatus());
String token = jwtService.generateToken(userCredentialDto.getId(), userCredentialDto.getUserName());
```

两参数的 `generateToken` 把 `accountType` 固定为 `"user"`。管理端登录调用三参数版本，第三个参数是 `"admin"`：

```java
String token = jwtService.generateToken(id, adminUser.getUserName(), "admin");
```

生成过程：

```34:41:clock-my-goal/src/main/java/com/zhuyue/clockMyGoal/common/authentication/JwtService.java
  public String generateToken(Long userId, String username, String accountType) {
    return Jwts.builder()
      .subject(userId.toString())
      .claim("userName", username)
      .claim("accountType", accountType)
      .expiration(new Date(System.currentTimeMillis() + expiration))
      .signWith(createSecretKey(), Jwts.SIG.HS256)
      .compact();
  }
```

令牌是一段字符串，中间用点分成三段。和本项目有关的内容是：

| 位置 | 本项目写入的值 |
| --- | --- |
| subject | 用户 id 的十进制字符串 |
| claim `userName` | 用户名 |
| claim `accountType` | `user` 或 `admin` |
| expiration | 当前时间加上 `jwt.expiration` 毫秒 |
| 签名 | HS256，密钥来自 `jwt.secret` |

签名用来发现令牌被改过。解析时用同一把密钥验签，验签失败或超过 expiration，`parseToken` 会抛异常。拦截器把过期收成「登录已过期」。

`jwt.secret`、`jwt.expiration` 来自 `application.yaml` 的 `${JWT_SECRET}`、`${JWT_EXPIRATION}`。本地和 Compose 里要配置环境变量，源码里没有默认密钥。

密码比对是 `stored.equals(input)`，库里的值与注册时提交的字符串相同。这是学习项目为了把流程露出来。生产环境存储哈希，比对哈希。课堂上说明这一点，避免把这段 `equals` 当成上线写法。

### 3. 哪些接口要带 token

`WebMvcConfig` 把 `GlobalAuthInterceptor` 挂到 `/**`，并排除 `/static/**` 和 `/error`：

```java
registry.addInterceptor(globalAuthInterceptor)
    .addPathPatterns("/**")
    .excludePathPatterns("/static/**", "/error");
```

这里必须注入容器中的拦截器。拦截器里的 `JwtService`、`BlackListService`、`UserService` 都是容器填的。

方法或类上有 `@IgnoreJwt` 时，`preHandle` 直接返回 `true`。当前标了这个注解的入口：

- `POST /auth/login`
- `POST /auth/register`
- `POST /admin/auth/login`
- `POST /admin/auth/logout`

C 端 `POST /auth/logout` 没有 `@IgnoreJwt`。登出请求会先被拦截器校验，通过后才进入 `AuthServiceImpl.logout`。过期 token 在拦截器就被拒绝，到不了黑名单逻辑。

管理端登出标了 `@IgnoreJwt`，拦截器放行，作废逻辑在 `AdminAuthServiceImpl.logout` 里自己解析 token。读代码时把这两个登出分开看。

### 4. 拦截器按顺序检查什么

`preHandle` 对需要登录的接口做这些事：

1. 处理器是 Controller 方法。静态资源之类直接放行。
2. 读 `Authorization`。缺头，或前缀不是 `Bearer `，抛 `Unauthorized`（10004），HTTP 状态 401。
3. `jwtService.getToken` 从第 7 个字符起截取，丢掉 `Bearer `。
4. Redis 键 `token:blacklist:<token>` 存在则抛 `LoginExpired`（10005）。
5. `parseToken`。过期抛 `LoginExpired`，HTTP 401。解析结果拿出 `id`、`userName`、`accountType`，装成 `LoginUser`。
6. 路径以 `/admin` 开头视为管理端接口。
   - `accountType` 为 `admin`：只能访问这类路径；再查 `admin_user`，不存在或 `status == 0` 则 10005。
   - `accountType` 为 `user`：访问 `/admin` 则 10004。
7. 普通用户再查状态。先读 Redis 键 `user:status:<userId>`，没有则 `userService.getUserByUserId` 回源，并把状态写回 Redis，TTL 10 分钟。`status == 0` 抛 `UserStatusDisabled`（11004），HTTP 401。
8. `request.setAttribute("userInfo", loginUser)`，返回 `true`，请求才进入 Controller。

Controller 里的 `@RequestAttribute("userInfo") LoginUser` 就是第 8 步放进去的对象。它只有 id 和用户名。目标、标签、头像都用这个 id，不再信任客户端传的 userId。

`postHandle` 把响应类型设为 JSON、UTF-8。业务返回值的包装仍由 `GlobalResponseHandler` 完成。

### 5. 登出和黑名单

JWT 签发之后，服务器默认没有一份「在线 session」可以删。令牌在过期之前一直能通过签名校验。这个项目用 Redis 补上「立刻失效」：

```java
String token = jwtService.getToken(authHeader);
long remainSeconds = jwtService.getTokenRemainSeconds(token);
if (remainSeconds > 0) {
  blackListService.addTokenToBlacklist(token, remainSeconds);
}
```

`addTokenToBlacklist` 执行：

```java
redisTemplate.opsForValue().set(
    "token:blacklist:" + token,
    "true",
    remainSeconds,
    TimeUnit.SECONDS);
```

键的生存时间和令牌剩余寿命一致。令牌本身到期后，键也消失，黑名单不用另外清理。下一次请求在解析之前就会因为键存在而被拒绝。

用户状态是另一个键：`user:status:<id>`，值是 `"0"` 或 `"1"`，生存 10 分钟。登录成功会写一次。管理端调用 `userService.updateUserStatus` 时会更新数据库并刷新这个键。拦截器优先读 Redis，所以停用可以在下一次请求生效，不必等 JWT 过期。Redis 没有这个键时回源数据库，停用状态不会因为缓存过期而放行。

### 6. 前端要接上的三件事

1. 登录、注册的 URL 不带 token。登录响应的 token 在 `data.token`（`LoginResponse` 的字段）。
2. 其他请求的头：

```http
Authorization: Bearer <data.token>
```

`Bearer` 和 token 之间有一个空格。拦截器用 `startsWith("Bearer ")` 判断，截取时丢掉前 7 个字符。

3. 响应处理：

| code | 含义 | 前端动作 |
| --- | --- | --- |
| 10004 | 未授权 | 清 token，去登录 |
| 10005 | 登录过期或已登出，或管理员不可用 | 清 token，去登录 |
| 11004 | 账号已停用 | 提示文案，清 token |

缺 `Authorization`、JWT 过期、账号停用这几支，拦截器在抛异常前把 HTTP 状态设为 401。token 已在黑名单时只抛 10005，没有改 HTTP 状态。页面判断以 JSON 里的 `code` 和 `message` 为准。

管理端是另一套登录：`POST /admin/auth/login`。它的 token 里 `accountType` 为 `admin`，只能调用 `/admin/**`。C 端 token 调用 `/admin/users` 会得到 10004。两个前端应用要分开放 token，不能混用一把。

## 课堂练习

按顺序做，每步看响应 `code`：

1. `POST /auth/register` 创建一个用户，再 `POST /auth/login`。把 `data.token` 存下来。
2. 不带 `Authorization` 调用 `GET /goals`。预期 10004。
3. 头写成 `Authorization: Bearer <token>`，再调 `GET /goals`。预期 `code` 为 0，`data` 为数组（新用户是空数组）。
4. `POST /auth/logout`，头里仍带这枚 token。预期成功。
5. 用同一枚 token 再调 `GET /goals`。预期 10005。Redis 里能看到键 `token:blacklist:` 加这整段 token。
6. 重新登录。用这枚 C 端 token 调 `GET /admin/users?pageNum=1&pageSize=10`。预期 10004。
7. 用 `init.sql` 里的管理员账号调用 `POST /admin/auth/login`，用返回的 token 再调第 6 步的列表。预期 `code` 为 0。

选做：登录后在管理端把该用户 `status` 更新为 0（`POST /admin/users/{id}/status`，body `{ "status": 0 }`），立刻用 C 端 token 调 `GET /goals`。预期 11004。这一步把第 4 课的 `updateUserStatus` 和这节的 Redis 状态键连在一起。管理端接口本身要管理员 token。

## 下课前对一下

- C 端 token 的 `accountType` 是 `user`，管理端是 `admin`，拦截器按路径前缀 `/admin` 分流。
- 当前用户进 Controller 的唯一入口是请求属性 `userInfo`。
- 登出把 token 放进 Redis，TTL 等于剩余有效期；拦截器先查黑名单再解析。
- 用户停用写在 `user:status:<id>`，没有缓存时回源数据库。
- 前端保存 `data.token`，请求头使用 `Bearer ` 前缀，10004 / 10005 / 11004 都要结束本地登录态。
