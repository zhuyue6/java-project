package app.modules.user.entity;
import lombok.Data;

@Data
public class User {
    private Long id;
    private String nickname;
    private int age;
}