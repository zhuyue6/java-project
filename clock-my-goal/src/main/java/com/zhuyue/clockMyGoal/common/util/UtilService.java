package com.zhuyue.clockMyGoal.common.util;

import java.lang.reflect.Field;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zhuyue.clockMyGoal.common.constants.ExceptionCodeEnum;
import com.zhuyue.clockMyGoal.common.exception.BusinessException;

public class UtilService {
  private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
  public static String objectToString(Object obj) {
    try {
      return OBJECT_MAPPER.writeValueAsString(obj);
    } catch (Exception e) {
      throw new BusinessException(ExceptionCodeEnum.ServiceError.getCode(), ExceptionCodeEnum.ServiceError.getMessage());
    }
  }
  public static <T> String ToQueryString(String url, T obj) {
    try {
      String queryStr = objectToQueryString(obj);
      String finalUrl = url.contains("?") ? url + "&" + queryStr : url + "?" + queryStr;
      return finalUrl;
    } catch (Exception e) {
      throw new BusinessException(ExceptionCodeEnum.ServiceError.getCode(), ExceptionCodeEnum.ServiceError.getMessage());
    }
  }
  private static String objectToQueryString(Object obj) throws IllegalAccessException {
    StringBuilder sb = new StringBuilder();
    Field[] fields = obj.getClass().getDeclaredFields();
    for (Field field : fields) {
      Object value = field.get(obj);
      if (value != null) {
        if (!sb.isEmpty()) {
          sb.append("&");
        }
        sb.append(field.getName()).append("=").append(value);
      }
    }
    return sb.toString();
  }

  public static <T> T mergeObject(T objTarget, T objMerge) throws IllegalAccessException, InstantiationException {
    T obj = (T) objTarget.getClass().newInstance();
    Field[] fields = objTarget.getClass().getDeclaredFields();
    
    for (Field field : fields) {
      field.setAccessible(true);
      Object value = field.get(objTarget);
      field.set(obj, value);
      Object valueMerge = field.get(objMerge);
      field.set(obj, valueMerge);
    }

    return obj;
  }
}
