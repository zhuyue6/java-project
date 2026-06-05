package app.modules.user.dto.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserRegisterRequest {
  private String name;
  private String password;
  private String openid;
  private String type;
}
