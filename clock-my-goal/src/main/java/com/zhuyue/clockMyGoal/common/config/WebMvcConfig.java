package com.zhuyue.clockMyGoal.common.config;

import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.zhuyue.clockMyGoal.common.interceptor.GlobalAuthInterceptor;

import org.springframework.lang.NonNull;

import lombok.RequiredArgsConstructor;

/* 全局拦截器配置，自定义 Spring MVC 的配置，只做增量定制，与 `@EnableWebMvc` 有很大区别 */
@Configuration
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {

    private final GlobalAuthInterceptor globalAuthInterceptor;

    @Value("${file.accessPrefix}")
    private String ACCESS_PREFIX;
    @Value("${file.accessPath}")
    private String ACCESS_PATH;

    @Override
    // 跨域配置
    public void addCorsMappings(@NonNull CorsRegistry registry) {
        registry.addMapping("/admin/**")
                .allowedOriginPatterns("http://localhost:*", "http://127.0.0.1:*")
                .allowedMethods("GET", "POST", "OPTIONS")
                .allowedHeaders("*");
    }

    @Override
    // 注册拦截器
    public void addInterceptors(@NonNull InterceptorRegistry registry) {
        // 必须注入 Spring 容器里的拦截器实例，new 出来的 @Autowired 字段全是 null
        // 全局拦截器必须通过这里注入，不能直接通过注解
        registry.addInterceptor(globalAuthInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns(
                    ACCESS_PREFIX + "/**"  // 静态资源
                );
    }

    /**
     * 把本地上传目录映射到 /static/**，例如：
     * 磁盘 ./resources/images/a.png  → 访问 http://host:port/resources/images/a.png
     */
    @Override
    // 映射静态资源
    public void addResourceHandlers(@NonNull ResourceHandlerRegistry registry) {
        String location = Paths.get(ACCESS_PATH, ACCESS_PREFIX).toAbsolutePath().normalize().toUri().toString();
        if (!location.endsWith("/")) {
            location = location + "/";
        }
        System.out.println("location: " + location);
        registry.addResourceHandler(ACCESS_PREFIX + "/**")
                .addResourceLocations(location);
    }
}
