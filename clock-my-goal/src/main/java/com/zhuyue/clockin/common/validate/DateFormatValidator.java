package com.zhuyue.clockin.common.validate;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;


public class DateFormatValidator implements ConstraintValidator<DateFormat, String> {
  private String pattern;

  @Override
  public void initialize(DateFormat constraintAnnotation) {
    // 获取注解中的value
    pattern = constraintAnnotation.value();
  }
  
  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    // 如果值为空，则返回true
    if (value == null) {
      return true;
    }
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
    try {
      formatter.parse(value);
    } catch (DateTimeParseException e) {
      return false;
    }
    return true;
  }
}
