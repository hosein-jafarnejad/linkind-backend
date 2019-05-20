package com.hosmos.linkind.services;

import com.hosmos.linkind.models.UserWithPassword;
import org.apache.ibatis.session.SqlSessionException;
import org.springframework.stereotype.Service;

@Service("userService")
public interface UserService {
    UserWithPassword getWithUsername(String username) throws SqlSessionException;
}
