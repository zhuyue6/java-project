package com.zhuyue.clockin.modules.user.controller;
import org.springframework.web.bind.annotation.*;
import com.zhuyue.clockin.modules.user.dto.request.*;
import com.zhuyue.clockin.modules.user.dto.response.*;
import com.zhuyue.clockin.modules.user.service.UserService;
import com.zhuyue.clockin.common.authentication.IgnoreJwt;
import jakarta.annotation.Resource;
import org.springframework.web.multipart.MultipartFile;

/**
 * 用户
 * UserController
 */
@RestController // @Controller + @ResponseBody 标注为控制器，并返回JSON数据
@RequestMapping("/user") // 映射请求路径
public class UserController {
  @Resource(name = "UserService") // Autowired + Qualifier('UserServiceImpl')
  private UserService userService;

  @IgnoreJwt
  @PostMapping("/login")  // @RequestMapping(value = "/login", method = RequestMethod.Post)
  public UserResponse login(@RequestBody UserLoginRequest userLoginRequest) {
    UserResponse user = userService.login(userLoginRequest);
    return user;
  }

  @IgnoreJwt
  @PostMapping("/register")
  public void register(@RequestBody UserRegisterRequest userRegisterRequest) {
    userService.register(userRegisterRequest);
  }
  @PostMapping("/logout")
  public void logout(@RequestHeader("Authorization") String authHeader) {
    userService.logout(authHeader);
  }

  @PostMapping("/uploadAvatar")
  public void uploadAvatar(@RequestBody(required = true) MultipartFile file) {
    userService.uploadAvatar(file);
  }
}
