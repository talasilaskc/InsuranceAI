package com.project.InsureAI.dto;

import com.project.InsureAI.entity.RiskLevel;

import java.time.LocalDateTime;

public class RiskAssessmentResponse {

    private Long id;
    private Long aiSystemId;

    private int governanceScore;
    private int exposureScore;
    private int impactScore;
    private int totalRiskScore;

    private RiskLevel riskLevel;

    private boolean isActive;
    private LocalDateTime assessedAt;

    public RiskAssessmentResponse() {}

    public RiskAssessmentResponse(Long id,
                                  Long aiSystemId,
                                  int governanceScore,
                                  int exposureScore,
                                  int impactScore,
                                  int totalRiskScore,
                                  RiskLevel riskLevel,
                                  boolean isActive,
                                  LocalDateTime assessedAt) {
        this.id = id;
        this.aiSystemId = aiSystemId;
        this.governanceScore = governanceScore;
        this.exposureScore = exposureScore;
        this.impactScore = impactScore;
        this.totalRiskScore = totalRiskScore;
        this.riskLevel = riskLevel;
        this.isActive = isActive;
        this.assessedAt = assessedAt;
    }

    public Long getId() {
        return id;
    }

    public Long getAiSystemId() {
        return aiSystemId;
    }

    public int getGovernanceScore() {
        return governanceScore;
    }

    public int getExposureScore() {
        return exposureScore;
    }

    public int getImpactScore() {
        return impactScore;
    }

    public int getTotalRiskScore() {
        return totalRiskScore;
    }

    public RiskLevel getRiskLevel() {
        return riskLevel;
    }

    public boolean isActive() {
        return isActive;
    }

    public LocalDateTime getAssessedAt() {
        return assessedAt;
    }
}