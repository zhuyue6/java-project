package app.modules.user.service.impl;

import org.springframework.stereotype.Service;
import app.modules.user.dto.request.*;
import app.modules.user.entity.*;
import app.modules.user.mapper.UserMapper;
import app.modules.user.service.UserService;
import jakarta.annotation.Resource;
import app.modules.user.dto.response.UserResponse;
import app.common.filter.*;
import app.common.constants.*;

@Service
public class UserServiceImpl implements UserService {
  // 注入Mapper代理对象
  @Resource
  private UserMapper userMapper;
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
    // if (user == null) {
    //   throw new BusinessException(ExceptionCodeEnum.UserNotFound.getCode(), ExceptionCodeEnum.UserNotFound.getMessage());
    // }

    // if (user.getPassword().equals(dto.getPassword())) {
    //   return convertToResponse(user);
    // }

    throw new BusinessException(ExceptionCodeEnum.UserNameOrPasswordError.getCode(), ExceptionCodeEnum.UserNameOrPasswordError.getMessage());
  }

  @Override
  public void register(UserRegisterRequest dto) {

  }
}