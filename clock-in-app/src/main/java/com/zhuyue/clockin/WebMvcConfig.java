package com.zhuyue.clockin;

import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.lang.NonNull;
import com.zhuyue.clockin.common.interceptor.GlobalAuthInterceptor;
import lombok.RequiredArgsConstructor;

/* 全局拦截器配置 */
@Configuration
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {

    private final GlobalAuthInterceptor globalAuthInterceptor;

    @Value("${file.accessPrefix}")
    private String ACCESS_PREFIX;

    @Override
    public void addInterceptors(@NonNull InterceptorRegistry registry) {
        // 必须注入 Spring 容器里的拦截器实例，new 出来的 @Autowired 字段全是 null
        registry.addInterceptor(globalAuthInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/user/login", "/user/register", "/static/**", "/error");
    }

    /**
     * 把本地上传目录映射到 /static/**，例如：
     * 磁盘 ./static/images/a.png  → 访问 http://host:port/static/images/a.png
     */
    @Override
    public void addResourceHandlers(@NonNull ResourceHandlerRegistry registry) {
        String location = Paths.get("." + ACCESS_PREFIX).toAbsolutePath().normalize().toUri().toString();
        if (!location.endsWith("/")) {
            location = location + "/";
        }
        registry.addResourceHandler(ACCESS_PREFIX + "/**")
                .addResourceLocations(location);
    }
}
