package com.project.InsureAI.dto;

public class PolicyIssuanceRequest {
    private Long aiSystemId;
    private Long policyTypeId;

    private double coverageLimit;
    private double deductibleAmount;

    private int tenureYears;

    public PolicyIssuanceRequest() {
    }

    // getters & setters

    public Long getAiSystemId() {
        return aiSystemId;
    }

    public void setAiSystemId(Long aiSystemId) {
        this.aiSystemId = aiSystemId;
    }

    public Long getPolicyTypeId() {
        return policyTypeId;
    }

    public void setPolicyTypeId(Long policyTypeId) {
        this.policyTypeId = policyTypeId;
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

    public int getTenureYears() {
        return tenureYears;
    }

    public void setTenureYears(int tenureYears) {
        this.tenureYears = tenureYears;
    }
}
