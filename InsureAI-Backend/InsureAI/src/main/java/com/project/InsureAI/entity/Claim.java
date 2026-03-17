package com.project.InsureAI.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
public class Claim {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "policy_id", nullable = false)
    private Policy policy;

    // who raised claim
    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;

    // assigned officer
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assigned_officer_id")
    private MyUser assignedOfficer;

    private double claimAmount;
    private double payoutAmount;
    private double verifiedLoss;

    private LocalDate claimDate;

    // COMPANY SIDE
    @Column(length = 1000)
    private String incidentDescription;

    // OFFICER SIDE
    @Column(length = 1000)
    private String officerRemarks;

    private double recommendedPayoutAmount;

    private LocalDateTime investigationStartedAt;

    private LocalDateTime recommendationDate;

    private LocalDateTime approvalDate;

    // ADMIN SIDE
    @Column(length = 1000)
    private String adminRemarks;

    @Enumerated(EnumType.STRING)
    @Column(length = 50)
    private ClaimStatus status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Policy getPolicy() {
        return policy;
    }

    public void setPolicy(Policy policy) {
        this.policy = policy;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public MyUser getAssignedOfficer() {
        return assignedOfficer;
    }

    public void setAssignedOfficer(MyUser assignedOfficer) {
        this.assignedOfficer = assignedOfficer;
    }

    public double getClaimAmount() {
        return claimAmount;
    }

    public void setClaimAmount(double claimAmount) {
        this.claimAmount = claimAmount;
    }

    public double getPayoutAmount() {
        return payoutAmount;
    }

    public void setPayoutAmount(double payoutAmount) {
        this.payoutAmount = payoutAmount;
    }

    public double getVerifiedLoss() {
        return verifiedLoss;
    }

    public void setVerifiedLoss(double verifiedLoss) {
        this.verifiedLoss = verifiedLoss;
    }

    public LocalDate getClaimDate() {
        return claimDate;
    }

    public void setClaimDate(LocalDate claimDate) {
        this.claimDate = claimDate;
    }

    public String getIncidentDescription() {
        return incidentDescription;
    }

    public void setIncidentDescription(String incidentDescription) {
        this.incidentDescription = incidentDescription;
    }

    public String getOfficerRemarks() {
        return officerRemarks;
    }

    public void setOfficerRemarks(String officerRemarks) {
        this.officerRemarks = officerRemarks;
    }

    public double getRecommendedPayoutAmount() {
        return recommendedPayoutAmount;
    }

    public void setRecommendedPayoutAmount(double recommendedPayoutAmount) {
        this.recommendedPayoutAmount = recommendedPayoutAmount;
    }

    public LocalDateTime getInvestigationStartedAt() {
        return investigationStartedAt;
    }

    public void setInvestigationStartedAt(LocalDateTime investigationStartedAt) {
        this.investigationStartedAt = investigationStartedAt;
    }

    public LocalDateTime getRecommendationDate() {
        return recommendationDate;
    }

    public void setRecommendationDate(LocalDateTime recommendationDate) {
        this.recommendationDate = recommendationDate;
    }

    public String getAdminRemarks() {
        return adminRemarks;
    }

    public void setAdminRemarks(String adminRemarks) {
        this.adminRemarks = adminRemarks;
    }

    public ClaimStatus getStatus() {
        return status;
    }

    public void setStatus(ClaimStatus status) {
        this.status = status;
    }

    public LocalDateTime getApprovalDate() {
        return approvalDate;
    }

    public void setApprovalDate(LocalDateTime approvalDate) {
        this.approvalDate = approvalDate;
    }
}