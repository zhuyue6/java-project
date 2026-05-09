package app.common.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum LoginMode {
  WX("wx"),
  COMMON("common");
  private String mode;
}
