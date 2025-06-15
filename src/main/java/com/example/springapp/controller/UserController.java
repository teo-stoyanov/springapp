package com.example.springapp.controller;

import com.example.springapp.dto.UserCreateRequest;
import com.example.springapp.dto.UserResponse;
import com.example.springapp.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse registerUser(@Valid @RequestBody UserCreateRequest request) {
        return userService.createUser(request);
    }
}