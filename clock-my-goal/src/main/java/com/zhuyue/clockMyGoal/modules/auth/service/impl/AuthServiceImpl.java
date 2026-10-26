package com.zhuyue.clockMyGoal.modules.auth.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import com.zhuyue.clockMyGoal.modules.auth.dto.request.*;
import com.zhuyue.clockMyGoal.modules.auth.dto.response.*;
import com.zhuyue.clockMyGoal.modules.user.dto.response.*;
import com.zhuyue.clockMyGoal.modules.user.dto.request.CreateUserRequest;
import com.zhuyue.clockMyGoal.modules.user.dto.request.UserCredentialRequest;
import com.zhuyue.clockMyGoal.modules.auth.service.AuthService;
import com.zhuyue.clockMyGoal.modules.user.service.UserService;
import com.zhuyue.clockMyGoal.common.authentication.JwtService;
import com.zhuyue.clockMyGoal.common.authentication.BlackListService;
import com.zhuyue.clockMyGoal.common.exception.BusinessException;
import com.zhuyue.clockMyGoal.common.constants.ExceptionCodeEnum;

@Service("AuthService")
public class AuthServiceImpl implements AuthService {
  @Autowired
  private JwtService jwtService;
  @Autowired
  private BlackListService blackListService;
  @Autowired
  private UserService userService;

  @Override
  public LoginResponse login(LoginDto dto) {
    // 根据用户名查询用户
    UserCredentialRequest userCredentialDto = userService.getUserCredentialByUserName(dto.getUserName());

    // 如果用户不存在，则抛出异常
    if (userCredentialDto == null) {
      throw new BusinessException(ExceptionCodeEnum.UserNotFound);
    }

    // 如果密码不匹配，则抛出异常
    if (!userCredentialDto.getPassword().equals(dto.getPassword())) {
      throw new BusinessException(ExceptionCodeEnum.UserNameOrPasswordError);
    }

    // 如果用户状态为禁用，则抛出异常
    if (userCredentialDto.getStatus() == 0) {
      throw new BusinessException(ExceptionCodeEnum.UserStatusDisabled);
    }

    // 更新redis中的用户状态
    userService.updateUserStatusInRedis(userCredentialDto.getId(), userCredentialDto.getStatus());

    // 生成令牌
    String token = jwtService.generateToken(userCredentialDto.getId(), userCredentialDto.getUserName());
    
    // 获取用户信息
    UserResponse userResponse = userService.getUserByUserId(userCredentialDto.getId());

    // 组装登录响应对象
    LoginResponse loginReponse = new LoginResponse();
    loginReponse.setToken(token);
    loginReponse.setUserName(userCredentialDto.getUserName());
    loginReponse.setId(userCredentialDto.getId());
    loginReponse.setStatus(userCredentialDto.getStatus());
    loginReponse.setAvatarUrl(userResponse.getAvatarUrl());
    loginReponse.setAge(userResponse.getAge());
    loginReponse.setSex(userResponse.getSex());
    loginReponse.setCreateTime(userResponse.getCreateTime());
    loginReponse.setUpdateTime(userResponse.getUpdateTime());

    return loginReponse;
  }

  @Override
  public void logout(String authHeader) {
    String token = jwtService.getToken(authHeader);
    // 计算token剩余过期秒数
    if (token == null) {
      throw new BusinessException(ExceptionCodeEnum.Unauthorized);
    }

    // 能进入这里肯定是没过期的token, 过期的会被拦截器拦截
    long remainSeconds = jwtService.getTokenRemainSeconds(token);
    if (remainSeconds > 0) {
      // 如果还有剩余时间，则将token加入黑名单
      blackListService.addTokenToBlacklist(token, remainSeconds);
    }
  }

  @Override
  public Long register(RegisterDto dto) {
    // 根据用户名查询用户
    UserCredentialRequest userCredential = userService.getUserCredentialByUserName(dto.getUserName());
    if (userCredential != null) {
      // 如果已经存在账号, 直接抛出异常
      throw new BusinessException(ExceptionCodeEnum.UserAlreadyExists);
    }

    CreateUserRequest createUserDto = new CreateUserRequest();
    createUserDto.setUserName(dto.getUserName());
    createUserDto.setPassword(dto.getPassword());
    createUserDto.setSex(dto.getSex());
    createUserDto.setAge(dto.getAge());

    long userId = userService.createUser(createUserDto);
    return userId;
  }
}
