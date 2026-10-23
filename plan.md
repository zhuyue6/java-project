## V0.2
优先优化现有架构，规范化命名，接口等
1. 优化接口传参以及命名, 适配restful
查询类get pathParam 单个、requestParam 分页

2. 认证单独拿出来, 用户注册调用user服务，因为没有鉴权所有需要新的内部api