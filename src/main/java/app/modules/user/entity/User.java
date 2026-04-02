package app.modules.user.entity;
import lombok.Data;

@Data
public class User {
    private Long id;
    private String name;
    private int age;
    private String password;
    private String avatarUrl;
}