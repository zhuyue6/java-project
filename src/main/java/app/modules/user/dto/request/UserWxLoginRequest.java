package app.modules.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserWxLoginRequest {
  @NotBlank(message = "微信登录码不能为空")
  private String code;
}

