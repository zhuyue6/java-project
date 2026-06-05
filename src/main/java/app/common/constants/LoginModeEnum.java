package app.common.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum LoginModeEnum {
  WX("wx"),
  COMMON("common");
  private String mode;
}
