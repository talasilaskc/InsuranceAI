package com.project.InsureAI.service;

import com.project.InsureAI.dto.AdminDashboardResponse;
import com.project.InsureAI.dto.PolicyRevenueDTO;
import com.project.InsureAI.dto.ClaimTrendDTO;
import com.project.InsureAI.entity.PolicyStatus;
import com.project.InsureAI.entity.ClaimStatus;
import com.project.InsureAI.repository.PolicyRepository;
import com.project.InsureAI.repository.ClaimRepository;
import com.project.InsureAI.repository.CompanyRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminDashboardService {

    private final PolicyRepository policyRepository;
    private final ClaimRepository claimRepository;
    private final CompanyRepository companyRepository;

    public AdminDashboardService(PolicyRepository policyRepository,
                                 ClaimRepository claimRepository,
                                 CompanyRepository companyRepository) {
        this.policyRepository = policyRepository;
        this.claimRepository = claimRepository;
        this.companyRepository = companyRepository;
    }

    public AdminDashboardResponse getDashboardData() {

        // ⭐ KPI Counts
        long totalCompanies = companyRepository.count();

        long activePolicies =
                policyRepository.countByIsActiveTrue();

        long pendingPolicies =
                policyRepository.countByStatus(PolicyStatus.PENDING_APPROVAL);

        long pendingClaims =
                claimRepository.countByStatus(ClaimStatus.CLAIM_RAISED);

        double totalPayout =
                claimRepository.getTotalPayout();

        // ⭐ Mini Analytics
        List<PolicyRevenueDTO> revenueByProduct =
                policyRepository.getRevenueByPolicyType();

        List<ClaimTrendDTO> claimTrend =
                claimRepository.getClaimTrend();

        return new AdminDashboardResponse(
                totalCompanies,
                activePolicies,
                pendingPolicies,
                pendingClaims,
                totalPayout,
                revenueByProduct,
                claimTrend
        );
    }
}