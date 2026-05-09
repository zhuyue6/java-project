package app.modules.user.entity;
import lombok.Data;

@Data
public class WxUser extends User {
    private String openid;
}