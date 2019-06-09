package com.hosmos.linkind.controller;

import com.hosmos.linkind.models.UserWithPassword;
import com.hosmos.linkind.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    private UserService userService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(value = "/{userName}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public String getUser (@PathVariable(name = "userName") String userName) {
        UserWithPassword userWithPassword = userService.getWithUsername(userName);
        System.out.println("------------------------------------ UserController " + userWithPassword.getNickname());
        return userWithPassword.getNickname();
    }

}