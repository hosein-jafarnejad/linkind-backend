package com.hosmos.linkind.services;

import com.hosmos.linkind.dao.UserMapper;
import com.hosmos.linkind.models.UserWithPassword;
import org.apache.ibatis.session.SqlSessionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.WebRequest;

@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@Component
public class UserServiceImpl implements UserService {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private UserMapper userMapper;

    @Autowired
    public void setUserMapper(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    // *********************** Override methods ***********************

    @Override
    @Transactional(readOnly = true)
    public UserWithPassword getWithEmail(String emailAddress) {
        logger.info("UserServiceImpl is going to get user with [" + emailAddress + "] email");
        return userMapper.getUser(emailAddress);
    }

    @Override
    public void registerNewUser(UserWithPassword userWithPassword) {
        logger.info("UserServiceImpl is going to registerNewUser with [" + userWithPassword.getNickname() + "] nickName");
        logger.info("check if email address doesn't exists on DB");
        if(emailExist(userWithPassword.getMail())){
            //TODO throw EmailExistException with a message
        }else{
            // todo first send a verification email.
            userMapper.saveUser(userWithPassword);
        }
    }

    @Override
    public void updateUser(UserWithPassword userWithPassword) {
        logger.info("UserServiceImpl is going to update user with id [" + userWithPassword.getId() + "]" );
        userMapper.updateUser(userWithPassword);
    }


    // *********************** helper methods ***********************

    /**
     *
     * @param email
     * @return true if email exists in Database
     */
    private boolean emailExist(String email){
        UserWithPassword user = this.getWithEmail(email);
        if(user != null){
            return true;
        }else{
            return false;
        }
    }


}
