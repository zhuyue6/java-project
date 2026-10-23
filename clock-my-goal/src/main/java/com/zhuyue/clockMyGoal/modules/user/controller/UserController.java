package com.zhuyue.clockMyGoal.modules.user.controller;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.Valid;
import jakarta.annotation.Resource;
import jakarta.validation.constraints.NotNull;

import com.zhuyue.clockMyGoal.common.interceptor.LoginUser;
import com.zhuyue.clockMyGoal.modules.user.dto.request.*;
import com.zhuyue.clockMyGoal.modules.user.dto.response.*;
import com.zhuyue.clockMyGoal.modules.user.service.UserService;
import com.zhuyue.clockMyGoal.modules.user.dto.response.PageResponse;

/**
 * 用户
 * UserController
 */
@RestController // @Controller + @ResponseBody 标注为控制器，并返回JSON数据
@RequestMapping("/users") // 映射请求路径
@Validated
public class UserController {
  @Resource(name = "UserService") // Autowired + Qualifier('UserServiceImpl')
  private UserService userService;

  @GetMapping("/getUserById")
  public UserResponse getUserByUserId(@RequestParam ("id") @NotNull Long userId) {
    return userService.getUserByUserId(userId);
  }
  
  @PostMapping("/getUserList")
  public PageResponse<UserResponse> getUserList(@Valid @RequestBody UserListRequest userListRequest) {
    return userService.getUserList(userListRequest);
  }

  @PostMapping("/uploadAvatar")
  public Object uploadAvatar(@RequestParam("file") MultipartFile file, @RequestAttribute("userInfo") LoginUser loginUser) {
    long userId = loginUser.getUserId();
    return userService.uploadAvatar(userId, file);
  }
}
