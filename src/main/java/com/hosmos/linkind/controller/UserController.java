package com.hosmos.linkind.controller;

import com.hosmos.linkind.models.UserWithPassword;
import com.hosmos.linkind.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

@RestController
@RequestMapping("/user")
public class UserController {

    private UserService userService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(value = "/get/{userName}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public UserWithPassword getUser (@PathVariable(name = "userName") String userName) {
        return userService.getWithEmail(userName);
    }

    @PostMapping("/register")
    public void saveUser( @RequestBody UserWithPassword userWithPassword){
        userService.registerNewUser(userWithPassword);
    }

    @PostMapping("/update")
    public void updateUser(@RequestBody UserWithPassword userWithPassword){
        userService.updateUser(userWithPassword);
    }



}
