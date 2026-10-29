# 先上「Maven ↔ npm 概念速查表」

先把术语一一对应上，后面讲规则你会非常顺。

表格

| Maven | 类比 npm / 前端工程 | 核心作用 |
| --- | --- | --- |
| `pom.xml` | `package.json` | 项目描述、依赖、配置 |
| `<parent>` 继承 | 近似「配置继承」，npm 没有原生对等物，类似 monorepo 根配置 + 扩展 | 子 POM 继承父 POM 的所有配置、版本、插件 |
| `dependencyManagement` | 类似 `overrides` / `resolutions` + 版本字典 | 只统一定版本，不自动引入依赖 |
| `<dependencies>` | `dependencies` | 真正要引入、参与编译运行的包 |
| `~/.m2/repository` 本地仓库 | 类似 pnpm 全局 store + npm 缓存 `~/.npm` | 所有项目共用的 jar 缓存，下载过就复用 |
| Maven 中央仓库 | npm registry（[npmjs.com](https://npmjs.com)） | 远程包仓库 |
| `<modules>` 聚合 | `workspaces` | monorepo 子模块列表 |
| Reactor 反应堆 | npm/pnpm workspace 构建集 | 本次一起构建的本地模块集合 |
| `-pl 模块名 -am` | `pnpm --filter 包名 build`（自动构建依赖的本地包） | 只构建指定模块，并自动带上它依赖的本地模块 |
| SNAPSHOT 版本 | `beta / next` 预发布版 | 开发中版本，会检查更新 |
| RELEASE 正式版 | `latest` 正式版 | 版本号固定，缓存永久有效 |

> 
> 一个关键差异先点明：
> npm workspace 只有**聚合**（批量管理子包），子包不会自动继承根 `package.json` 的依赖；
> 而 Maven 的 `<parent>` 是**真・配置继承**，父 POM 里的属性、依赖、插件，子模块会全部继承下来。

---

# 一、POM 配置加载与合并规则

对应理解：`package.json` 是怎么一层层找到、怎么合并配置的。

## 1.1 POM 查找顺序（parent 怎么找到）

当子模块写了 `<parent>`，Maven 按这个顺序找父 POM：

1. **先看 `<relativePath>`**（本地文件路径）
   - 默认值是 `../pom.xml`：先去上级目录找父 pom 文件
   - 如果写了 `<relativePath/>`（空标签）：**跳过本地文件，直接去仓库找**
   - 你的根 POM 继承 SpringBoot 父时写了 `<relativePath/>`，就是告诉 Maven：别在本地找，直接去远程仓库拉 `spring-boot-starter-parent`
2. **本地仓库 `~/.m2/repository`**
3. **远程仓库（私服 / 中央仓库）**

> 
> npm 类比：
> 就像你依赖一个包，npm 先看 `node_modules` 里有没有 → 没有就去 registry 搜。
> `<relativePath>` 相当于 `file:../apps` 本地路径依赖，优先读本地文件夹；关掉它就完全走远程仓库。

## 1.2 父子 POM 合并规则（merge 规则）

子 POM 继承父 POM，不是简单覆盖，是**分层合并 + 就近优先**，规则如下：

### ① 属性 `properties`

- 父子属性合并；**子有同名属性，子覆盖父**
- 就像 JS 的 `Object.assign(父属性, 子属性)`

### ② 依赖 `dependencies`

- 父子的依赖列表**合并**
- 同一个 `groupId + artifactId`（同一个包），子声明的版本覆盖父的版本
- 父里声明的依赖，子模块**自动拥有**，不用再写一遍

> 
> npm 差异点：
> npm 子包不会继承根 package.json 的 dependencies，每个包独立；Maven 是真继承，父写了的依赖，子模块直接能用。

### ③ 版本管理 `dependencyManagement`

- 父子合并，子的优先级更高
- **只定版本，不引入包**：就像一个全局版本字典
- 子模块写 `<dependency>` 时可以不写 `<version>`，自动从继承链上的 `dependencyManagement` 取版本

> 
> npm 类比：
> 非常像 pnpm 的 `resolutions` 或 npm 的 `overrides`，统一锁版本；
> 区别是 overrides 会强制生效，`dependencyManagement` 只是提供默认版本，你不引入这个包就不生效。

### ④ 插件 `plugins`

- 合并列表；同插件子配置覆盖 / 追加父配置
- 父里配置的插件，子模块构建时自动生效

### ⑤ 构建目录、资源目录

- 相对路径**相对于当前模块自身目录**，不是父目录
- 比如父写 `src/main/java`，子模块就是 `clock-my-goal/src/main/java`

## 1.3 对应你项目的三层继承链

```
spring-boot-starter-parent（远程，官方版本字典+插件配置）
        ↑ 继承 + 合并
apps 根 POM（你的聚合父工程，加自己的属性、插件）
        ↑ 继承 + 合并
clock-my-goal 子模块（自己的业务依赖）
```

最终 `clock-my-goal` 拿到的是**三层合并后的完整 POM**（叫 effective POM）。

> 
> 想看最终合并结果：
> 
> 
> ```
> mvn help:effective-pom -pl clock-my-goal
> ```
> 
> 
> 类似你在前端想看最终 webpack 配置。

## 1.4 为什么不带 `-am` 会失败（前端视角秒懂）

你的 `apps` 是本地父工程，版本 `1.0.0-SNAPSHOT`，**没发布到远程仓库**。

- **不带 `-am`**：
相当于你 `cd clock-my-goal && npm install`，
npm 看到依赖 `@zhuyue/apps`，直接去 npm 官网搜，搜不到 → 报错。
Maven 同理：只加载 clock-my-goal，去仓库找 `com.zhuyue:apps`，找不到 → `Non-resolvable parent POM`。
- **带 `-am`（also-make）**：
相当于 `pnpm --filter @zhuyue/clock-my-goal build`，
pnpm 知道 `@zhuyue/apps` 是同一个 workspace 里的本地包，**不会去远程搜**，先构建 apps，再构建 clock-my-goal。
Maven 同理：把 apps 加入本次构建内存池，子模块找 parent 时优先在本次构建里命中，不用去仓库。

---

# 二、依赖安装下载规则（对应 npm install）

对应理解：依赖怎么解析、去哪里下、缓存怎么存、版本怎么定。

## 2.1 两种依赖：直接依赖 + 传递依赖

和 npm 完全一样：

- **直接依赖**：你 pom 里 `<dependencies>` 手写的
- **传递依赖**：你依赖的包，它自己依赖的包，自动带进来

例子：

```
你写了：spring-boot-starter-web（直接依赖）
它自动带进来：spring-web、tomcat、jackson...（传递依赖）
```

Maven 会递归展开所有传递依赖，形成一棵依赖树。

> 
> 看依赖树：
> 
> 
> ```
> mvn dependency:tree -pl clock-my-goal
> ```
> 
> 
> 等价于 `npm ls` / `pnpm list`。

## 2.2 版本仲裁规则（同一个包出现多个版本，听谁的）

npm 有 `package-lock.json` 锁版本；Maven 默认没有锁文件，靠**依赖调解规则**定版本，核心两条：

### 规则 1：最短路径优先

哪个版本在依赖树上离你更近，用哪个。

```
你的项目 → A → B → C:1.0  （路径长度 3）
你的项目 → D → C:2.0      （路径长度 2）
```

最终用 `C:2.0`，因为路径更短。

### 规则 2：路径一样长，先声明优先

如果两个版本路径长度相同，谁在 pom 里先出现，用谁的版本。

> 
> 和 npm 对比：
> npm 是扁平化 + lock 文件锁死版本；Maven 是按规则仲裁，没有 lock 时结果确定但不直观。
> 生产级项目一般会加 `maven-dependency-plugin` 做版本锁定，类似 package-lock。

## 2.3 去哪里下载（仓库查找顺序）

和 npm 找包逻辑一致，固定优先级：

1. **本地仓库 `~/.m2/repository`** → 有就直接用，不下
2. **私服 Nexus / 镜像源**（比如阿里云 Maven 镜像）→ 有就下
3. **Maven 中央仓库**（Maven Central）→ 最后兜底

> 
> 对应 npm：
> `node_modules` → `~/.npm` 缓存 → 公司 npm 私服 → npm 官方 registry。
> 区别：Maven 没有项目级 `node_modules`，所有项目共享全局 `.m2` 仓库，更像 pnpm 的全局 store 模式。

## 2.4 什么去「索引」：GAV 坐标定位

Maven 没有单独的包搜索索引服务，它是**按坐标直接拼路径找**：

- 坐标三要素：`groupId` + `artifactId` + `version`（简称 GAV）
- `groupId` 按点拆成目录，比如 `com.zhuyue` → `com/zhuyue`

比如 `com.zhuyue:apps:1.0.0-SNAPSHOT` 的 pom 文件路径就是：

```
~/.m2/repository/com/zhuyue/apps/1.0.0-SNAPSHOT/apps-1.0.0-SNAPSHOT.pom
```

jar 包就是同目录下的 `apps-1.0.0-SNAPSHOT.jar`。

远程仓库也是完全一样的目录结构，直接按路径 HTTP 请求。

> 
> npm 类比：
> npm 是先搜 registry 的 `/-/v1/search` 或 `@scope/name` 元数据接口，拿到 tarball 地址再下载；
> Maven 更朴素：**约定大于配置**，GAV 直接映射文件路径，不用单独搜索引，直接拼 URL 去拉。

## 2.5 正式版 vs 快照版（RELEASE / SNAPSHOT）

### RELEASE 正式版（比如 `3.2.0`）

- 版本号固定，内容不可变
- 本地仓库有了就永远用本地的，不会主动检查更新
- 对应 npm 正式发布版

### SNAPSHOT 快照版（比如 `1.0.0-SNAPSHOT`）

- 开发中版本，内容可以变
- Maven 会定期（默认每天）检查远程有没有更新的快照
- 构建时可以强制更新：`mvn clean package -U`
- 对应 npm 的 `beta / next` 标签

> 
> 你的 `apps:1.0.0-SNAPSHOT` 就是快照版，正常应该只在本地 / 公司内部流转，不发到中央仓库。

## 2.6 缓存什么时候失效

- RELEASE 包：本地有就一直用，除非你手动删 `.m2` 里的文件
- SNAPSHOT 包：默认每天检查更新，或者加 `-U` 强制更新
- 清理本地缓存：
```
mvn dependency:purge-local-repository
```

类似你删 `node_modules` 重装。

---

# 三、一句话串完整流程（对应 npm install）

执行 `mvn -pl clock-my-goal -am clean package` 时：

1. **收集构建集**：找到 clock-my-goal，-am 把父工程 apps 也加进来，排序 apps → clock-my-goal
2. **解析 POM 继承链**：
   - apps 去远程仓库下载 `spring-boot-starter-parent` 的 pom
   - clock-my-goal 从本次构建里拿到 apps 的 pom，合并继承
3. **解析依赖**：展开所有直接依赖 + 传递依赖，按最短路径仲裁版本
4. **下载缺失包**：按本地仓库 → 镜像 → 中央仓库顺序，下载缺的 pom 和 jar
5. **编译打包**：用所有 jar 组成 classpath，编译代码，打出 jar