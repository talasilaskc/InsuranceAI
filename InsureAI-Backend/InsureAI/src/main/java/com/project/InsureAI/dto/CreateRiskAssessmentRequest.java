package com.project.InsureAI.dto;

import com.project.InsureAI.entity.DataExposureCategory;
import com.project.InsureAI.entity.FinancialImpactLevel;

public class CreateRiskAssessmentRequest {

    private Long aiSystemId;

    private boolean humanOversight;
    private boolean biasTesting;
    private boolean auditLogsMaintained;

    private DataExposureCategory dataExposureCategory;

    private boolean lifeCriticalUsage;

    private FinancialImpactLevel financialImpactLevel;

    private int pastIncidentCount;

    public CreateRiskAssessmentRequest() {}

    public Long getAiSystemId() {
        return aiSystemId;
    }

    public void setAiSystemId(Long aiSystemId) {
        this.aiSystemId = aiSystemId;
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
}