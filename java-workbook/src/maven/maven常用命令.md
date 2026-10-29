```sh
    ## -DskipTests 跳过单元测试
    ## 删除target目录，清空上一次的构建产物
    mvn clean
    ## 编译src/main/java
    mvn compile
    ## 构建jar包 -pl 指定构建相关包
    mvn package
    ## 安装依赖
    mvn install
    ## 查看依赖树
    mvn dependency:tree
```