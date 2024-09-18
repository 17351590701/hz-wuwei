package org.example.hzwuwei.service;

import com.baomidou.mybatisplus.extension.service.IService;
import jakarta.servlet.http.HttpServletRequest;
import org.example.hzwuwei.pojo.dto.LoginDto;
import org.example.hzwuwei.pojo.entity.User;

/**
 * @author 张雨润
 * @date 2024/9/18 下午5:08
 * @Description
 */
public interface UserService extends IService<User> {
    String login(LoginDto loginDto, HttpServletRequest request);
}
