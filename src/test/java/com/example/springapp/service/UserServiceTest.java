package com.example.springapp.service;

import com.example.springapp.domain.User;
import com.example.springapp.dto.UserCreateRequest;
import com.example.springapp.dto.UserResponse;
import com.example.springapp.repository.UserRepository;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    private final UserRepository userRepository = mock(UserRepository.class);
    private final UserService userService = new UserService(userRepository);

    @Test
    void createUser_shouldReturnResponse() {
        UserCreateRequest request = new UserCreateRequest("Teodor", "teo@dev.bg", "Sofia");

        User savedUser = new User();
        savedUser.setId(1L);
        savedUser.setName("Teodor");
        savedUser.setEmail("teo@dev.bg");
        savedUser.setAddress("Sofia");

        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        UserResponse response = userService.createUser(request);

        assertEquals("Teodor", response.getName());
        assertEquals("teo@dev.bg", response.getEmail());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void createUser_shouldThrowOnDuplicateEmail() {
        User existing = new User();
        existing.setEmail("teo@dev.bg");
        existing.setId(1L);
        existing.setName("Teodor");

        when(userRepository.existsByEmail("teo@dev.bg")).thenReturn(true);

        UserCreateRequest request = new UserCreateRequest("Teodor", "teo@dev.bg", "Sofia");

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> userService.createUser(request));
        assertEquals("Email already exists", ex.getMessage());

        verify(userRepository, never()).save(any()); // should not save if duplicate
    }
}