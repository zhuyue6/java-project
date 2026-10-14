package com.zhuyue.clockin.modules.user.controller;
import org.springframework.web.bind.annotation.*;
import com.zhuyue.clockin.modules.user.dto.request.*;
import com.zhuyue.clockin.modules.user.dto.response.*;
import com.zhuyue.clockin.modules.user.service.UserService;
import com.zhuyue.clockin.common.authentication.IgnoreJwt;
import jakarta.annotation.Resource;
import org.springframework.web.multipart.MultipartFile;
import com.zhuyue.clockin.common.interceptor.LoginUser;
import jakarta.validation.Valid;

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
  public UserResponse login(@Valid @RequestBody UserLoginRequest userLoginRequest) {
    UserResponse user = userService.login(userLoginRequest);
    return user;
  }

  @IgnoreJwt
  @PostMapping("/register")
  public long register(@Valid @RequestBody UserRegisterRequest userRegisterRequest) {
    return userService.register(userRegisterRequest);
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
