package com.project.InsureAI.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
public class Policy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "ai_system_id", nullable = false)
    private AISystem aiSystem;

    @ManyToOne
    @JoinColumn(name = "policy_type_id", nullable = false)
    private InsurancePolicyType policyType;

    @OneToOne
    @JoinColumn(name = "risk_assessment_id")
    private RiskAssessment riskSnapshot;

    @ManyToOne
    @JoinColumn(name = "underwriter_id")
    private MyUser underwriter;


    private double coverageLimit;
    private double deductibleAmount;

    private int tenureYears;

    @Enumerated(EnumType.STRING)
    @Column(length = 50)
    private PolicyStatus status;

    private double premiumAmount; // frozen technical premium

    private double remainingCoverage;

    private LocalDate startDate;
    private LocalDate endDate;

    private boolean isActive;

    // Renewal tracking
    private Long renewedFromPolicyId; // nullable

    public Policy() {}

    // getters and setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AISystem getAiSystem() {
        return aiSystem;
    }

    public void setAiSystem(AISystem aiSystem) {
        this.aiSystem = aiSystem;
    }

    public InsurancePolicyType getPolicyType() {
        return policyType;
    }

    public void setPolicyType(InsurancePolicyType policyType) {
        this.policyType = policyType;
    }

    public RiskAssessment getRiskSnapshot() {
        return riskSnapshot;
    }

    public void setRiskSnapshot(RiskAssessment riskSnapshot) {
        this.riskSnapshot = riskSnapshot;
    }

    public double getCoverageLimit() {
        return coverageLimit;
    }

    public void setCoverageLimit(double coverageLimit) {
        this.coverageLimit = coverageLimit;
    }

    public double getDeductibleAmount() {
        return deductibleAmount;
    }

    public void setDeductibleAmount(double deductibleAmount) {
        this.deductibleAmount = deductibleAmount;
    }

    public double getPremiumAmount() {
        return premiumAmount;
    }

    public void setPremiumAmount(double premiumAmount) {
        this.premiumAmount = premiumAmount;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public Long getRenewedFromPolicyId() {
        return renewedFromPolicyId;
    }

    public void setRenewedFromPolicyId(Long renewedFromPolicyId) {
        this.renewedFromPolicyId = renewedFromPolicyId;
    }

    public PolicyStatus getStatus() {
        return status;
    }

    public void setStatus(PolicyStatus status) {
        this.status = status;
    }

    public double getRemainingCoverage() {
        return remainingCoverage;
    }

    public void setRemainingCoverage(double remainingCoverage) {
        this.remainingCoverage = remainingCoverage;
    }

    public MyUser getUnderwriter() {
        return underwriter;
    }

    public void setUnderwriter(MyUser underwriter) {
        this.underwriter = underwriter;
    }

    public int getTenureYears() {
        return tenureYears;
    }

    public void setTenureYears(int tenureYears) {
        this.tenureYears = tenureYears;
    }
}