package com.project.InsureAI.service;

import com.project.InsureAI.dto.ClaimResponse;
import com.project.InsureAI.dto.OfficerRecommendationRequest;
import com.project.InsureAI.entity.Claim;
import com.project.InsureAI.entity.ClaimStatus;
import com.project.InsureAI.entity.MyUser;
import com.project.InsureAI.exception.BusinessException;
import com.project.InsureAI.exception.ConflictException;
import com.project.InsureAI.exception.ResourceNotFoundException;
import com.project.InsureAI.repository.ClaimRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ClaimOfficerService {

    private final ClaimRepository claimRepository;
    private final AuthService authService;

    public ClaimOfficerService(ClaimRepository claimRepository, AuthService authService) {
        this.claimRepository = claimRepository;
        this.authService = authService;
    }

    @Transactional
    public void startInvestigation(Long claimId) {
        Claim claim = claimRepository.findById(claimId)
                .orElseThrow(() -> new ResourceNotFoundException("Claim not found"));

        MyUser currentUser = authService.getCurrentUser();
        if (claim.getAssignedOfficer() == null || !claim.getAssignedOfficer().getId().equals(currentUser.getId())) {
            throw new BusinessException("You are not assigned to this claim");
        }
        if (claim.getStatus() != ClaimStatus.ASSIGNED_TO_OFFICER) {
            throw new ConflictException("Claim cannot be moved to investigation state from status: " + claim.getStatus());
        }

        claim.setStatus(ClaimStatus.UNDER_INVESTIGATION);
        claim.setInvestigationStartedAt(LocalDateTime.now());
        claimRepository.save(claim);
    }

    @Transactional
    public void recommendPayout(Long claimId, OfficerRecommendationRequest request) {
        Claim claim = claimRepository.findById(claimId)
                .orElseThrow(() -> new ResourceNotFoundException("Claim not found"));

        MyUser currentUser = authService.getCurrentUser();
        if (claim.getAssignedOfficer() == null || !claim.getAssignedOfficer().getId().equals(currentUser.getId())) {
            throw new BusinessException("You are not assigned to this claim");
        }
        if (claim.getStatus() != ClaimStatus.UNDER_INVESTIGATION) {
            throw new ConflictException("Recommendation can only be made when under investigation");
        }

        claim.setRecommendedPayoutAmount(request.getRecommendedPayoutAmount());
        claim.setOfficerRemarks(request.getOfficerRemarks());
        claim.setRecommendationDate(LocalDateTime.now());
        claim.setStatus(ClaimStatus.OFFICER_RECOMMENDED);
        claimRepository.save(claim);
    }

    public List<ClaimResponse> getAssignedClaims() {
        MyUser currentUser = authService.getCurrentUser();
        return claimRepository.findByAssignedOfficer_Id(currentUser.getId()).stream()
                .map(this::mapToResponse)
                .toList();
    }

    private ClaimResponse mapToResponse(Claim c) {
        ClaimResponse res = new ClaimResponse();
        res.setClaimId(c.getId());
        res.setPolicyId(c.getPolicy().getId());
        res.setClaimAmount(c.getClaimAmount());
        res.setPayoutAmount(c.getPayoutAmount());
        res.setStatus(c.getStatus());
        res.setClaimDate(c.getClaimDate());
        res.setVerifiedLoss(c.getVerifiedLoss());
        res.setDescription(c.getIncidentDescription());
        if(c.getAssignedOfficer() != null) res.setAssignedOfficerName(c.getAssignedOfficer().getFullName());
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
