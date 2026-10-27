# 第 2 课：请求分发与 REST

时长：90 分钟。

上一课：[工程与启动](./01-工程与启动.md)

下一课：[入参、校验与统一响应](./03-入参校验与统一响应.md)

## 这节课结束时要能做到

- 看到一个 URL 和方法，在项目里找到唯一的 Controller 方法。
- 说清路径参数、查询参数、JSON 体、请求头分别用哪个注解接。
- 按本项目的现行约定设计一个新接口的路径。

## 课前要打开的文件

- `clock-my-goal/src/main/java/com/zhuyue/clockMyGoal/modules/user/controller/UserController.java`
- `clock-my-goal/src/main/java/com/zhuyue/clockMyGoal/modules/auth/controller/AuthController.java`
- `clock-my-goal/src/main/java/com/zhuyue/clockMyGoal/modules/goal/controller/GoalController.java`
- `clock-my-goal/src/main/java/com/zhuyue/clockMyGoal/common/interceptor/LoginUser.java`
- `.cursor/rules/constitution.mdc` 里「RESTful API」一节

## 讲解

### 1. Controller 就是路由表

前端的路由文件把路径映射到页面。这里 `@RestController` 把类映射到一段 URL，方法上的 `@GetMapping`、`@PostMapping`、`@PatchMapping`、`@DeleteMapping` 再映射到具体动作。

`@RestController` 同时表示返回值直接写成响应体（JSON），方法里返回一个 Java 对象即可。包装成 `{ code, message, data }` 是后面的 `GlobalResponseHandler` 做的，Controller 自己返回业务数据。

`UserController` 的类级别路径是 `/users`：

```20:36:clock-my-goal/src/main/java/com/zhuyue/clockMyGoal/modules/user/controller/UserController.java
@RestController
@RequestMapping("/users")
public class UserController {
  @Resource(name = "UserService")
  private UserService userService;

  @GetMapping("/{id}")
  public UserResponse getUserByUserId(@PathVariable @NotNull long id) {
    return userService.getUserByUserId(id);
  }

  @GetMapping
  public PageResponse<UserResponse> getUserList(
    @Valid @ModelAttribute UserListRequest userListRequest
  ) {
    return userService.getUserList(userListRequest);
  }
```

因此：

- `GET /users/3` 进入 `getUserByUserId`，`3` 进参数 `id`。
- `GET /users?pageNum=1&pageSize=10&keyword=张` 进入 `getUserList`。查询串绑到 `UserListRequest` 的同名字段。

类上的路径和方法上的路径拼在一起。方法映射写成 `@GetMapping`、没有子路径时，命中的就是类路径本身。

### 2. 现行路径约定

新接口按 `.cursor/rules/constitution.mdc` 来写，样板是 `UserController`。

| 动作 | 方法 | 路径 | 入参 |
| --- | --- | --- | --- |
| 列表 | GET | `/users` | `@ModelAttribute`，分页字段 `pageNum`、`pageSize` |
| 详情 | GET | `/users/{id}` | `@PathVariable` |
| 创建 | POST | `/users` | `@RequestBody` |
| 全量更新 | PUT | `/users/{id}` | `@RequestBody` |
| 部分更新 | PATCH | `/users/{id}` | `@RequestBody` |
| 删除 | DELETE | `/users/{id}` | `@PathVariable` |

约定本身：

- 路径用复数名词、小写，多词用连字符：`/users`、`/goals`、`/labels`。
- 资源 id 放在路径里。
- 子资源继续往下挂。头像是用户的子资源：`POST /users/{id}/avatar`，文件用 `@RequestParam("file") MultipartFile`。
- 路径里不写 `create`、`get`、`update`、`remove`。动词交给 HTTP 方法。
- 列表走 GET 的 query，查询条件放在 URL 上。

登录、注册、登出是动作，挂在 `/auth` 下，一律 POST：

```21:36:clock-my-goal/src/main/java/com/zhuyue/clockMyGoal/modules/auth/controller/AuthController.java
  @IgnoreJwt
  @PostMapping("/login")
  public LoginResponse login(@RequestBody LoginDto request) {
    return authService.login(request);
  }

  @IgnoreJwt
  @PostMapping("/register")
  public Long register(@RequestBody RegisterDto request) {
    return authService.register(request);
  }

  @PostMapping("/logout")
  public void logout(@RequestHeader("Authorization") String authHeader) {
    authService.logout(authHeader);
  }
```

`@IgnoreJwt` 表示这个方法跳过登录校验。登录和注册自己还没有 token。登出必须带 `Authorization`，所以没有这个注解。第 6 课再讲拦截器怎么读它。

打卡这种动作做成子资源的创建。`GoalController` 里现有的是：

```http
POST /goals/{id}/clockIn
```

新代码写成连字符：`POST /goals/{id}/clock-ins`。`goal`、`label` 里已经存在的驼峰路径保持可调用，新接口按连字符写。

管理端路径以 `/admin` 开头，例如 `/admin/auth/login`、`/admin/users`。拦截器用这个前缀区分管理端 token 和 C 端 token。

### 3. 四种入参，对应前端发请求的四个位置

| 注解 | HTTP 里的位置 | 本项目例子 |
| --- | --- | --- |
| `@PathVariable` | URL 路径中的 `{id}` | `GET /users/{id}`、`GET /goals/{id}` |
| `@ModelAttribute` | query string | `GET /users?pageNum=1&pageSize=10` |
| `@RequestBody` | JSON body | `POST /auth/login`、`POST /goals` |
| `@RequestHeader` | 请求头 | 登出读取 `Authorization` |
| `@RequestParam` | query 或 `multipart` 表单字段 | 上传头像的 `file` |
| `@RequestAttribute` | 服务端自己放进请求的属性 | 当前登录用户 `userInfo` |

`@RequestAttribute("userInfo")` 在前端没有对应字段。拦截器验完 JWT 后执行：

```java
request.setAttribute("userInfo", loginUser);
```

Controller 再取出来。`LoginUser` 只有 `userId` 和 `userName`。

`GoalController.createGoal` 的签名把这件事写清楚了：

```33:37:clock-my-goal/src/main/java/com/zhuyue/clockMyGoal/modules/goal/controller/GoalController.java
  @PostMapping
  public long createGoal(
      @Valid @RequestBody CreateGoalRequest createGoalRequest,
      @RequestAttribute("userInfo") LoginUser loginUser) {
    long userId = loginUser.getUserId();
    return goalService.createGoal(userId, createGoalRequest);
  }
```

客户端提交目标的备注、到期时间、周期、标签。用户 id 来自 token。这样换一个 token 就只能创建自己的目标，请求体里多传一个 `userId` 也不会被这段代码采用。

列表接口同理。`GET /goals` 没有查询对象，直接用 `loginUser.getUserId()` 查这个人的全部目标。

### 4. 一个请求被分发时，Spring 在匹配什么

匹配用三样信息：HTTP 方法、路径、以及参数注解能否绑定。

`GET /users/8`：

1. 方法是 GET，路径在 `/users` 下还多了一段。
2. 命中 `@GetMapping("/{id}")`，这一段赋给 `id`。
3. 进入方法后调用 `userService.getUserByUserId`。

`GET /users?pageNum=1&pageSize=10`：

1. 方法是 GET，路径正好是 `/users`。
2. 命中没有子路径的 `@GetMapping`。
3. query 按字段名填进 `UserListRequest`。`pageNum`、`pageSize`、`keyword`、`status` 与 DTO 属性同名。

`POST /goals` 带 JSON：

1. 方法是 POST，路径是 `/goals`。
2. 命中 `GoalController` 上没有子路径的 `@PostMapping`。
3. JSON 反序列化成 `CreateGoalRequest`。`expirationTime` 的格式由字段上的 `@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")` 约定。

同一个类里，`GET /goals` 和 `GET /goals/{id}` 能共存，因为一个没有路径变量，一个有。如果两个方法的方法和路径都相同，应用会在启动时失败。排错时先看映射是否撞车。

### 5. Controller 的返回值就是 data

方法返回什么，成功响应里的 `data` 就是什么：

| 方法 | Java 返回值 | 前端拿到的 `data` |
| --- | --- | --- |
| `register` | `Long` | 新用户 id，例如 `1` |
| `login` | `LoginResponse` | 含 `token` 以及用户字段的对象 |
| `getUserList` | `PageResponse<UserResponse>` | `{ pageNum, pageSize, total, data: User[] }` |
| `logout` / `updateGoal` / `removeGoal` | `void` | `null` |
| `createGoal` | `long` | 新目标 id |

分页要看两层 `data`。外层是统一包装，内层是分页对象自己的列表字段，也叫 `data`：

```json
{
  "code": 0,
  "message": "操作成功",
  "data": {
    "pageNum": 1,
    "pageSize": 10,
    "total": 25,
    "data": []
  }
}
```

前端取列表用 `response.data.data`。这是 `PageResponse` 的字段名，和第 3 课的 `ApiResponse.data` 叠在一起的结果。

### 6. 经过 Nginx 之后，后端看到的路径少了 `/api`

线上 Nginx 的 `location /api/` 把请求转到 `http://clock-my-goal:9090/`。`proxy_pass` 末尾的 `/` 会去掉前缀 `/api/`。

浏览器请求 `GET /api/users/1`，Controller 匹配的是 `GET /users/1`。本地直连 `9090` 时不要加 `/api`。第 8 课再对着 `nginx.conf` 走一遍。

## 课堂练习

准备一张表，只靠注解填完，先不要打开 Service：

| 请求 | Controller 方法 | 用户 id 从哪来 |
| --- | --- | --- |
| `GET /users/1` | | |
| `GET /users?pageNum=1&pageSize=10` | | |
| `POST /users/1/avatar` | | |
| `POST /auth/login` | | |
| `POST /auth/logout` | | |
| `POST /goals` | | |
| `PATCH /goals/3` | | |
| `DELETE /goals/3` | | |
| `POST /goals/3/clockIn` | | |
| `GET /labels` | | |

参考答案在各自的 Controller 里。预期结论：

- 用户详情的 id 来自路径。这个接口当前没有读 `userInfo`。
- 目标、标签、头像上传的「是谁」来自 `userInfo`。
- 登录没有 `userInfo`，因为方法标了 `@IgnoreJwt`。
- 登出只读请求头里的 `Authorization`。

再写一个新接口的路径设计，先不写代码：为「当前用户的某条目标增加一条打卡记录」给出方法、路径、以及 userId 的来源。对照现行约定，路径应是 `POST /goals/{id}/clock-ins`，userId 来自 `LoginUser`。

## 下课前对一下

- 找接口时先看类上的 `@RequestMapping`，再看方法上的 HTTP 映射。
- 路径参数、query、JSON、请求头、登录用户，各自有固定注解。
- 新接口用复数名词；id 在路径中；列表用 GET query；当前用户从 `userInfo` 取。
- Controller 返回的对象会变成响应里的 `data`。分页列表要读 `data.data`。
