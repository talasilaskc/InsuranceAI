package com.project.InsureAI.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.InsureAI.dto.CreateAISystemRequest;
import com.project.InsureAI.entity.AISystem;
import com.project.InsureAI.security.CustomUserDetailsService;
import com.project.InsureAI.security.JwtUtil;
import com.project.InsureAI.service.AISystemService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AISystemController.class)
@AutoConfigureMockMvc(addFilters = false)
class AISystemControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AISystemService aiSystemService;

    @MockBean
    private JwtUtil jwtUtil;

    @MockBean
    private CustomUserDetailsService customUserDetailsService;

    @Test
    void testCreateAISystem() throws Exception {

        CreateAISystemRequest request = new CreateAISystemRequest();
        request.setName("Fraud AI");

        AISystem system = new AISystem();
        system.setId(1L);
        system.setName("Fraud AI");

        when(aiSystemService.createAISystem(any(CreateAISystemRequest.class)))
                .thenReturn(system);

        mockMvc.perform(post("/api/ai-system")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Fraud AI"));
    }

    @Test
    void testGetMyAISystems() throws Exception {

        AISystem s = new AISystem();
        s.setId(2L);
        s.setName("Risk Prediction AI");

        when(aiSystemService.getMyAISystems()).thenReturn(List.of(s));

        mockMvc.perform(get("/api/ai-system"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(2));
    }

    @Test
    void testGetAISystemById() throws Exception {

        AISystem s = new AISystem();
        s.setId(3L);
        s.setName("Claim Analyzer AI");

        when(aiSystemService.getAISystemById(3L)).thenReturn(s);

        mockMvc.perform(get("/api/ai-system/3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Claim Analyzer AI"));
    }
}