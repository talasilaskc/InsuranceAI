package com.project.InsureAI.dto;

public class PolicyRenewalRequest {
    private Long policyId;
    private double coverageLimit;
    private double deductibleAmount;
    private int tenureYears;

    public PolicyRenewalRequest() {
    }

    public Long getPolicyId() {
        return policyId;
    }

    public void setPolicyId(Long policyId) {
        this.policyId = policyId;
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
