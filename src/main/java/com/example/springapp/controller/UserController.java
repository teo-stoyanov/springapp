package com.example.springapp.controller;

import com.example.springapp.dto.UserCreateRequest;
import com.example.springapp.dto.UserResponse;
import com.example.springapp.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Tag(name = "User Management", description = "Operations related to user registration and management")
public class UserController {

    private final UserService userService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Register a new user", description = "Creates a new user account with the provided information")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User successfully created"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "409", description = "User already exists")
    })
    public UserResponse registerUser(@Valid @RequestBody UserCreateRequest request) {
        return userService.createUser(request);
    }
}