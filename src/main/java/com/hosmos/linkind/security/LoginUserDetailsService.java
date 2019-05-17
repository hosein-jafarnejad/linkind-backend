package com.hosmos.linkind.security;

import com.hosmos.linkind.models.UserWithPassword;
import com.hosmos.linkind.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class LoginUserDetailsService implements UserDetailsService {

    // *********************** variables ***********************
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private UserService userService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }



    // *********************** methods ***********************
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        logger.trace("START_AUTHENTICATION.[USERNAME:" + username + "]");
        UserWithPassword loadedUser;
        try {
            loadedUser = new UserWithPassword();

            logger.info("AUTHENTICATION_SUCCESSFUL.[USERNAME:" + username + "]");
        } catch (Exception e) {
            logger.error("AUTHENTICATION_FAILED.[USERNAME:" + username + "]", e);
            throw new UsernameNotFoundException("نام کاربری یافت نشد.");
        }

        return new AuthenticatedUser(loadedUser);
    }
}
