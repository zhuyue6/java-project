package app.modules.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserLoginRequest {
  @NotBlank(message = "名称必须填写")
  private String name;
  @NotBlank(message = "名称必须填写")
  private String password;
}

