package org.example.hzwuwei.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.example.hzwuwei.filter.SecurityContextFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.*;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.springframework.security.config.Customizer.withDefaults;

/**
 * @author 张雨润
 * @date 2024/9/18 下午5:10
 * @Description
 */
@Slf4j
@Configuration
@EnableMethodSecurity // 开启方法级权限控制
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // 除登录外，都拦截
        http.authorizeHttpRequests(auth -> auth
                .requestMatchers("/users/login")
                .permitAll()
                .anyRequest()
                .authenticated());

        http
                .exceptionHandling(e -> {
                    // 未认证处理
                    e.authenticationEntryPoint(new LoginFailureHandler());
                    // 无权限访问处理
                    e.accessDeniedHandler(new CustomerAccessDeineHandler());
                });
        http.addFilterBefore(new SecurityContextFilter(), UsernamePasswordAuthenticationFilter.class);
        // 禁用浏览器的同源策略限制
        http.headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable));
        // 跨域配置
        http.cors(AbstractHttpConfigurer::disable);
        // 关闭 csrf
        http.csrf(AbstractHttpConfigurer::disable);
        return http.build();
    }


    /**
     * 用户认证器
     * @param authenticationConfiguration
     * @return
     * @throws Exception
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    /**
     * 无密码加密器
     * @return
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }

    /**
     * 登录失败处理
     */
    public static class LoginFailureHandler implements AuthenticationEntryPoint {

        @Override
        public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException, ServletException {
            int code = 500;
            String str = "";
            if (e instanceof AccountExpiredException) {
                str = "账户过期，登录失败";
            } else if (e instanceof BadCredentialsException) {
                str = "用户名或密码错误，登录失败";
            } else if (e instanceof CredentialsExpiredException) {
                str = "密码过期，登录失败";
            } else if (e instanceof DisabledException) {
                str = "账户被禁用，登录失败";
            } else if (e instanceof LockedException) {
                str = "账户锁定，登录失败";
            } else if (e instanceof InternalAuthenticationServiceException) {
                str = "用户名错误或不存在，登录失败";
            } else if (e instanceof InsufficientAuthenticationException) {
                str = "无权限访问资源";
            } else {
                str = "登录失败!";
            }
            response.setContentType("application/json;charset=utf-8");
            ServletOutputStream out = response.getOutputStream();
            out.write(str.getBytes(StandardCharsets.UTF_8));
            out.flush();
            out.close();
        }
    }

    /**
     * 请求无权限处理
     */
    public static class CustomerAccessDeineHandler implements AccessDeniedHandler {
        @Override
        public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
            String msg = "请求无权限";
            response.setContentType("application/json;charset=utf-8");
            ServletOutputStream out = response.getOutputStream();
            out.write(msg.getBytes(StandardCharsets.UTF_8));
            out.flush();
            out.close();
        }
    }

}
