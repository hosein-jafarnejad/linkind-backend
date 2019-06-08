package com.hosmos.linkind.dao;

import com.hosmos.linkind.models.UserWithPassword;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.session.SqlSessionException;

@Mapper
public interface UserMapper {
    @Select("SELECT * FROM linkind.public.USERS WHERE MAIL = #{mail}")
    UserWithPassword getUser(@Param("mail") String mail) throws SqlSessionException;
}
