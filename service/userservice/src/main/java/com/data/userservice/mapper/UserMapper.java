package com.data.userservice.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.data.userservice.model.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * UserMapper
 * @author suncong
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

}




