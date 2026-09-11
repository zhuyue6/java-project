package com.zhuyue.clockin;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import com.zhuyue.clockin.common.interceptor.GlobalAuthInterceptor;
import lombok.RequiredArgsConstructor;

/* 全局拦截器配置 */
@Configuration
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {

    private final GlobalAuthInterceptor globalAuthInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 必须注入 Spring 容器里的拦截器实例，new 出来的 @Autowired 字段全是 null
        registry.addInterceptor(globalAuthInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/user/login", "/user/register", "/static/**", "/error");
    }
}
