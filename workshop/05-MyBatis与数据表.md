# 第 5 课：MyBatis 与数据表

时长：90 分钟。

上一课：[Service 分层](./04-Service分层.md)

下一课：[登录态：JWT 与拦截器](./06-登录态JWT与拦截器.md)

## 这节课结束时要能做到

- 把 Mapper 接口的一个方法和 XML 里的一条 SQL 对上。
- 说清 `#{}` 名字从哪来：方法参数、`@Param`，或对象属性。
- 看懂用户列表的动态 WHERE，以及插入后 id 如何回到 Java 对象。

## 课前要打开的文件

- `modules/user/mapper/UserMapper.java`
- `src/main/resources/mapper/UserMapper.xml`
- `modules/user/entity/User.java`
- `modules/goal/mapper/GoalMapper.java`
- `src/main/resources/mapper/GoalMapper.xml`
- `docker/mysql/init/init.sql` 里的 `user`、`goal` 表
- `application.yaml` 的 `mybatis` 段

## 讲解

### 1. MyBatis 在这条链路里的位置

Service 不拼 SQL 字符串。它调用一个接口方法，MyBatis 按方法名找到 XML，执行 SQL，再把结果行填成实体。

```text
UserServiceImpl.getUserList
  → UserMapper.selectUserList(...)
      → UserMapper.xml 里 id="selectUserList" 的 <select>
          → MySQL user 表
          → 每一行变成一个 User
```

接口上的 `@Mapper` 让 Spring 扫描并生成代理对象。注入 `UserMapper` 时拿到的就是这个代理。项目里没有 `UserMapper` 的实现类，SQL 全在 XML。

`application.yaml`：

```yaml
mybatis:
  mapper-locations: classpath:mapper/*.xml
  configuration:
    map-underscore-to-camel-case: true
    log-impl: org.apache.ibatis.logging.stdout.StdOutImpl
```

- `mapper-locations` 指向 `src/main/resources/mapper/`。文件名可以和业务对应，真正绑定靠 XML 里的 `namespace`。
- `map-underscore-to-camel-case: true`：列 `user_name` 写入属性 `userName`，`create_time` 写入 `createTime`。实体上因此不用再写一行行 `@Results`。
- `StdOutImpl` 把 SQL 打到控制台，上课时看一次真实语句很有用。

### 2. namespace 和方法名必须对齐

```xml
<mapper namespace="com.zhuyue.clockMyGoal.modules.user.mapper.UserMapper">
    <select id="selectByUserId" resultType="com.zhuyue.clockMyGoal.modules.user.entity.User">
        SELECT * FROM user WHERE id = #{id}
    </select>
```

两条硬约定：

- `namespace` 是 Mapper 接口的全类名。
- `<select>` / `<insert>` / `<update>` / `<delete>` 的 `id` 是接口方法名。
- `resultType` 是返回的 Java 类型。列表方法在接口里是 `List<User>`，XML 仍写 `User`，MyBatis 会收集成 List。

`selectByUserId` 的参数：

```java
User selectByUserId(@Param("id") Long id);
```

XML 里的 `#{id}` 就是 `@Param("id")` 这个名字。单个原始参数也建议写上 `@Param`，这样 XML 里的名字稳定，不依赖编译器是否保留参数名。

### 3. 对象参数：名字就是属性名

`insertUser` 的参数是整个 `User`，接口没有给它加 `@Param`：

```java
void insertUser(User user);
```

XML：

```xml
<insert id="insertUser" useGeneratedKeys="true" keyProperty="id" keyColumn="id">
    INSERT INTO user(user_name, password, sex, age, status)
    VALUES(#{userName}, #{password}, #{sex}, #{age}, #{status})
</insert>
```

只有一个对象、又没有 `@Param` 时，`#{userName}` 表示这个对象的 `getUserName()`。列名仍然是数据库的 `user_name`，两边靠 SQL 里的对应关系，不靠名字自动相等。

`useGeneratedKeys="true"` 且 `keyProperty="id"`：插入成功后，把自增主键写回这个 `User` 的 `id`。所以 `UserServiceImpl.createUser` 在 `insertUser` 返回后能 `return user.getId()`。前端注册拿到的数字就是这样来的。

多个参数时必须 `@Param`，回写主键的 `keyProperty` 要写成「参数名.属性」。`GoalMapper` 是样板：

```java
void insertGoal(@Param("userId") long userId, @Param("goal") Goal goal);
```

```xml
<insert id="insertGoal" useGeneratedKeys="true" keyProperty="goal.id" keyColumn="id">
    INSERT INTO goal(user_id, label_id, expiration_time, clock_in_count,
                     target_clock_in_count, clock_in_cycle, remark)
    VALUES(#{userId}, #{goal.labelId}, #{goal.expirationTime}, #{goal.clockInCount},
           #{goal.targetClockInCount}, #{goal.clockInCycle}, #{goal.remark})
</insert>
```

`#{userId}` 是单独的参数。`#{goal.labelId}` 是名为 `goal` 的对象上的属性。`keyProperty="goal.id"` 把新 id 写进这只 `Goal`。如果写成 `keyProperty="id"`，MyBatis 找不到该把主键放回哪个参数。

`updateGoal`、`deleteGoal`、`selectGoalById` 的 WHERE 都带 `user_id = #{userId}`。即便客户端猜到了别人的目标 id，SQL 仍要求行属于当前用户。这是数据层对「只能动自己的目标」的又一道约束，和 Controller 从 token 取 userId 是连着的。

### 4. 动态 SQL：列表筛选

用户列表的 WHERE 抽成片段，计数和分页共用：

```xml
<sql id="UserWhere">
    <where>
        <if test="keyword != null and keyword != ''">
            AND user_name LIKE CONCAT('%', #{keyword}, '%')
        </if>
        <if test="status != null">
            AND status = #{status}
        </if>
    </where>
</sql>

<select id="selectUserList" resultType="com.zhuyue.clockMyGoal.modules.user.entity.User">
    SELECT * FROM user
    <include refid="UserWhere"/>
    ORDER BY update_time DESC, id DESC
    LIMIT #{offset}, #{pageSize}
</select>
```

`<if test="...">` 按参数决定要不要拼这段 SQL。`keyword` 为空时，语句里就没有 `LIKE`。`<where>` 会处理多余的 `AND`，所以第一个条件前面写 `AND` 是安全的。

`LIMIT #{offset}, #{pageSize}` 是 MySQL 的「跳过 offset 行，取 pageSize 行」。offset 在 `UserServiceImpl` 里已经算好：`(pageNum - 1) * pageSize`。XML 不再知道「第几页」，只接收两个整数。

`#{keyword}` 是预编译参数。即使用户输入里带引号，它也是字符串值，不会把输入拼进 SQL 结构。动态的是「哪几个条件出现」，值始终走占位符。

接口侧把每个名字都声明出来：

```java
List<User> selectUserList(
    @Param("offset") int offset,
    @Param("pageSize") int pageSize,
    @Param("keyword") String keyword,
    @Param("status") Integer status
);
```

XML 的 `#{offset}` 和 `@Param("offset")` 必须同名。改了 Java 参数名而忘了 `@Param` 或 XML，运行期会报找不到参数。

### 5. 表和时间字段

`init.sql` 里 `user.create_time`、`update_time` 是 `datetime`，默认 `current_timestamp`。实体里对应 `LocalDateTime`。开启驼峰后，MyBatis 按列名填充这两个字段。

响应给前端之前，Service 再用 `TimeService` 格式化成 `yyyy-MM-dd HH:mm:ss`。所以：

- 在 Mapper 和 Entity 这一层看到的是 `LocalDateTime`。
- 在 `UserResponse` 里看到的是 `String`。

`goal.expiration_time` 同样是 `datetime`，请求侧用 `LocalDateTime` 接，插入时由 `#{goal.expirationTime}` 写入。

目标的完成状态（进行中 / 已完成 / 已过期）在 `GoalServiceImpl.getGoalStatus` 里按次数和当前时间计算，响应上的 `status` 来自这次计算。表上的 `goal.status` 列有默认值，读列表时以 Service 算出的结果为准。筛选若要按「已过期」查，要先确认比的是计算结果还是这一列。第 7 课的练习会避开这个列，改筛备注。

### 6. 控制台里的 SQL

`log-impl` 已设为标准输出。调用 `GET /users?pageNum=1&pageSize=10` 时，日志里应能看到两条语句：一条 `COUNT`，一条带 `LIMIT 0, 10` 的查询。带 `keyword` 时两条都会出现 `LIKE`。

这条日志是开发期的观察口。它打印的是把参数替换进去之后的样子，用来对照 XML 是否拼对。

## 课堂练习

1. 打开 `GoalMapper.xml` 的 `insertGoal`。列出每一列的值从哪来：
   - `user_id`：方法参数 `userId`，来源是 token。
   - `label_id`、`expiration_time`、`clock_in_cycle`、`remark`：`goal` 对象上的属性，来源是 `CreateGoalRequest`，在 Service 里拷进 `Goal`。
   - `target_clock_in_count`：Service 按周期和到期时间算好后 `set` 到 `Goal` 上，XML 只负责写入 `#{goal.targetClockInCount}`。
   - `clock_in_count`：`Goal.clockInCount` 是 `int`，`Goal.builder()` 没有单独赋值时就是 `0`。XML 把这个 `0` 写进 INSERT。表上的 `DEFAULT 0` 在这条语句里用不上，因为列已经出现在 INSERT 中。
2. 给 `selectUserList` 配三组 query，看日志中的 WHERE：
   - 只有分页。
   - `keyword=test`。
   - `keyword=test&status=1`。
3. 在 `UserMapper.xml` 里找出「更新头像」和「更新状态」两条 `update`，说出它们的 `@Param` 名字。预期是 `id` + `avatarUrl`，以及 `id` + `status`。

## 下课前对一下

- Mapper 接口加 `@Mapper`，XML 的 `namespace` 等于接口全类名，标签 `id` 等于方法名。
- `#{名字}` 来自 `@Param`，或来自单个未命名对象的属性。多参数时属性要写成 `goal.labelId` 这种形式。
- 驼峰配置负责列名到字段名；INSERT 列和值的对应关系写在 SQL 里。
- 自增主键用 `useGeneratedKeys` 写回对象。多个参数时 `keyProperty` 带参数名前缀。
- 动态条件用 `<if>`，分页的 offset 在 Service 算好再传进 `LIMIT`。
