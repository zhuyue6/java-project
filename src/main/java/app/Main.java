package app;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// 这个注解 = 3 个功能：
// @ComponentScan（自动扫描包）
// @EnableAutoConfiguration（自动配置）
// @SpringBootConfiguration（标记配置类)
@SpringBootApplication
public class Main {
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }
}
