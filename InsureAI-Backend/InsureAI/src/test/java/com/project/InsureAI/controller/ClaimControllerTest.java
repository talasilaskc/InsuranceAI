package com.project.InsureAI.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.InsureAI.dto.ClaimApprovalRequest;
import com.project.InsureAI.dto.ClaimResponse;
import com.project.InsureAI.dto.SubmitClaimRequest;
import com.project.InsureAI.service.ClaimService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(
        controllers = ClaimController.class,
        excludeAutoConfiguration = {
                org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class,
                org.springframework.boot.autoconfigure.security.servlet.SecurityFilterAutoConfiguration.class
        }
)
class ClaimControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    ClaimService claimService;

    @MockBean
    com.project.InsureAI.security.JwtUtil jwtUtil; // ⭐ required because JwtFilter depends on it

    @MockBean
    com.project.InsureAI.security.CustomUserDetailsService customUserDetailsService;

    @Test
    void testSubmitClaim() throws Exception {

        SubmitClaimRequest request = new SubmitClaimRequest();
        request.setPolicyId(1L);
        request.setClaimAmount(10000);
        request.setDescription("AI system failure");

        ClaimResponse response = new ClaimResponse();
        response.setClaimId(10L);
        response.setDescription("AI system failure");

        when(claimService.submitClaim(any(SubmitClaimRequest.class)))
                .thenReturn(response);

        mockMvc.perform(post("/api/claims/submit")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.claimId").value(10L));
    }

    @Test
    void testApproveClaim() throws Exception {

        ClaimApprovalRequest request = new ClaimApprovalRequest();
        request.setVerifiedLoss(5000);

        ClaimResponse response = new ClaimResponse();
        response.setClaimId(5L);
        response.sertVerifiedLoss(5000);

        when(claimService.approveClaim(5L, 5000)).thenReturn(response);

        mockMvc.perform(put("/api/claims/admin/5/approve")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.claimId").value(5));
    }

    @Test
    void testRejectClaim() throws Exception {

        ClaimResponse response = new ClaimResponse();
        response.setClaimId(7L);

        when(claimService.rejectClaim(7L)).thenReturn(response);

        mockMvc.perform(put("/api/claims/admin/7/reject"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.claimId").value(7));
    }

    @Test
    void testGetClaimsForPolicy() throws Exception {

        ClaimResponse response = new ClaimResponse();
        response.setClaimId(1L);

        when(claimService.getClaimsForPolicy(1L))
                .thenReturn(List.of(response));

        mockMvc.perform(get("/api/claims/policy/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].claimId").value(1));
    }

    @Test
    void testGetClaimsForCompany() throws Exception {

        ClaimResponse response = new ClaimResponse();
        response.setClaimId(2L);

        when(claimService.getClaimsForCompany())
                .thenReturn(List.of(response));

        mockMvc.perform(get("/api/claims/company"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].claimId").value(2));
    }

    @Test
    void testGetAllClaimsForAdmin() throws Exception {

        ClaimResponse response = new ClaimResponse();
        response.setClaimId(3L);

        when(claimService.getAllClaimsForAdmin())
                .thenReturn(List.of(response));

        mockMvc.perform(get("/api/claims/admin/allClaims"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].claimId").value(3));
    }
}