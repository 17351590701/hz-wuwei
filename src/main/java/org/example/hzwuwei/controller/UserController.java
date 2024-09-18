package org.example.hzwuwei.controller;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.example.hzwuwei.pojo.dto.LoginDto;
import org.example.hzwuwei.service.UserService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;


/**
 * @author 张雨润
 * @date 2024/9/18 下午4:54
 * @Description 用户类
 */
@RestController
@RequestMapping("/users")
public class UserController {
    @Resource
    private UserService  userService;


    @GetMapping("/hello")
    public String hello() {
        return "Hello World";
    }

    @PostMapping("/login")
    public String UserLogin(@RequestBody LoginDto loginDto, HttpServletRequest request) {
        return userService.login(loginDto,request);
    }


}
