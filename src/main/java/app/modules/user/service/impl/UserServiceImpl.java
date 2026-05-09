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
          .nickname(user.getNickname())
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
    Map<String, Object> params = new HashMap<>();
    params.put("appid", wxAppId);
    params.put("secret", wxSecret);
    params.put("code", wxCode);
    params.put("grant_type", "authorization_code");

    Response res = HttpService.post(wxCode, null, params);
    if (res.openid) {
      throw new BusinessException(ExceptionCodeEnum.UserNotFound.getCode(), ExceptionCodeEnum.UserNotFound.getMessage());
      // 如果获取wx的openid失败的情况
    }
    WxUser wxUser = wxUserMapper.selectByOpenId(res.openid);
    if (wxUser == null) {
      // 如果微信openid不存在的情况下，注册用户保存至用户表中
      UserRegisterRequest registerParams = new UserRegisterRequest();
      long timestamp = System.currentTimeMillis();
      registerParams.setName("用户" + timestamp);
      registerParams.setPassword(null);
      registerParams.set(null);
      register((UserRegisterRequest) registerParams);
    }

    // if (user.getPassword().equals(dto.getPassword())) {
    //   return convertToResponse(user);
    // }

    throw new BusinessException(ExceptionCodeEnum.UserNameOrPasswordError.getCode(), ExceptionCodeEnum.UserNameOrPasswordError.getMessage());
  }

  @Override
  public void register(UserRegisterRequest dto) {

  }
}