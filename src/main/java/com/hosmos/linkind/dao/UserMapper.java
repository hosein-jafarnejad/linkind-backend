package com.hosmos.linkind.dao;

import com.hosmos.linkind.models.UserWithPassword;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.session.SqlSessionException;

public interface UserMapper {

    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "mail", column = "mail"),
            @Result(property = "nickname", column = "nickname"),
            @Result(property = "creationDate", column = "creationDate"),
            @Result(property = "activationDate", column = "activationDate")
    })

    @Select("SELECT * FROM linkind.public.USERS WHERE MAIL = #{mail}")
    UserWithPassword getUser(@Param("mail") String mail) throws SqlSessionException;
}
