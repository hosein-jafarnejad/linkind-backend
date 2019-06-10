package com.hosmos.linkind.dao;

import com.hosmos.linkind.models.UserWithPassword;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.session.SqlSessionException;

@Mapper
public interface UserMapper {

    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "mail", column = "mail"),
            @Result(property = "nickname", column = "nick_name"),
            @Result(property = "password", column = "password"),
            @Result(property = "creationDate", column = "creation_date"),
            @Result(property = "activationDate", column = "activation_date")
    })

    @Select("SELECT * FROM linkind.public.USERS WHERE MAIL = #{mail}")
    UserWithPassword getUser(@Param("mail") String mail) throws SqlSessionException;
}
