# 第 4 课：Service 分层

时长：90 分钟。

上一课：[入参、校验与统一响应](./03-入参校验与统一响应.md)

下一课：[MyBatis 与数据表](./05-MyBatis与数据表.md)

## 这节课结束时要能做到

- 顺着一次调用走完 Controller → Service 接口 → ServiceImpl → Mapper。
- 指出业务规则写在哪一层，以及为什么 Controller 不直接碰数据库。
- 会抛一个带错误码的 `BusinessException`，并知道它如何变成前端看到的 JSON。

## 课前要打开的文件

- `modules/user/service/UserService.java`
- `modules/user/service/impl/UserServiceImpl.java`
- `modules/auth/service/impl/AuthServiceImpl.java`
- `modules/user/controller/UserController.java`
- `common/constants/ExceptionCodeEnum.java`
- `common/exception/BusinessException.java`

## 讲解

### 1. 三层各自的职责

| 层 | 做什么 | 不做什么 |
| --- | --- | --- |
| Controller | 接 HTTP，取出 `LoginUser`，调用 Service，返回业务对象 | 不写 SQL，不组装大段业务判断 |
| Service | 业务规则：是否存在、状态是否允许、字段如何转换、调用谁 | 不关心参数来自 path 还是 body |
| Mapper | 按方法执行一条 SQL | 不决定错误码 |

Controller 依赖的是接口 `UserService`，运行时拿到的是 `UserServiceImpl`。接口上列出「能做什么」，实现类里写「怎么做」。换实现时，Controller 的方法签名可以保持不动。

### 2. 依赖怎么注入

`UserServiceImpl` 类上：

```java
@Service("UserService")
public class UserServiceImpl implements UserService {
```

`UserController` 字段上：

```java
@Resource(name = "UserService")
private UserService userService;
```

`@Service("UserService")` 把这个对象放进 Spring 容器，并起名 `UserService`。`@Resource(name = "UserService")` 按这个名字取出来，赋给接口类型的字段。名字要和实现类上的一致。

容器在启动时创建这些对象，并填好字段。业务代码里看不到 `new UserServiceImpl()`。第 6 课的拦截器也是同一套：`WebMvcConfig` 注入的是容器里的 `GlobalAuthInterceptor`，这样拦截器内部的 `JwtService`、`UserService` 才有值。自己 `new` 出来的拦截器，里面的依赖全是空的。

`UserServiceImpl` 自己再注入 `UserMapper`、`BlackListService`、`StoreageService`。调用链是容器把对象串起来的，方法里只使用字段。

项目里 `@Autowired` 和 `@Resource` 都会出现。新代码按宪法用 `@Resource(name = "XxxService")` 注入 Service。Mapper 上现有代码多用 `@Autowired`，阅读时知道两者都是在向容器要对象即可。

### 3. 走读：创建用户

注册的入口在 `AuthServiceImpl.register`，它先查重，再调用用户服务：

```java
UserCredentialRequest userCredential = userService.getUserCredentialByUserName(dto.getUserName());
if (userCredential != null) {
  throw new BusinessException(ExceptionCodeEnum.UserAlreadyExists);
}
long userId = userService.createUser(createUserDto);
```

`createUser`：

```54:64:clock-my-goal/src/main/java/com/zhuyue/clockMyGoal/modules/user/service/impl/UserServiceImpl.java
  public long createUser(@Valid CreateUserRequest createUserRequest) {
    User user = new User();
    user.setUserName(createUserRequest.getUserName());
    user.setPassword(createUserRequest.getPassword());
    user.setSex(createUserRequest.getSex());
    user.setAge(createUserRequest.getAge());
    user.setStatus(1);
    userMapper.insertUser(user);
    return user.getId();
  }
```

这里能看到分层在干什么：

1. 入参是 `CreateUserRequest`，对外字段到此为止。
2. Service 决定新用户 `status = 1`（正常）。这个默认值来自业务，请求体里没有 status。
3. `insertUser` 执行插入。XML 打开了 `useGeneratedKeys`，数据库自增 id 会写回 `user.id`。
4. 方法返回 `long`。Controller 把这个 long 交给响应包装，前端 `data` 就是新 id。

密码在这里原样放进 `User`。登录时 `AuthServiceImpl` 用 `String.equals` 和库里的字符串比较。学习项目把比对过程留在明处；生产环境应在写入前做密码哈希，比对哈希结果。

### 4. 走读：用户列表

`getUserList` 是分页的标准样子：

```102:118:clock-my-goal/src/main/java/com/zhuyue/clockMyGoal/modules/user/service/impl/UserServiceImpl.java
  public PageResponse<UserResponse> getUserList(UserListRequest userListRequest) {
    int pageSize = userListRequest.getPageSize();
    int pageNum = userListRequest.getPageNum();
    String keyword = userListRequest.getKeyword();
    Integer status = userListRequest.getStatus();
    int offset = (pageNum - 1) * pageSize;
    List<User> users = userMapper.selectUserList(offset, pageSize, keyword, status);
    long total = getUserListTotal(keyword, status);

    PageResponse<UserResponse> userListResponse = PageResponse.<UserResponse>builder()
      .pageSize(pageSize)
      .pageNum(pageNum)
      .data(users.stream().map(this::convertToUserResponse).collect(Collectors.toList()))
      .total(total)
      .build();
    return userListResponse;
  }
```

Service 做了三件 Mapper 不该决定的事：

- 把页码换成 SQL 的 `offset`。第 1 页、每页 10 条时，offset 为 0。
- 单独查 total。列表 SQL 带了 `LIMIT`，总数必须是另一条 `COUNT`。
- `convertToUserResponse`：`User` → `UserResponse`，时间变成字符串，密码不拷贝。

`PageResponse.builder()` 来自 Lombok 的 `@Builder`。它和手写 setter 一样，是在填一个普通对象。

### 5. 走读：不存在就抛业务异常

```140:146:clock-my-goal/src/main/java/com/zhuyue/clockMyGoal/modules/user/service/impl/UserServiceImpl.java
  private User requireUser(Long userId) {
    User user = userMapper.selectByUserId(userId);
    if (user == null) {
      throw new BusinessException(ExceptionCodeEnum.UserNotFound);
    }
    return user;
  }
```

`getUserByUserId` 和 `updateUserStatus` 都先调用它。Mapper 查不到时返回 `null`，Service 把 `null` 变成 `BusinessException`。异常越过 Controller，落到第 3 课的 `GlobalExceptionHandler`，响应为：

```json
{ "code": 11001, "message": "用户不存在", "data": null }
```

这就是「失败不要返回 null 让前端猜」在本项目里的做法。需要区分「没有数据」和「调用成功」时，用错误码；列表为空则返回空数组和 `total: 0`，那是成功。

登录失败的分支在 `AuthServiceImpl.login`，可以当成业务异常的目录来读：

- 查无此人 → `UserNotFound`（11001）
- 密码字符串不相等 → `UserNameOrPasswordError`（11002）
- `status == 0` → `UserStatusDisabled`（11004）
- 通过后把状态写入 Redis，再 `jwtService.generateToken`，并组装 `LoginResponse`

鉴权细节留到第 6 课。这节只要看到：登录的规则在 Auth 的 Service 里，用户的读写在 User 的 Service 里，Auth 通过注入 `UserService` 复用创建和查询，而不是自己去调 `UserMapper`。

### 6. 上传头像：Service 协调两件事

`uploadUserAvatar` 先让 `StoreageService` 保存文件，成功后再 `userMapper.updateAvatarByUserId`。文件 IO 失败时转成 `BusinessException`，错误码 `FileUploadError`（12002）。

Controller 只提供 `userId` 和 `MultipartFile`：

```java
long userId = loginUser.getUserId();
return userService.uploadUserAvatar(userId, file);
```

路径里的 `{id}` 在这个方法上没有参与「写到谁的头像」。实际写入的是 token 对应用户。读接口时把路径参数和 `userInfo` 分开看，避免以为 URL 上的 id 一定就是被修改的那一行。

### 7. 一个模块对外只暴露 Service

管理端停用用户时，`AdminPlatformUserServiceImpl.updateUserStatus` 调用的是 `userService.updateUserStatus`。用户状态的数据库更新和 Redis 更新都留在 `UserServiceImpl`：

```java
userMapper.updateUserStatus(userId, status);
updateUserStatusInRedis(userId, status);
```

管理端因此不用知道 `user` 表和 `user:status:` 这个 key 的细节。第 7 课串管理端时会再用到这条调用。

## 课堂练习

打开 `LabelServiceImpl.createLabel`，在纸上画出三行：

```text
LabelController.createLabel
  → LabelServiceImpl.createLabel
      → LabelMapper.insertLabel
```

标出抛 `LabelNameAlreadyExists` 的那一行。预期在 Service：先按用户取出已有标签，名称和类型都相同则抛 13001，否则才插入。

再回答：

1. Controller 的返回类型为什么是 `Long`，而重复名称的失败不会返回一个特殊 Long？
2. 如果把「名称不能重复」写进 Controller，Mapper 和 Service 的调用会变成什么样？

预期：失败通过异常离开方法，由全局处理器变成 JSON。规则放在 Service 后，别的入口（例如以后的管理端）复用同一段判断。

选做：给 `GET /users/{id}` 在用户不存在时的响应做一次真实请求，确认 body 是 11001。这个行为来自 `requireUser`，Controller 没有写 if。

## 下课前对一下

- Controller 取入参和登录用户，Service 写规则和对象转换，Mapper 执行 SQL。
- Service 用 `@Service("XxxService")` 注册，用 `@Resource(name = "XxxService")` 按接口注入。
- 创建用户时 status 的默认值、分页 offset、Entity 到 Response 的转换，都在 `UserServiceImpl`。
- 业务失败抛 `BusinessException`，错误码用 `ExceptionCodeEnum`。
- 跨模块复用通过注入对方的 Service 完成，Auth 创建用户、管理端改状态都是这样。
