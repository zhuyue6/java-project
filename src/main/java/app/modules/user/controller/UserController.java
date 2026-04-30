package app.modules.user.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import app.modules.user.dto.request.*;
import app.modules.user.dto.response.UserResponse;
import app.modules.user.service.UserService;
import jakarta.annotation.Resource;

@RestController // @Controller + @ResponseBody
@RequestMapping("/user")
public class UserController {
  @Resource // Autowired + Qualifier('UserServiceImpl')
  private UserService userService;
  @PostMapping("/login")  // @RequestMapping(value = "/login", method = RequestMethod.Post)
  public UserResponse login(@RequestBody UserLoginRequest userLoginRequest) {
    UserResponse user = userService.login(userLoginRequest);
    return user;
  }
  @PostMapping("/wxLogin")  // @RequestMapping(value = "/login", method = RequestMethod.Post)
  public UserResponse wxLogin(@RequestBody UserWxLoginRequest userLoginRequest) {
    UserResponse user = userService.wxLogin(userLoginRequest);
    return user;
  }
}
