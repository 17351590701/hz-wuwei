package org.example.hzwuwei.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import jakarta.annotation.Resource;
import org.example.hzwuwei.mapper.UserMapper;
import org.example.hzwuwei.pojo.entity.User;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @author 张雨润
 * @date 2024/9/18 下午5:10
 * @Description
 */
@Component()
public class UserDetailServiceImpl implements UserDetailsService {
    @Resource
    private UserMapper userMapper;

    /**
     * 根据用户名查询用户信息
     * 为简化RDBC ，直接赋值空权限列表，模拟登录后的用户
     * @param username
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userMapper.selectOne(new QueryWrapper<User>().eq("username", username));
        if (user == null) {
            throw new UsernameNotFoundException("用户名不存在");
        } else {
            // 空权限列表
            Collection<GrantedAuthority> authorities = new ArrayList<>();
            // 组装security中的User对象
            return new org.springframework.security.core.userdetails.User(
                    user.getUsername(),
                    user.getPassword(),
                    true,// 用户账户是否过期
                    true,// 用户凭证是否过期
                    true,// 用户是否锁定
                    true,// 是否可用
                    authorities// 权限列表
            );
        }
    }
}
