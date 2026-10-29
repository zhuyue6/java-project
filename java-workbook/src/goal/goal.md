一、Java基础核心（必须掌握）

JDK 1.8 核心知识

// 1. Lambda表达式与Stream流
- Lambda语法、函数式接口
- Stream API：map/filter/reduce/collect
- 方法引用、Optional类

// 2. 常用类库
- 集合框架：ArrayList、HashMap（底层原理）
- 并发包：JUC（后面单独说明）
- 时间API：LocalDate/LocalDateTime
- IO/NIO：文件操作、网络编程基础

MyBatis 框架（必须掌握）

<!-- 1. 基础使用 -->
- #{}和${}的区别
- 动态SQL（if、choose、foreach）
- 结果映射（resultMap）
- 关联查询（一对一、一对多）

<!-- 2. 进阶特性 -->
- MyBatis-Plus的使用
- 代码生成器
- 分页插件
- 逻辑删除
- 乐观锁插件

二、Spring生态体系

Spring Boot 基础

// 1. 核心注解
@SpringBootApplication
@RestController / @Service / @Repository
@Autowired / @Resource
@ConfigurationProperties

// 2. 核心功能
- 自动配置原理
- starter机制
- 配置文件（application.yml）
- 日志框架（Logback）
- 单元测试（JUnit）

Spring Cloud 微服务

# 1. 服务注册与发现
- Nacos：注册中心、配置中心
- 服务注册、发现、健康检查
- 配置管理（动态刷新）

# 2. 服务调用
- OpenFeign：声明式HTTP客户端
- 负载均衡：Ribbon
- 服务熔断：Sentinel（可暂缓学习）
  - 流量控制
  - 熔断降级
  - 系统保护

# 3. 网关层
- Spring Cloud Gateway
- 路由配置
- 过滤器（全局/局部）
- 跨域处理
- 限流

# 4. 链路追踪
- SkyWalking
- Sleuth + Zipkin

三、中间件深度理解

MySQL（必须精通）

-- 1. 基础
- 索引原理（B+树）
- 事务ACID、隔离级别
- 锁机制（行锁、表锁、间隙锁）
- MVCC机制

-- 2. 优化
- SQL执行计划（EXPLAIN）
- 慢查询优化
- 分库分表（ShardingSphere）
- 主从复制原理

Redis（必须精通）

// 1. 数据类型及应用
- String：分布式锁
- Hash：对象存储
- List：消息队列
- Set：标签系统
- ZSet：排行榜
- HyperLogLog：UV统计

// 2. 核心机制
- 持久化（RDB/AOF）
- 过期策略
- 淘汰策略（LRU/LFU）
- 缓存穿透/击穿/雪崩解决方案
- 分布式锁实现（Redisson）

Kafka（消息队列，懂原理且会用）

// 1. 核心概念
- Producer/Consumer/Broker
- Topic/Partition
- Consumer Group
- Offset管理

// 2. 可靠性
- 消息丢失场景及解决方案
- 消息重复消费
- 顺序消息
- 事务消息

XXL-JOB（分布式调度，会用即可）

- 任务类型（Bean模式/GLUE模式）
- 路由策略
- 阻塞处理策略
- 任务分片
- 失败重试机制
- 动态任务配置

四、进阶中间件

ElasticSearch（搜索引擎，懂原理即可）

- 倒排索引原理
- 分词器
- 查询DSL
- 聚合分析
- 集群架构
- 与MySQL数据同步（canal/logstash）

EMQX（MQTT物联网，懂原理即可）

发布订阅模式

QoS级别（0,1,2）

保留消息、遗嘱消息

共享订阅

FastDFS（分布式文件，会用即可）

架构（Tracker/Storage）

文件上传下载流程

高可用部署

断点续传

文件同步机制

五、高并发编程（JUC）

核心组件详解

// 1. 并发容器（必须掌握）
- ConcurrentHashMap（重点！）
- CopyOnWriteArrayList
- BlockingQueue（Array/Linked）
- ConcurrentLinkedQueue

// 2. 线程池（必须掌握）
- ThreadPoolExecutor参数详解
- 四种拒绝策略
- 常见的阻塞队列
- 线程池监控

// 3. 锁机制
- ReentrantLock（公平/非公平）
- ReadWriteLock
- Condition

// 4. 同步工具
- CountDownLatch
- CyclicBarrier

// 5. Atomic包（必须掌握）
- AtomicInteger/Long/Boolean

六、运维部署

Nginx（必须掌握）

# 1. 基础配置
- 正向/反向代理
- 负载均衡策略（轮询/权重/ip_hash）

# 2. 高级特性（理解即可）
- SSL证书配置
- 跨域配置
- 缓存配置
- 日志切割

Docker（会用即可）

# 1. 基础命令
- 镜像/容器/仓库
- Dockerfile编写
- 数据卷
- 网络模式

# 2. 编排
- Docker Compose
- 多容器部署
- 环境变量传递
- 日志管理

七、开发工具与规范

必备工具

- Maven/Gradle（依赖管理）
- Git（分支管理、代码审查）
- Jenkins（CI/CD）
- SonarQube（代码质量）
- Postman/Apifox（接口测试）
- Arthas（这个必须掌握）

代码规范

- 阿里编码规约
- 日志规范（log4j2/logback）
- 异常处理规范
- 接口设计规范（RESTful）
- 数据库设计规范

