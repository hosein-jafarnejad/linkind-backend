package com.hosmos.linkind.services;

import com.hosmos.linkind.dao.UserMapper;
import com.hosmos.linkind.models.UserWithPassword;
import org.apache.ibatis.session.SqlSessionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@Component
public class UserServiceImpl implements UserService {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private UserMapper userMapper;

    @Autowired
    public void setUserMapper(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    // *********************** methods ***********************

    @Override
    @Transactional(readOnly = true)
    public UserWithPassword getWithUsername(String username) {
        logger.info("UserServiceImpl is going to get username with [" + username + "] email");
        return userMapper.getUser(username);
    }

    @Override
    public void createNewUser(UserWithPassword userWithPassword) {
        logger.info("UserServiceImpl is going to createNewUser with [" + userWithPassword.getNickname() + "] nickName");
        userMapper.saveUser(userWithPassword);
    }
}
