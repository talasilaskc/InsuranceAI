package com.project.InsureAI.service;

import com.project.InsureAI.dto.ClaimResponse;
import com.project.InsureAI.dto.SubmitClaimRequest;
import com.project.InsureAI.entity.*;
import com.project.InsureAI.exception.ResourceNotFoundException;
import com.project.InsureAI.exception.BusinessException;
import com.project.InsureAI.repository.ClaimRepository;
import com.project.InsureAI.repository.PolicyRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ClaimService {
    private final ClaimRepository claimRepository;
    private final PolicyRepository policyRepository;
    private final AuthService authService;

    public ClaimService(ClaimRepository claimRepository,
                        PolicyRepository policyRepository,
                        AuthService authService) {
        this.claimRepository = claimRepository;
        this.policyRepository = policyRepository;
        this.authService = authService;
    }

    @Transactional
    public ClaimResponse submitClaim(SubmitClaimRequest request) {
        //Get logged-in company
        Company company = authService.getCurrentUser().getCompany();

        //Fetch policy and validate ownership
        Policy policy = policyRepository.findById(request.getPolicyId())
                .orElseThrow(() -> new ResourceNotFoundException("Policy not found"));
        
        if (!policy.getAiSystem().getCompany().getId().equals(company.getId())) {
            throw new BusinessException("Unauthorized policy access");
        }

        // Validate active policy
        if (!policy.isActive() || policy.getStatus() != PolicyStatus.ACTIVE) {
            throw new BusinessException("Policy is not active");
        }

        if (request.getClaimAmount() <= 0) {
            throw new BusinessException("Invalid claim amount");
        }

        Claim claim = new Claim();
        claim.setPolicy(policy);
        claim.setCompany(company);
        claim.setClaimAmount(request.getClaimAmount());
        claim.setClaimDate(LocalDate.now());
        claim.setIncidentDescription(request.getDescription());
        claim.setStatus(ClaimStatus.CLAIM_RAISED);
        claim.setPayoutAmount(0);
        claim.setVerifiedLoss(0);

        Claim saved = claimRepository.save(claim);

        return mapToResponse(saved);
    }

    public List<ClaimResponse> getClaimsForPolicy(Long policyId) {
        Company company = authService.getCurrentUser().getCompany();
        Policy policy = policyRepository.findById(policyId)
                .orElseThrow(() -> new ResourceNotFoundException("Policy not found"));

        if (!policy.getAiSystem().getCompany().getId().equals(company.getId())) {
            throw new BusinessException("Unauthorized access");
        }

        return claimRepository.findByPolicy(policy).stream()
                .map(this::mapToResponse)
                .toList();
    }

    public List<ClaimResponse> getClaimsForCompany() {
        Company company = authService.getCurrentUser().getCompany();
        return claimRepository.findByPolicy_AiSystem_Company(company).stream()
                .map(this::mapToResponse)
                .toList();
    }

    public ClaimResponse getClaimById(Long id) {
        Company company = authService.getCurrentUser().getCompany();
        Claim claim = claimRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Claim not found"));

        if (!claim.getPolicy().getAiSystem().getCompany().getId().equals(company.getId())) {
            throw new BusinessException("Unauthorized access");
        }

        return mapToResponse(claim);
    }

    public List<ClaimResponse> getAllClaimsForAdmin() {
        return claimRepository.findAll().stream()
                .map(this::mapToResponse)
                .toList();
    }

    public ClaimResponse mapToResponse(Claim c) {
        ClaimResponse res = new ClaimResponse();
        res.setClaimId(c.getId());
        res.setPolicyId(c.getPolicy().getId());
        res.setClaimAmount(c.getClaimAmount());
        res.setPayoutAmount(c.getPayoutAmount());
        res.setStatus(c.getStatus());
        res.setClaimDate(c.getClaimDate());
        res.setVerifiedLoss(c.getVerifiedLoss());
        res.setDescription(c.getIncidentDescription());
        
        if(c.getAssignedOfficer() != null) {
            res.setAssignedOfficerName(c.getAssignedOfficer().getFullName());
        }
        
        res.setOfficerRemarks(c.getOfficerRemarks());
        res.setRecommendedPayoutAmount(c.getRecommendedPayoutAmount());
        res.setAdminRemarks(c.getAdminRemarks());
        res.setInvestigationStartedAt(c.getInvestigationStartedAt());
        res.setRecommendationDate(c.getRecommendationDate());
        res.setApprovalDate(c.getApprovalDate());

        if (c.getPolicy() != null) {
            res.setPolicyName(c.getPolicy().getPolicyType().getName());
            if (c.getPolicy().getAiSystem() != null) {
                res.setAiSystemName(c.getPolicy().getAiSystem().getName());
                if (c.getPolicy().getAiSystem().getCompany() != null) {
                    res.setCompanyName(c.getPolicy().getAiSystem().getCompany().getName());
                }
            }
            res.setCoverageAmount(c.getPolicy().getCoverageLimit());
            res.setRemainingCoverage(c.getPolicy().getRemainingCoverage());
        }
        
        return res;
    }
}
