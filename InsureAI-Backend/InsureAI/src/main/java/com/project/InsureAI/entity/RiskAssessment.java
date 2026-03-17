package com.project.InsureAI.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class RiskAssessment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // VERY IMPORTANT CHANGE
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "policy_id")
    private Policy policy;

    // who assessed
    @ManyToOne
    @JoinColumn(name = "underwriter_id")
    private MyUser underwriter;

    // AI system details are now accessed via the policy -> aiSystem relationship
    @ManyToOne
    @JoinColumn(name = "ai_system_id")
    private AISystem aiSystem;

    // Inputs
    private boolean humanOversight;
    private boolean biasTesting;
    private boolean auditLogsMaintained;

    @Enumerated(EnumType.STRING)
    @Column(length = 50)
    private DataExposureCategory dataExposureCategory;

    private boolean lifeCriticalUsage;

    @Enumerated(EnumType.STRING)
    @Column(length = 50)
    private FinancialImpactLevel financialImpactLevel;

    private int pastIncidentCount;

    // Scores
    private int governanceScore;
    private int exposureScore;
    private int impactScore;
    private int totalRiskScore;

    @Enumerated(EnumType.STRING)
    @Column(length = 50)
    private RiskLevel riskLevel;

    // Underwriter recommendation
    private double recommendedPremium;

    @Column(length = 1000)
    private String remarks;

    @Enumerated(EnumType.STRING)
    @Column(length = 50)
    private AssessmentStatus status; // DRAFT, SUBMITTED

    private boolean isActive;
    private LocalDateTime assessedAt;

    public AISystem getAiSystem() {
        return aiSystem;
    }

    public void setAiSystem(AISystem aiSystem) {
        this.aiSystem = aiSystem;
    }


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

    public MyUser getUnderwriter() {
        return underwriter;
    }

    public void setUnderwriter(MyUser underwriter) {
        this.underwriter = underwriter;
    }

    public boolean isHumanOversight() {
        return humanOversight;
    }

    public void setHumanOversight(boolean humanOversight) {
        this.humanOversight = humanOversight;
    }

    public boolean isBiasTesting() {
        return biasTesting;
    }

    public void setBiasTesting(boolean biasTesting) {
        this.biasTesting = biasTesting;
    }

    public boolean isAuditLogsMaintained() {
        return auditLogsMaintained;
    }

    public void setAuditLogsMaintained(boolean auditLogsMaintained) {
        this.auditLogsMaintained = auditLogsMaintained;
    }

    public DataExposureCategory getDataExposureCategory() {
        return dataExposureCategory;
    }

    public void setDataExposureCategory(DataExposureCategory dataExposureCategory) {
        this.dataExposureCategory = dataExposureCategory;
    }

    public boolean isLifeCriticalUsage() {
        return lifeCriticalUsage;
    }

    public void setLifeCriticalUsage(boolean lifeCriticalUsage) {
        this.lifeCriticalUsage = lifeCriticalUsage;
    }

    public FinancialImpactLevel getFinancialImpactLevel() {
        return financialImpactLevel;
    }

    public void setFinancialImpactLevel(FinancialImpactLevel financialImpactLevel) {
        this.financialImpactLevel = financialImpactLevel;
    }

    public int getPastIncidentCount() {
        return pastIncidentCount;
    }

    public void setPastIncidentCount(int pastIncidentCount) {
        this.pastIncidentCount = pastIncidentCount;
    }

    public int getGovernanceScore() {
        return governanceScore;
    }

    public void setGovernanceScore(int governanceScore) {
        this.governanceScore = governanceScore;
    }

    public int getExposureScore() {
        return exposureScore;
    }

    public void setExposureScore(int exposureScore) {
        this.exposureScore = exposureScore;
    }

    public int getImpactScore() {
        return impactScore;
    }

    public void setImpactScore(int impactScore) {
        this.impactScore = impactScore;
    }

    public int getTotalRiskScore() {
        return totalRiskScore;
    }

    public void setTotalRiskScore(int totalRiskScore) {
        this.totalRiskScore = totalRiskScore;
    }

    public RiskLevel getRiskLevel() {
        return riskLevel;
    }

    public void setRiskLevel(RiskLevel riskLevel) {
        this.riskLevel = riskLevel;
    }

    public double getRecommendedPremium() {
        return recommendedPremium;
    }

    public void setRecommendedPremium(double recommendedPremium) {
        this.recommendedPremium = recommendedPremium;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public AssessmentStatus getStatus() {
        return status;
    }

    public void setStatus(AssessmentStatus status) {
        this.status = status;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public LocalDateTime getAssessedAt() {
        return assessedAt;
    }

    public void setAssessedAt(LocalDateTime assessedAt) {
        this.assessedAt = assessedAt;
    }


}