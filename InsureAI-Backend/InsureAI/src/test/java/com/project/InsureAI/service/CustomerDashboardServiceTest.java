package com.project.InsureAI.service;

import com.project.InsureAI.dto.AiRiskSnapshotDTO;
import com.project.InsureAI.dto.CustomerDashboardResponse;
import com.project.InsureAI.dto.PolicySummaryDTO;
import com.project.InsureAI.entity.Company;
import com.project.InsureAI.entity.MyUser;
import com.project.InsureAI.entity.PolicyStatus;
import com.project.InsureAI.entity.RiskLevel;
import com.project.InsureAI.repository.AISystemRepository;
import com.project.InsureAI.repository.ClaimRepository;
import com.project.InsureAI.repository.PolicyRepository;
import com.project.InsureAI.repository.RiskAssessmentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerDashboardServiceTest {

    @Mock
    private PolicyRepository policyRepository;

    @Mock
    private ClaimRepository claimRepository;

    @Mock
    private AISystemRepository aiSystemRepository;

    @Mock
    private RiskAssessmentRepository riskRepository;

    @Mock
    private AuthService authService;

    @InjectMocks
    private CustomerDashboardService dashboardService;

    private MyUser mockUser(Long companyId) {
        Company c = new Company();
        c.setId(companyId);

        MyUser u = new MyUser();
        u.setId(10L);
        u.setCompany(c);
        return u;
    }

    @Test
    void getDashboardData_success_withAlerts() {

        Long companyId = 1L;
        MyUser user = mockUser(companyId);

        PolicySummaryDTO policyDto =
                new PolicySummaryDTO(
                        "Auto Insurance",
                        500000,
                        200000,
                        PolicyStatus.ACTIVE
                );

        AiRiskSnapshotDTO riskDto =
                new AiRiskSnapshotDTO(
                        "Fraud AI",
                        85,
                        RiskLevel.MEDIUM
                );

        when(authService.getCurrentUser()).thenReturn(user);

        when(policyRepository.countActivePoliciesForCompany(companyId))
                .thenReturn(3L);

        when(claimRepository.countClaimsForCompany(companyId))
                .thenReturn(2L);

        when(policyRepository.sumRemainingCoverage(companyId))
                .thenReturn(900000.0);

        when(aiSystemRepository.countByCompanyId(companyId))
                .thenReturn(4L);

        when(claimRepository.getLastPayout(companyId))
                .thenReturn(20000.0);

        when(policyRepository.getPolicySnapshot(companyId))
                .thenReturn(List.of(policyDto));

        when(riskRepository.getRiskSnapshot(companyId))
                .thenReturn(List.of(riskDto));

        CustomerDashboardResponse res =
                dashboardService.getDashboardData();

        assertEquals(3L, res.getActivePolicies());
        assertEquals(2L, res.getClaimsSubmitted());
        assertEquals(900000.0, res.getRemainingCoverage());
        assertEquals(4L, res.getAiSystems());
        assertEquals(20000.0, res.getLastPayout());
        assertEquals(2, res.getAlerts().size());

        verify(policyRepository).countActivePoliciesForCompany(companyId);
        verify(claimRepository).countClaimsForCompany(companyId);
        verify(aiSystemRepository).countByCompanyId(companyId);
        verify(riskRepository).getRiskSnapshot(companyId);
    }

    @Test
    void getDashboardData_success_noAlerts() {

        Long companyId = 1L;
        MyUser user = mockUser(companyId);

        PolicySummaryDTO policyDto =
                new PolicySummaryDTO(
                        "Health Insurance",
                        300000,
                        300000,
                        PolicyStatus.ACTIVE
                );

        AiRiskSnapshotDTO riskDto =
                new AiRiskSnapshotDTO(
                        "Chatbot AI",
                        40,
                        RiskLevel.LOW
                );

        when(authService.getCurrentUser()).thenReturn(user);

        when(policyRepository.countActivePoliciesForCompany(companyId))
                .thenReturn(1L);

        when(claimRepository.countClaimsForCompany(companyId))
                .thenReturn(0L);

        when(policyRepository.sumRemainingCoverage(companyId))
                .thenReturn(300000.0);

        when(aiSystemRepository.countByCompanyId(companyId))
                .thenReturn(1L);

        when(claimRepository.getLastPayout(companyId))
                .thenReturn(0.0);

        when(policyRepository.getPolicySnapshot(companyId))
                .thenReturn(List.of(policyDto));

        when(riskRepository.getRiskSnapshot(companyId))
                .thenReturn(List.of(riskDto));

        CustomerDashboardResponse res =
                dashboardService.getDashboardData();

        assertEquals(0, res.getAlerts().size());

        verify(policyRepository).getPolicySnapshot(companyId);
        verify(riskRepository).getRiskSnapshot(companyId);
    }
}