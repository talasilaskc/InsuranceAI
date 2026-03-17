package com.project.InsureAI.dto;

import com.project.InsureAI.entity.RiskLevel;

public class AiRiskSnapshotDTO {

    private String systemName;
    private int riskScore;
    private RiskLevel riskLevel;

    public AiRiskSnapshotDTO(String systemName,
                             int riskScore,
                             RiskLevel riskLevel) {
        this.systemName = systemName;
        this.riskScore = riskScore;
        this.riskLevel = riskLevel;
    }

    public String getSystemName() { return systemName; }
    public int getRiskScore() { return riskScore; }
    public RiskLevel getRiskLevel() { return riskLevel; }
}