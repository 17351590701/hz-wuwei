package org.example.hzwuwei.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.micrometer.common.util.StringUtils;
import jakarta.annotation.Resource;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.example.hzwuwei.mapper.UserMapper;
import org.example.hzwuwei.pojo.dto.LoginDto;
import org.example.hzwuwei.pojo.entity.User;
import org.example.hzwuwei.service.UserService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

/**
 * @author 张雨润
 * @date 2024/9/18 下午5:32
 * @Description
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Resource
    private AuthenticationManager authenticationManager;

    @Override
    public String login(LoginDto loginDto, HttpServletRequest request) {
        if (loginDto == null || StringUtils.isBlank(loginDto.getUsername()) || StringUtils.isBlank(loginDto.getPassword())) {
            return "请输入用户名或密码";
        }
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword());
        Authentication authenticate = authenticationManager.authenticate(usernamePasswordAuthenticationToken);
        if (ObjectUtil.isNotNull(authenticate)) {
            // 设置认证用户信息到上下文
            SecurityContextHolder.getContext().setAuthentication(authenticate);
        }
        HttpSession session = request.getSession(true);
        Object principal = authenticate.getPrincipal();
        session.setAttribute("principal", principal);
        return "登录成功";
    }
}
