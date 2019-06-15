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
            @Result(property = "creationDate", column = "creattion_date"),
            @Result(property = "activationDate", column = "activation_date")
    })

    @Select("SELECT * FROM USERS WHERE MAIL = #{mail}")
    UserWithPassword getUser(@Param("mail") String mail) throws SqlSessionException;

    @SelectKey(before = true, statement = "SELECT nextval('SUSERS')", resultType = long.class, keyProperty = "id", keyColumn = "id")
    @Insert("INSERT INTO USERS (id, mail, nick_name, password, creattion_date, activation_date) " +
            "VALUES (#{id}, #{user.mail}, #{user.nickname}, #{user.password}, #{user.creationDate}, #{user.activationDate})")
    void saveUser(@Param("user") UserWithPassword user);
}
