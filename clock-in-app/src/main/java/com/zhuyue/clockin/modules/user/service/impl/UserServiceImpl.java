package com.zhuyue.clockin.modules.user.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.zhuyue.clockin.common.authentication.JwtService;
import com.zhuyue.clockin.modules.user.dto.request.*;
import com.zhuyue.clockin.modules.user.entity.*;
import com.zhuyue.clockin.modules.user.mapper.*;
import com.zhuyue.clockin.modules.user.service.UserService;
import com.zhuyue.clockin.modules.user.dto.response.UserResponse;
import com.zhuyue.clockin.common.exception.*;
import com.zhuyue.clockin.common.constants.*;
import com.zhuyue.clockin.common.authentication.BlackListService;
import jakarta.annotation.Resource;
import java.util.Objects;
import org.springframework.web.multipart.MultipartFile;
import com.zhuyue.clockin.common.storeage.*;
import java.io.IOException;

/**
 * 用户服务实现类
 * UserServiceImpl
 */
@Service("UserService")
public class UserServiceImpl implements UserService {
  // 注入Mapper代理对象
  @Resource()
  private UserMapper userMapper;
  @Autowired
  private JwtService jwtService;
  @Autowired
  private BlackListService blackListService;
  @Autowired
  private StoreageService storeageService;


  private UserResponse convertToResponse(User user, String token) {
    // 使用Builder模式将用户信息转换为响应对象
    return UserResponse.builder()
        .id(user.getId())
        .userName(user.getUserName())
        .age(user.getAge())
        .sex(user.getSex())
        .avatarUrl(user.getAvatarUrl())
        .token(token)
        .build();
  }

  // 登录接口
  @Override
  public UserResponse login(UserLoginRequest dto) throws BusinessException {
    // 根据用户名查询用户
    User user = userMapper.selectByName(dto.getUserName());
    // 如果用户不存在，则抛出异常
    if (user == null) {
      throw new BusinessException(ExceptionCodeEnum.UserNotFound.getCode(), ExceptionCodeEnum.UserNotFound.getMessage());
    }

    // 如果密码不匹配，则抛出异常
    if (!user.getPassword().equals(dto.getPassword())) {
      throw new BusinessException(ExceptionCodeEnum.UserNameOrPasswordError.getCode(), ExceptionCodeEnum.UserNameOrPasswordError.getMessage());
    }

    // 生成令牌
    String token = jwtService.generateToken(user.getId(), user.getUserName());

    return convertToResponse(user, token);
  }

  @Override
  public void register(UserRegisterRequest dto) throws BusinessException {
    // 根据用户名查询用户
    User user = userMapper.selectByName(dto.getUserName());
    if (user != null) {
      // 如果已经存在账号, 直接抛出异常
      throw new BusinessException(ExceptionCodeEnum.UserAlreadyExists.getCode(), ExceptionCodeEnum.UserAlreadyExists.getMessage());
    } 

    int sex = Objects.requireNonNullElse(dto.getSex(), 3);
    User newUser = User.builder()
      .userName(dto.getUserName())
      .password(dto.getPassword())
      .sex(sex)
      .age(dto.getAge())
      .build();

    userMapper.createUser(newUser);
  }

  @Override
  public void logout(String authHeader) {
    String token = jwtService.getToken(authHeader);
    // 计算token剩余过期秒数
    long remainSeconds = jwtService.getTokenRemainSeconds(token);

    if (remainSeconds > 0) {
      // 如果还有剩余时间，则将token加入黑名单
      blackListService.addTokenToBlacklist(token, remainSeconds);
    }
  }

  @Override
  public String uploadAvatar(MultipartFile file) {
    String avatarUrl = null;
    try {
      avatarUrl = storeageService.uploadImage(file);
    } catch (BusinessException e) {
      throw new BusinessException(e.getCode(), e.getMessage());
    } catch (IOException e) {
      throw new BusinessException(ExceptionCodeEnum.FileUploadError.getCode(), ExceptionCodeEnum.FileUploadError.getMessage());
    }
    return avatarUrl;
  }
}