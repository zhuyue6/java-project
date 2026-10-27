# clock-my-goal 全栈培训

面向会写接口调用的前端。用本仓库的打卡应用把一次请求从浏览器讲到 MySQL，再讲回 `{ code, message, data }`。Java 项目内部是第 2 课到第 7 课。

业务代码在 `clock-my-goal`。每节讲义都可以单独打开，按「目标 → 顺着代码讲 → 练习 → 下课检查」上课。

## 课时

| 课 | 讲义 | 时长 | 重点文件 |
| --- | --- | --- | --- |
| 1 | [工程与启动](./01-工程与启动.md) | 90 分钟 | `pom.xml`、`application.yaml`、`docker/mysql/init/init.sql` |
| 2 | [请求分发与 REST](./02-请求分发与REST.md) | 90 分钟 | `UserController`、`AuthController`、`GoalController` |
| 3 | [入参、校验与统一响应](./03-入参校验与统一响应.md) | 90 分钟 | Request DTO、`GlobalExceptionHandler`、`ApiResponse` |
| 4 | [Service 分层](./04-Service分层.md) | 90 分钟 | `UserServiceImpl`、`ExceptionCodeEnum` |
| 5 | [MyBatis 与数据表](./05-MyBatis与数据表.md) | 90 分钟 | `UserMapper.java`、`UserMapper.xml` |
| 6 | [登录态：JWT 与拦截器](./06-登录态JWT与拦截器.md) | 90 分钟 | `JwtService`、`GlobalAuthInterceptor`、`WebMvcConfig` |
| 7 | [模块串讲，并改一个接口](./07-模块串讲与改一个接口.md) | 90 分钟 | goal、label、admin |
| 8 | [配置、数据与部署](./08-配置数据与部署.md) | 60–90 分钟 | `docker-compose.yaml`、`nginx.conf`、`Dockerfile` |

## 贯穿全课的一条请求

以「已登录用户创建一个目标」为例：

```text
浏览器
  → Nginx  location /api/          去掉 /api 前缀
  → Spring Boot :9090
  → GlobalAuthInterceptor.preHandle 验 JWT，写入 userInfo
  → GoalController.createGoal
  → GoalServiceImpl
  → GoalMapper.xml → MySQL goal 表
  → Controller 返回 long
  → GlobalResponseHandler           包成 { code: 0, message, data }
  → 失败则进 GlobalExceptionHandler，响应形状相同
```

执行顺序：拦截器 `preHandle` → Controller →（异常则 Advice）→ 响应体包装 → 拦截器 `postHandle`。

## 前端概念对照

| 前端 | 本项目 |
| --- | --- |
| `package.json` / `npm` | 根目录 `pom.xml` 与 `clock-my-goal/pom.xml`，构建用 Maven |
| 路由 + 请求方法 | `@RestController` + `@GetMapping` / `@PostMapping` 等 |
| TypeScript 类型 | `dto.request`、`dto.response`、`entity` |
| axios 请求拦截器 | `GlobalAuthInterceptor` |
| axios 响应拦截器 | `GlobalResponseHandler` |
| 前端错误码表 | `ExceptionCodeEnum` |
| `.env` | `application.yaml` 里的 `${DB_URL}` 等占位，值来自容器环境变量 |
| 浏览器保存的 token | 服务端签发的 JWT；登出写入 Redis 黑名单 |

## 上课时守住的三条

- 每节从一条真实请求进出，再打开对应的类。
- 现行接口写法以 `UserController` 和 `.cursor/rules/constitution.mdc` 为准。`goal`、`label` 里的 `/clockIn` 这类路径是早期写法，新代码用复数名词和连字符。
- 这个仓库是学习项目。注册和登录用字符串直接比对密码，表里存的也是这串字符。生产环境要对密码做哈希。培训目标是读懂这条链路。
