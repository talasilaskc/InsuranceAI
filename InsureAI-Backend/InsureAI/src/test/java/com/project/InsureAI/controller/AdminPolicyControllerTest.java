package com.project.InsureAI.controller;

import com.project.InsureAI.entity.Policy;
import com.project.InsureAI.entity.PolicyStatus;
import com.project.InsureAI.security.CustomUserDetailsService;
import com.project.InsureAI.security.JwtUtil;
import com.project.InsureAI.service.PolicyService;
import com.project.InsureAI.repository.PolicyRepository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AdminPolicyController.class)
@AutoConfigureMockMvc(addFilters = false)
class AdminPolicyControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    PolicyRepository policyRepository;

    @MockBean
    PolicyService policyService;

    @MockBean
    JwtUtil jwtUtil;

    @MockBean
    CustomUserDetailsService customUserDetailsService;

    @Test
    void testGetPendingPolicies() throws Exception {

        Policy p = new Policy();
        p.setId(1L);
        p.setStatus(PolicyStatus.PENDING_APPROVAL);

        when(policyRepository.findByStatus(PolicyStatus.PENDING_APPROVAL))
                .thenReturn(List.of(p));

        mockMvc.perform(get("/api/admin/policies/pending"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1));
    }

    @Test
    void testApprovePolicy() throws Exception {

        Policy p = new Policy();
        p.setId(2L);

        when(policyService.approvePolicy(2L)).thenReturn(p);

        mockMvc.perform(put("/api/admin/policies/2/approve"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(2));
    }
}