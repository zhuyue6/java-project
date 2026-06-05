package app.modules.user.dto.request;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class WxLoginValidateRequest {
  private String appid;
  private String secret;
  private String code;
  private String grant_type;
}
