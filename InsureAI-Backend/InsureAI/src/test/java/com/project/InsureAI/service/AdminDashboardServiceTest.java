package com.project.InsureAI.service;

import com.project.InsureAI.dto.AdminDashboardResponse;
import com.project.InsureAI.dto.PolicyRevenueDTO;
import com.project.InsureAI.dto.ClaimTrendDTO;
import com.project.InsureAI.entity.PolicyStatus;
import com.project.InsureAI.entity.ClaimStatus;
import com.project.InsureAI.repository.PolicyRepository;
import com.project.InsureAI.repository.ClaimRepository;
import com.project.InsureAI.repository.CompanyRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class AdminDashboardServiceTest {

    @Mock
    PolicyRepository policyRepository;

    @Mock
    ClaimRepository claimRepository;

    @Mock
    CompanyRepository companyRepository;

    @InjectMocks
    AdminDashboardService service;

    @Test
    void testGetDashboardData() {

        // ⭐ KPI mocks
        when(companyRepository.count()).thenReturn(10L);
        when(policyRepository.countByIsActiveTrue()).thenReturn(20L);
        when(policyRepository.countByStatus(PolicyStatus.PENDING_APPROVAL)).thenReturn(5L);
        when(claimRepository.countByStatus(ClaimStatus.RAISED)).thenReturn(3L);
        when(claimRepository.getTotalPayout()).thenReturn(50000.0);

        // ⭐ Analytics mocks
        PolicyRevenueDTO revenueDTO =
                new PolicyRevenueDTO("AI Liability", 100000, 50L);

        ClaimTrendDTO trendDTO =
                new ClaimTrendDTO(ClaimStatus.APPROVED, 4L);

        when(policyRepository.getRevenueByPolicyType())
                .thenReturn(List.of(revenueDTO));

        when(claimRepository.getClaimTrend())
                .thenReturn(List.of(trendDTO));

        // ⭐ call service
        AdminDashboardResponse response =
                service.getDashboardData();

        // ⭐ assertions
        assertEquals(10, response.getTotalCompanies());
        assertEquals(20, response.getActivePolicies());
        assertEquals(5, response.getPendingPolicies());
        assertEquals(3, response.getPendingClaims());
        assertEquals(50000.0, response.getTotalPayout());

        assertEquals(1, response.getRevenueByProduct().size());
        assertEquals("AI Liability",
                response.getRevenueByProduct().get(0).getPolicyTypeName());

        assertEquals(1, response.getClaimTrend().size());
        assertEquals(ClaimStatus.APPROVED,
                response.getClaimTrend().get(0).getStatus());
        assertEquals(4L,
                response.getClaimTrend().get(0).getCount());
    }
}