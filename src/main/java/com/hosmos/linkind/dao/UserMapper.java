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

    @Select("SELECT * FROM USERS WHERE mail = #{mail}")
    UserWithPassword getUser(@Param("mail") String mail) throws SqlSessionException;

    @SelectKey(before = true, statement = "SELECT nextval('SUSERS')", resultType = long.class, keyProperty = "id", keyColumn = "id")
    @Insert("INSERT INTO USERS (id, mail, nick_name, password, creation_date) " +
            "VALUES (#{id}, #{user.mail}, #{user.nickname}, #{user.password}, #{user.creationDate})")
    void saveUser(@Param("user") UserWithPassword user);

    @Update("UPDATE USERS SET " +
            "mail = #{user.mail}, nick_name = #{user.nickname}, password = #{user.password}, " +
            "creation_date = #{user.creationDate} WHERE id = #{user.id}")
    void updateUser(@Param("user") UserWithPassword user);
}
