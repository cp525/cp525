package com.czxy.dao;

import com.czxy.domain.User;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@org.apache.ibatis.annotations.Mapper
public interface UserDao  extends Mapper<User> {

    @Select("select * from user where username=#{username} and password = #{password}")
    public List<User> findUserByNameAndPsw(User user);
}
