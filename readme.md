## 安装项目依赖
```sh
  mvn install
```

## 构建项目

```sh
  mvn clean package
```

## 启动docker 
1. 需要docker docker-compose 先安装
2. 安装mysql 8.0 redis 7-alpine image
3. 拷贝docker 目录使用 docker compose up -d