package com.project.InsureAI.controller;

import com.project.InsureAI.dto.AdminDashboardResponse;
import com.project.InsureAI.security.JwtUtil;
import com.project.InsureAI.service.AdminDashboardService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AdminDashboardController.class)
@AutoConfigureMockMvc(addFilters = false)
class AdminDashboardControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    AdminDashboardService adminDashboardService;

    @MockBean
    JwtUtil jwtUtil; // required because JwtFilter depends on it

    @MockBean
    com.project.InsureAI.security.CustomUserDetailsService customUserDetailsService;

    @Test
    void testGetDashboard() throws Exception {

        AdminDashboardResponse response = new AdminDashboardResponse(10, 20, 5, 2, 50000.0, null, null);


        when(adminDashboardService.getDashboardData())
                .thenReturn(response);

        mockMvc.perform(get("/api/admin/dashboard"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalCompanies").value(10))
                .andExpect(jsonPath("$.activePolicies").value(20))
                .andExpect(jsonPath("$.pendingPolicies").value(5))
                .andExpect(jsonPath("$.pendingClaims").value(2))
                .andExpect(jsonPath("$.totalPayout").value(50000.0));
    }
}