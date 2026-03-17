package com.project.InsureAI.service;

import com.project.InsureAI.entity.MyUser;
import com.project.InsureAI.exception.BusinessException;
import com.project.InsureAI.exception.ResourceNotFoundException;
import com.project.InsureAI.repository.MyUserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    MyUserRepository userRepository;

    @Mock
    Authentication authentication;

    @InjectMocks
    AuthService authService;

    @Test
    void testGetCurrentUserSuccess() {

        MyUser user = new MyUser();
        user.setEmail("test@mail.com");

        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getName()).thenReturn("test@mail.com");

        SecurityContextHolder.getContext().setAuthentication(authentication);

        when(userRepository.findByEmail("test@mail.com"))
                .thenReturn(Optional.of(user));

        MyUser result = authService.getCurrentUser();

        assertEquals("test@mail.com", result.getEmail());
    }

    @Test
    void testUserNotAuthenticated() {

        SecurityContextHolder.clearContext();

        assertThrows(BusinessException.class,
                () -> authService.getCurrentUser());
    }

    @Test
    void testUserNotFound() {

        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getName()).thenReturn("unknown@mail.com");

        SecurityContextHolder.getContext().setAuthentication(authentication);

        when(userRepository.findByEmail("unknown@mail.com"))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> authService.getCurrentUser());
    }
}