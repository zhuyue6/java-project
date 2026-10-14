## 安装项目依赖
```sh
  mvn install
```

## 构建项目

```sh
  mvn clean package
```

## 启动docker 
1. 需要 docker docker-compose 先安装
2. 安装 mysql 8.0 redis 7-alpine image
3. 构建后端项目镜像 docker build -f clock-my-goal/Dockerfile -t clock-my-goal:0.1.0 .
4. 导出image docker save -o clock-my-goal.tar clock-my-goal:0.1.0
5. 拷贝image 到远端服务器 scp ./clock-my-goal.tar root@114.55.136.107:/opt/apps/clock-my-goal
6. 远端服务器导入镜像 docker load -i /opt/clock-my-goal.tar
7. 拷贝docker目录到远端服务器 scp -r ./clock-my-goal/docker root@114.55.136.107:/opt/apps/clock-my-goal
8. 在该目录使用 docker compose up -d 启动服务

**注意要适配各个版本号，如果更新版本了要调整docker-compose.yaml**