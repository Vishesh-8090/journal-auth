package com.vishesh.journal.auth.controller;

import com.vishesh.journal.auth.dto.LoginRequest;
import com.vishesh.journal.auth.dto.RegisterRequest;
import com.vishesh.journal.auth.dto.UserResponse;
import com.vishesh.journal.auth.entity.User;
import com.vishesh.journal.auth.service.UserService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    private UserService userService;

    public UserController(UserService userService){
        this.userService = userService;
    }

    @PostMapping("register")
    public UserResponse register(@Valid @RequestBody RegisterRequest request){
        return userService.registerUser(request);
    }

    @PostMapping("login")
    public UserResponse login(@Valid @RequestBody LoginRequest request){
        return userService.loginUser(request.getEmail(), request.getPassword());
    }
}
