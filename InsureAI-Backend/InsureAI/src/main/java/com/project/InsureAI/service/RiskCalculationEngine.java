package com.project.InsureAI.service;

import com.project.InsureAI.entity.*;

import org.springframework.stereotype.Component;

@Component
public class RiskCalculationEngine {

    public void calculateRisk(RiskAssessment risk) {

        // Governance
        int governanceScore = 60;

        if (risk.isHumanOversight()) governanceScore -= 20;
        if (risk.isBiasTesting()) governanceScore -= 20;
        if (risk.isAuditLogsMaintained()) governanceScore -= 20;

        // Exposure
        int exposureScore = switch (risk.getDataExposureCategory()) {
            case GENERIC -> 20;
            case PERSONAL -> 50;
            case FINANCIAL -> 100;
            case HEALTH -> 120;
        };

        // Impact
        int impactScore = 0;

        if (risk.isLifeCriticalUsage()) impactScore += 100;

        impactScore += switch (risk.getFinancialImpactLevel()) {
            case LOW -> 20;
            case MEDIUM -> 50;
            case HIGH -> 100;
        };

        impactScore += risk.getPastIncidentCount() * 10;

        int total = governanceScore + exposureScore + impactScore;

        risk.setGovernanceScore(governanceScore);
        risk.setExposureScore(exposureScore);
        risk.setImpactScore(impactScore);
        risk.setTotalRiskScore(total);

        // Override Logic
        if (risk.isLifeCriticalUsage()) {
            risk.setRiskLevel(RiskLevel.HIGH);
        }
        else if (risk.getDataExposureCategory() == DataExposureCategory.HEALTH
                && risk.getFinancialImpactLevel() == FinancialImpactLevel.HIGH) {
            risk.setRiskLevel(RiskLevel.HIGH);
        }
        else if (risk.getPastIncidentCount() >= 5) {
            risk.setRiskLevel(RiskLevel.HIGH);
        }
        else {
            if (total <= 120) {
                risk.setRiskLevel(RiskLevel.LOW);
            } else if (total <= 250) {
                risk.setRiskLevel(RiskLevel.MEDIUM);
            } else {
                risk.setRiskLevel(RiskLevel.HIGH);
            }
        }
    }

}