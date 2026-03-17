package com.project.InsureAI.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.InsureAI.entity.InsurancePolicyType;
import com.project.InsureAI.service.PolicyTypeService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AdminPolicyTypeController.class)
class AdminPolicyTypeControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Mock
    PolicyTypeService service;

    @InjectMocks
    AdminPolicyTypeController controller;

    @Test
    void testGetAllPolicyTypes() throws Exception {

        InsurancePolicyType type = new InsurancePolicyType();
        type.setId(1L);
        type.setName("AI Liability");

        when(service.getAllPolicyTypes())
                .thenReturn(List.of(type));

        mockMvc.perform(get("/api/admin/policy-types"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("AI Liability"));
    }

    @Test
    void testCreatePolicyType() throws Exception {

        InsurancePolicyType input = new InsurancePolicyType();
        input.setName("Cyber Risk");

        InsurancePolicyType saved = new InsurancePolicyType();
        saved.setId(2L);
        saved.setName("Cyber Risk");

        when(service.createPolicyType(input)).thenReturn(saved);

        mockMvc.perform(post("/api/admin/policy-types")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Cyber Risk"));
    }
}