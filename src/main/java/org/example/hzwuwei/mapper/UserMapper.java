package org.example.hzwuwei.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.example.hzwuwei.pojo.entity.User;

/**
 * @author 张雨润
 * @date 2024/9/18 下午5:06
 * @Description
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
}
