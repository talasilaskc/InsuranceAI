package com.project.InsureAI.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.InsureAI.dto.LoginRequest;
import com.project.InsureAI.entity.MyUser;
import com.project.InsureAI.entity.Role;
import com.project.InsureAI.security.JwtUtil;
import com.project.InsureAI.service.AuthService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    AuthService authService;

    @MockBean
    AuthenticationManager authenticationManager;

    @MockBean
    JwtUtil jwtUtil;

    @MockBean
    com.project.InsureAI.security.CustomUserDetailsService customUserDetailsService; // ⭐ required because JwtFilter depends on it

    @Test
    void testLoginSuccess() throws Exception {

        LoginRequest request = new LoginRequest();
        request.setEmail("test@mail.com");
        request.setPassword("1234");

        // ⭐ create dummy user returned by service
        MyUser user = new MyUser();
        user.setEmail("test@mail.com");
        user.setRole(Role.ROLE_COMPANY);

        // ⭐ VERY IMPORTANT mock
        when(authService.getUserByEmail("test@mail.com"))
                .thenReturn(user);

        when(jwtUtil.generateToken(any(), any()))
                .thenReturn("dummy-jwt-token");

        mockMvc.perform(post("/api/auth/login")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("dummy-jwt-token"))
                .andExpect(jsonPath("$.role").value("ROLE_COMPANY"));
    }
}