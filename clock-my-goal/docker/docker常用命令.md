## 镜像操作
```sh
    ## 查看本地的镜像
    docker images 
    ## 拉取镜像
    docker pull nginx:1.24
    ## 清理镜像
    docker image prune
    ## 删除镜像
    docker rmi nginx:1.24
    ## 构建镜像 -t 打标签
    docker build -t clock-my-goal:0.1.0
    ## 查看镜像分层构建历史
    docker history nginx
    ## 导出镜像为tar包 -o output
    docker save nginx -o nginx.tar
    ## 从tar 包导入镜像 -i --input
    docker load -i nginx.tar
```

## 容器操作
```sh
    ## 容器运行 -d 后台运行；--name 指定容器名；-p 宿主机端口:容器端口；-e 设置环境变量；-v 数据卷挂载 
    docker run -d --name nginx-demo -p 8080:80 nginx:1.24
    ## 查看运行中的容器 -a 所有容器
    docker ps
    ## 强杀容器
    docker kill nginx
    ## 删除容器 -f 强杀
    docker rm nginx
    ## 启动容器
    docker start nginx
    ## 停止容器
    docker stop nginx
    ## 重启容器
    docker restart nginx
```

## 调试
```sh
    ## 进入容器执行 -i标准输入打开 -t分配一个伪终端界面
    docker exec -it nginx nginx -s restart
    ## 查看日志
    docker logs CONTAINER_ID        
```


## 编排操作
需要docker-compose.yaml
```sh
    ## 运行容器服务 -d 后台运行
    docker-compose up  
    ## 停止并删除容器，-v 删除卷
    docker-compose down
    ## 查看日志 -f 实时
    docker-compose logs 
```