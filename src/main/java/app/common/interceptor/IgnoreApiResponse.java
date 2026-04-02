package app.common.interceptor;

import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.TYPE}) // 可用于方法或类
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface IgnoreApiResponse {
    // 标记此注解的接口不进行统一返回格式包装
}