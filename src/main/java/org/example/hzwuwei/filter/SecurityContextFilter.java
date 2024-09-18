package org.example.hzwuwei.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Resource;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.hzwuwei.service.impl.UserDetailServiceImpl;
import org.example.hzwuwei.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * @author 张雨润
 * @date 2024/9/18 下午8:52
 * @Description 获取security存储的用户信息
 */
@Component
public class SecurityContextFilter extends OncePerRequestFilter {

    /**
     * 判断路径是否是白名单，如果不是
     * 解析session中的用户信息，并设置到security上下文
     * @param request
     * @param response
     * @param filterChain
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if ("/users/login".equals(request.getRequestURI())) {
            filterChain.doFilter(request, response);
        } else {
            Object principal = request.getSession().getAttribute("principal");
            if (principal != null) {
                User user = (User) principal;
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);

            }
            filterChain.doFilter(request, response);
        }
    }
}
