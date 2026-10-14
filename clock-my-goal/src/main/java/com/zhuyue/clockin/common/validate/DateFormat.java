package com.zhuyue.clockin.common.validate;

import java.lang.annotation.Target;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

/**
 * 日期校验注解
 * 用于校验日期格式是否为yyyy-MM-dd HH:mm:ss
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = DateFormatValidator.class)
public @interface DateFormat {
  Class<?>[] groups() default {};
  Class<? extends Payload>[] payload() default {};
  String value() default "yyyy-MM-dd HH:mm:ss";
  String message() default "日期格式不正确";
}
