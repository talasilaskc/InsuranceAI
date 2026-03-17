package com.project.InsureAI.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.InsureAI.entity.InsurancePolicyType;
import com.project.InsureAI.service.PolicyTypeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(
        controllers = PolicyTypeController.class,
        excludeAutoConfiguration = {
                org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class,
                org.springframework.boot.autoconfigure.security.servlet.SecurityFilterAutoConfiguration.class
        }
)
class PolicyTypeControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    PolicyTypeService service;

    @MockBean
    com.project.InsureAI.security.JwtUtil jwtUtil; // ⭐ required because JwtFilter depends on it

    @MockBean
    com.project.InsureAI.security.CustomUserDetailsService customUserDetailsService;

    @Test
    void testGetActivePolicyTypes() throws Exception {

        InsurancePolicyType type = new InsurancePolicyType();
        type.setId(1L);
        type.setName("AI Liability");

        when(service.getActivePolicyTypes())
                .thenReturn(List.of(type));

        mockMvc.perform(get("/api/policy-types"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("AI Liability"));
    }
}