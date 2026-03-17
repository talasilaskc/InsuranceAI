package com.project.InsureAI.dto;

import com.project.InsureAI.entity.ClaimStatus;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class ClaimResponse {
    private Long claimId;
    private Long policyId;
    private double claimAmount;
    private double payoutAmount;
    private ClaimStatus status;
    private LocalDate claimDate;
    private double verifiedLoss;
    private double coverageAmount;
    private double remainingCoverage;
    private String description;
    private String policyName;
    private String companyName;
    private String aiSystemName;

    private String assignedOfficerName;
    private String officerRemarks;
    private double recommendedPayoutAmount;
    private String adminRemarks;
    private LocalDateTime investigationStartedAt;
    private LocalDateTime recommendationDate;
    private LocalDateTime approvalDate;

    public ClaimResponse() {}

    public Long getClaimId() { return claimId; }
    public void setClaimId(Long claimId) { this.claimId = claimId; }

    public Long getPolicyId() { return policyId; }
    public void setPolicyId(Long policyId) { this.policyId = policyId; }

    public double getClaimAmount() { return claimAmount; }
    public void setClaimAmount(double claimAmount) { this.claimAmount = claimAmount; }

    public double getPayoutAmount() { return payoutAmount; }
    public void setPayoutAmount(double payoutAmount) { this.payoutAmount = payoutAmount; }

    public ClaimStatus getStatus() { return status; }
    public void setStatus(ClaimStatus status) { this.status = status; }

    public LocalDate getClaimDate() { return claimDate; }
    public void setClaimDate(LocalDate claimDate) { this.claimDate = claimDate; }

    public double getVerifiedLoss() { return verifiedLoss; }
    public void setVerifiedLoss(double verifiedLoss) { this.verifiedLoss = verifiedLoss; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getAssignedOfficerName() { return assignedOfficerName; }
    public void setAssignedOfficerName(String name) { this.assignedOfficerName = name; }

    public String getOfficerRemarks() { return officerRemarks; }
    public void setOfficerRemarks(String remarks) { this.officerRemarks = remarks; }

    public double getRecommendedPayoutAmount() { return recommendedPayoutAmount; }
    public void setRecommendedPayoutAmount(double amount) { this.recommendedPayoutAmount = amount; }

    public String getAdminRemarks() { return adminRemarks; }
    public void setAdminRemarks(String remarks) { this.adminRemarks = remarks; }

    public LocalDateTime getInvestigationStartedAt() { return investigationStartedAt; }
    public void setInvestigationStartedAt(LocalDateTime dt) { this.investigationStartedAt = dt; }

    public LocalDateTime getRecommendationDate() { return recommendationDate; }
    public void setRecommendationDate(LocalDateTime dt) { this.recommendationDate = dt; }

    public LocalDateTime getApprovalDate() { return approvalDate; }
    public void setApprovalDate(LocalDateTime dt) { this.approvalDate = dt; }

    public String getPolicyName() { return policyName; }
    public void setPolicyName(String policyName) { this.policyName = policyName; }

    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }

    public String getAiSystemName() { return aiSystemName; }
    public void setAiSystemName(String aiSystemName) { this.aiSystemName = aiSystemName; }

    public double getCoverageAmount() { return coverageAmount; }
    public void setCoverageAmount(double coverageAmount) { this.coverageAmount = coverageAmount; }

    public double getRemainingCoverage() { return remainingCoverage; }
    public void setRemainingCoverage(double remainingCoverage) { this.remainingCoverage = remainingCoverage; }
}
