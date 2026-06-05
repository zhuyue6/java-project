package app.modules.user.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import app.modules.user.dto.request.*;
import app.modules.user.entity.*;
import app.modules.user.mapper.*;
import app.modules.user.service.UserService;
import jakarta.annotation.Resource;
import okhttp3.Response;
import app.modules.user.dto.response.UserResponse;
import app.common.filter.*;
import app.common.constants.*;
import app.common.http.*;

@Service
public class UserServiceImpl implements UserService {
  // 注入Mapper代理对象
  @Resource
  private UserMapper userMapper;
   // 注入Mapper代理对象
  @Resource
  private WxUserMapper wxUserMapper;
  @Value("${wx.appid}")
  private String wxAppId;
  @Value("${wx.secret}")
  private String wxSecret;
  private UserResponse convertToResponse(User user) {
      return UserResponse.builder()
          .id(user.getId())
          .name(user.getName())
          .age(user.getAge())
          .build();
  }
  private UserResponse convertToResponse(WxUser user) {
      return UserResponse.builder()
          .id(user.getId())
          .name(user.getName())
          .age(user.getAge())
          .build();
  }

  // 登录接口
  @Override
  public UserResponse login(UserLoginRequest dto) {
    // User user = userMapper.selectByName(dto.getName());
    // if (user == null) {
    //   throw new BusinessException(ExceptionCodeEnum.UserNotFound.getCode(), ExceptionCodeEnum.UserNotFound.getMessage());
    // }

    // if (user.getPassword().equals(dto.getPassword())) {
    //   return convertToResponse(user);
    // }

    throw new BusinessException(ExceptionCodeEnum.UserNameOrPasswordError.getCode(), ExceptionCodeEnum.UserNameOrPasswordError.getMessage());
  }

  @Override
  public UserResponse wxLogin(UserWxLoginRequest dto) {
    String wxCode = dto.getCode();
    // String wxCode = dto.getCode();
    WxLoginValidateRequest params = WxLoginValidateRequest.builder()
                                        .appid(wxAppId)
                                        .secret(wxSecret)
                                        .code(wxCode)
                                        .grant_type("authorization_code")
                                        .build();

    Response res = HttpService.post(wxCode, null, params);
    if (res.openid) {
      throw new BusinessException(ExceptionCodeEnum.UserNotFound.getCode(), ExceptionCodeEnum.UserNotFound.getMessage());
      // 如果获取wx的openid失败的情况
    }
    WxUser wxUser = wxUserMapper.selectByOpenId(res.openid);
    if (wxUser == null) {
      // 如果微信openid不存在的情况下，注册用户保存至用户表中
      long timestamp = System.currentTimeMillis();
      UserRegisterRequest registerParams = UserRegisterRequest.builder()
                                            .name("用户" + timestamp)
                                            .password("123456")
                                            .type("wx")
                                            .openid(res.openid)
                                            .build();
                                            
      register((UserRegisterRequest) registerParams, LoginModeEnum.WX);
    }

    return convertToResponse(wxUser);

    // throw new BusinessException(ExceptionCodeEnum.UserNameOrPasswordError.getCode(), ExceptionCodeEnum.UserNameOrPasswordError.getMessage());
  }

  @Override
  public void register(UserRegisterRequest dto) {

  }
  @Override
  public void register(UserRegisterRequest dto, LoginModeEnum mode) {
    if (LoginModeEnum.WX.equals(mode)) {
      // 如果是微信登录的情况
      WxUser wxUser = wxUserMapper.selectByOpenId(dto.getOpenid());
      if (wxUser != null) {
        // 如果存在微信账号, 直接抛出异常
        throw new BusinessException(ExceptionCodeEnum.UserAlreadyExists.getCode(), ExceptionCodeEnum.UserAlreadyExists.getMessage());
      }
      wxUserMapper
    }
  }
}