package app.modules.user.dto.request;

import lombok.Data;

@Data
public class UserRegisterRequest {
  private String name;
  private String password;
  private String openid;
  private String type;
}
