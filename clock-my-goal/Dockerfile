# clock-in-app 镜像
#
# 多阶段构建：
#   1. builder 阶段用带 JDK 和 Maven 的镜像编译打包
#   2. 运行阶段只保留 JRE 和打好的 jar，镜像更小，也不带编译工具
#
# 必须在仓库根目录构建。clock-in-app 的 parent 是根目录的 pom.xml，
# 构建上下文如果只包含 clock-in-app 目录，Maven 找不到父 POM。
#
#   docker build -f clock-in-app/Dockerfile -t clock-in-app:1.0.0 .
#
# 和 MySQL、Redis 一起启动（环境变量已写在 docker-compose.yaml）：
#
#   cd clock-in-app/docker && docker compose up -d --build

# ---------- 编译阶段 ----------
# maven 镜像自带 Maven 3.9.9 和 Temurin JDK 17，和项目的 Java 版本一致。
# AS builder 给这一阶段起名，后面用 --from=builder 取出产物。
FROM maven:3.9.9-eclipse-temurin-17 AS builder

# 容器内的工作目录。后面的 COPY、RUN 都相对这个目录。
WORKDIR /workspace

# 先拷父 POM，再拷本模块 POM 和源码。
# Docker 按指令顺序做缓存：pom 或源码变了才会重新执行后面的 mvn。
COPY pom.xml pom.xml
COPY clock-my-goal/pom.xml clock-my-goal/pom.xml
COPY clock-my-goal/src clock-my-goal/src

# -B：批处理模式，不出现交互提示，适合 CI 和镜像构建
# -pl clock-my-goal：只构建这个模块
# -am：同时构建它依赖的模块（这里会带上父工程）
# -DskipTests：镜像构建时不跑测试
# --mount=type=cache：把本机 Maven 本地仓库挂进构建，依赖下载可以复用，
# 不会写进最终镜像层
RUN --mount=type=cache,target=/root/.m2 \
    mvn -B -pl clock-my-goal -am clean package -DskipTests

# ---------- 运行阶段 ----------
# 只带 JRE 的 Ubuntu Jammy 镜像，没有 JDK 和 Maven。
# 新的 FROM 会丢掉上一阶段的层，只保留下面显式 COPY 进来的文件。
FROM eclipse-temurin:17-jre-jammy

# 应用工作目录。日志和上传文件不写在这里，路径见下面两个目录。
WORKDIR /app

# 不用 root 跑 Java 进程。
# groupadd/useradd --system：创建系统用户，没有登录密码
# --shell /usr/sbin/nologin：禁止交互登录
# 目录与运行配置对齐：
#   application.yaml  logging.file.name = /var/logs/clock-my-goal/clock-my-goal-app.log
#   docker-compose    FILE_ACCESS_PATH=/opt/clock-my-goal，文件写到 /opt/clock-my-goal/resources
# compose 会把宿主机目录挂到这两个路径上，挂载后的属主是宿主机用户（一般是 root）。
# 镜像构建时的 chown 会被挂载盖掉，所以这里只先建好目录；真正改属主放在启动命令里。
RUN groupadd --system app \
    && useradd --system --gid app --home-dir /app --shell /usr/sbin/nologin app \
    && mkdir -p /opt/clock-my-goal/resources /var/logs/clock-my-goal \
    && chown -R app:app /app /opt/clock-my-goal /var/logs/clock-my-goal

# 从编译阶段拷出 Spring Boot 可执行 jar，并改属主为 app。
# 通配符匹配版本号，升版本后不用改这一行。
# spring-boot-maven-plugin 打出来的是 fat jar，里面已经包含依赖。
COPY --from=builder --chown=app:app \
    /workspace/clock-my-goal/target/clock-my-goal-*.jar /app/app.jar

# 声明容器监听 9090。这只是文档，真正端口由 SERVER_PORT 和 application.yaml 决定。
# 映射到宿主机要在 docker run -p 或 compose 的 ports 里配置。
EXPOSE 9090

# TZ：系统时区和日志时间
# SERVER_PORT：覆盖 application.yaml 里的 ${SERVER_PORT:9090}
# JAVA_TOOL_OPTIONS：JVM 启动时自动读取，不用改 ENTRYPOINT
#   user.timezone：Java 时间与 Asia/Shanghai 对齐
#   UseContainerSupport：按容器内存限制计算堆大小，而不是按宿主机内存
ENV TZ=Asia/Shanghai \
    SERVER_PORT=9090 \
    JAVA_TOOL_OPTIONS="-Duser.timezone=Asia/Shanghai -XX:+UseContainerSupport"

# 启动时仍是 root，才能 chown 被挂载盖住的目录。
# chown 之后 runuser 会 exec 到 java，java 以 app 运行，并且是 1 号进程，docker stop 能把 SIGTERM 传给它。
# 数据库、Redis、JWT、微信相关变量没有默认值，启动前必须从外部传入，见 docker-compose.yaml。
ENTRYPOINT ["sh", "-c", "chown -R app:app /opt/clock-my-goal /var/logs/clock-my-goal && exec /usr/sbin/runuser -u app -- java -jar /app/app.jar"]
