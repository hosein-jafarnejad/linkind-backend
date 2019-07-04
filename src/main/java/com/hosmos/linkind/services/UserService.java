package com.hosmos.linkind.services;

import com.hosmos.linkind.models.UserWithPassword;
import org.apache.ibatis.session.SqlSessionException;
import org.springframework.stereotype.Service;

@Service("userService")
public interface UserService {

    UserWithPassword getWithEmail(String emailAddress);

    void registerNewUser(UserWithPassword userWithPassword);

    void updateUser(UserWithPassword userWithPassword);
}
