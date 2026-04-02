package app.common.interceptor;

import lombok.Data;

@Data
public class ApiResponse<T> {
  private int code;
  private String message;
  private T data;
  public static <T> ApiResponse<T> success(T data) {
    ApiResponse<T> result = new ApiResponse<>();
    result.code = 0;
    result.data = data;
    return result;
  }
  public static <T> ApiResponse<T> fail(String message) {
    ApiResponse<T> result = new ApiResponse<>();
    result.code = 0;
    result.message = message;
    result.data = null;
    return result;
  }
  public static <T> ApiResponse<T> fail(String message, int code) {
    ApiResponse<T> result = new ApiResponse<>();
    result.code = code;
    result.message = message;
    result.data = null;
    return result;
  }
}
