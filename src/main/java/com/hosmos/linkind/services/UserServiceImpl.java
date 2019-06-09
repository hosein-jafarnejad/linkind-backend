package com.hosmos.linkind.services;

import com.hosmos.linkind.dao.UserMapper;
import com.hosmos.linkind.models.UserWithPassword;
import org.apache.ibatis.session.SqlSessionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class UserServiceImpl implements UserService {

    private UserMapper userMapper;

    @Autowired
    public void setUserMapper(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    // *********************** methods ***********************

    @Override
    @Transactional(readOnly = true)
    public UserWithPassword getWithUsername(String username) throws SqlSessionException {
        return userMapper.getUser(username);
    }
}
