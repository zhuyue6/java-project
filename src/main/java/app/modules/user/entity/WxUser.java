package app.modules.user.entity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class WxUser extends User {
    private String openid;
}