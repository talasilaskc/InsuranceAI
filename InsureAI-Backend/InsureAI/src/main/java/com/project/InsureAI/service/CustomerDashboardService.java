package com.project.InsureAI.service;

import com.project.InsureAI.dto.AiRiskSnapshotDTO;
import com.project.InsureAI.dto.CustomerDashboardResponse;
import com.project.InsureAI.dto.PolicySummaryDTO;
import com.project.InsureAI.entity.Company;
import com.project.InsureAI.repository.PolicyRepository;
import com.project.InsureAI.repository.ClaimRepository;
import com.project.InsureAI.repository.AISystemRepository;
import com.project.InsureAI.repository.RiskAssessmentRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CustomerDashboardService {

    private final PolicyRepository policyRepository;
    private final ClaimRepository claimRepository;
    private final AISystemRepository aiSystemRepository;
    private final RiskAssessmentRepository riskRepository;
    private final AuthService authService;

    public CustomerDashboardService(
            PolicyRepository policyRepository,
            ClaimRepository claimRepository,
            AISystemRepository aiSystemRepository,
            RiskAssessmentRepository riskRepository,
            AuthService authService) {

        this.policyRepository = policyRepository;
        this.claimRepository = claimRepository;
        this.aiSystemRepository = aiSystemRepository;
        this.riskRepository = riskRepository;
        this.authService = authService;
    }

    public CustomerDashboardResponse getDashboardData() {

        Long companyId =
                authService.getCurrentUser().getCompany().getId();

        long activePolicies =
                policyRepository.countActivePoliciesForCompany(companyId);

        long claimsSubmitted =
                claimRepository.countClaimsForCompany(companyId);

        double remainingCoverage =
                policyRepository.sumRemainingCoverage(companyId);

        long aiSystems =
                aiSystemRepository.countByCompanyId(companyId);

        double lastPayout =
                claimRepository.getLastPayout(companyId);

        List<PolicySummaryDTO> policies =
                policyRepository.getPolicySnapshot(companyId);

        List<AiRiskSnapshotDTO> risks =
                riskRepository.getRiskSnapshot(companyId);

        // ⭐ Alerts engine
        List<String> alerts = new ArrayList<>();

        if (risks.stream().anyMatch(r -> r.getRiskScore() > 70))
            alerts.add("High risk AI deployment detected");

        if (claimsSubmitted > 0)
            alerts.add("Claim under review");

        return new CustomerDashboardResponse(
                activePolicies,
                claimsSubmitted,
                remainingCoverage,
                aiSystems,
                lastPayout,
                alerts,
                policies,
                risks
        );
    }
}