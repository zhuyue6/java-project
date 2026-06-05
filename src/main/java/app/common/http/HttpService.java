package app.common.http;
import okhttp3.*;
import java.util.concurrent.TimeUnit;
import app.common.filter.BusinessException;
import app.common.constants.ExceptionCodeEnum;
import app.common.util.UtilService;

public class HttpService {
  private static final OkHttpClient HttpClient;
  static {
    HttpClient = new OkHttpClient.Builder()
                  .connectTimeout(10, TimeUnit.SECONDS)
                  .build();
  }

  public static <T, R> Response post(String url, T data, R params) {
    return request(url, HttpMethod.POST, params, data);
  }
  public static <T, R> Response get(String url, T params) {
    // 发送请求
    return request(url, HttpMethod.POST, params, null);
  }
  /**
   * 
   * @param <T> Object
   * @param <R> Object
   * @param url 请求地址
   * @param Method 请求方法枚举
   * @param params url query 参数对象
   * @param data body data 参数对象
   * @return
   * @throws BusinessException
   */
  public static <T, R> Response request(String url, HttpMethod Method, T params, R data) throws BusinessException {
    // 发送请求
    String queryUrl = url;
    Request request = new Request.Builder().url(queryUrl).get().build();
    if (params != null) {
      queryUrl = UtilService.ToQueryString(url, params);
    }
    switch (Method) {
      case GET:
        request = new Request.Builder().url(queryUrl).get().build();
        break;
      case POST:
        String jsonData = UtilService.objectToString(data);
        RequestBody body = RequestBody.create(
          jsonData, 
          MediaType.parse("application/json; charset=utf-8")
        );
        request = new Request.Builder().url(queryUrl).post(body).build();
        break;
    }

    try (Response response = HttpClient.newCall(request).execute()) {
      return response;
    } catch (Exception e) {
      throw new BusinessException(ExceptionCodeEnum.ServiceError.getCode(), ExceptionCodeEnum.ServiceError.getMessage());
    }
  }
}
