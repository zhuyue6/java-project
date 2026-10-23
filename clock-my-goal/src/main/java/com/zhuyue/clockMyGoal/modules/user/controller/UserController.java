package com.zhuyue.clockMyGoal.modules.user.controller;
import org.springframework.web.bind.annotation.*;

import jakarta.annotation.Resource;
import org.springframework.web.multipart.MultipartFile;

import com.zhuyue.clockMyGoal.common.authentication.IgnoreJwt;
import com.zhuyue.clockMyGoal.common.interceptor.LoginUser;
import com.zhuyue.clockMyGoal.modules.user.dto.request.*;
import com.zhuyue.clockMyGoal.modules.user.dto.response.*;
import com.zhuyue.clockMyGoal.modules.user.service.UserService;
import com.zhuyue.clockMyGoal.modules.user.dto.response.PageResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;

/**
 * 用户
 * UserController
 */
@RestController // @Controller + @ResponseBody 标注为控制器，并返回JSON数据
@RequestMapping("/user") // 映射请求路径
@Validated
public class UserController {
  @Resource(name = "UserService") // Autowired + Qualifier('UserServiceImpl')
  private UserService userService;

  @IgnoreJwt
  @PostMapping("/login")  // @RequestMapping(value = "/login", method = RequestMethod.Post)
  public UserLoginReponse login(@Valid @RequestBody UserLoginRequest userLoginRequest) {
    UserLoginReponse user = userService.login(userLoginRequest);
    return user;
  }

  @IgnoreJwt
  @PostMapping("/register")
  public long register(@Valid @RequestBody UserRegisterRequest userRegisterRequest) {
    return userService.register(userRegisterRequest);
  }

  @GetMapping("/getUserById")
  public UserResponse getUserByUserId(@RequestParam ("id") @NotNull Long userId) {
    return userService.getUserByUserId(userId);
  }
  
  @PostMapping("/getUserList")
  public PageResponse<UserResponse> getUserList(@Valid @RequestBody UserListRequest userListRequest) {
    return userService.getUserList(userListRequest);
  }


  @PostMapping("/logout")
  public void logout(@RequestHeader("Authorization") String authHeader) {
    userService.logout(authHeader);
  }

  @PostMapping("/uploadAvatar")
  public Object uploadAvatar(@RequestParam("file") MultipartFile file, @RequestAttribute("userInfo") LoginUser loginUser) {
    long userId = loginUser.getUserId();
    return userService.uploadAvatar(userId, file);
  }
}
