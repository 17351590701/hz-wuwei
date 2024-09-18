package org.example.hzwuwei.pojo.dto;

import lombok.Data;

/**
 * @author 张雨润
 * @date 2024/9/18 下午5:03
 * @Description 接受前端用户登录信息
 */
@Data
public class LoginDto {
    private String username;
    private String password;
}
