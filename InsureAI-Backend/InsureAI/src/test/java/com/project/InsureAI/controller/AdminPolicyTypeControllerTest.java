package com.project.InsureAI.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.InsureAI.entity.InsurancePolicyType;
import com.project.InsureAI.security.CustomUserDetailsService;
import com.project.InsureAI.security.JwtUtil;
import com.project.InsureAI.service.PolicyTypeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AdminPolicyTypeController.class)
@AutoConfigureMockMvc(addFilters = false)
class AdminPolicyTypeControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    PolicyTypeService service;

    @MockBean
    JwtUtil jwtUtil; // ⭐ required because JwtFilter depends on it

    @MockBean
    CustomUserDetailsService customUserDetailsService;

    @Test
    void stGetAllPolicyTypes() throws Exception {

        InsurancePolicyType type = new InsurancePolicyType();
        type.setId(1L);
        type.setName("AI Liability");

        when(service.getAllPolicyTypes()).thenReturn(List.of(type));

        mockMvc.perform(get("/api/admin/policy-types"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("AI Liability"));
    }

    @Test
    void testCreatePolicyType() throws Exception {

        InsurancePolicyType saved = new InsurancePolicyType();
        saved.setId(2L);
        saved.setName("Cyber Risk");

        when(service.createPolicyType(org.mockito.ArgumentMatchers.any()))
                .thenReturn(saved);

        mockMvc.perform(post("/api/admin/policy-types")
                        .contentType("application/json")
                        .content("""
                        {
                          "name":"Cyber Risk"
                        }
                    """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Cyber Risk"));
    }
}