package com.project.InsureAI.dto;

public class SubmitClaimRequest {
    private Long policyId;
    private double claimAmount;
    private String description;

    public SubmitClaimRequest() {
    }

    public SubmitClaimRequest(Long policyId, double claimAmount, String description) {
        this.policyId = policyId;
        this.claimAmount = claimAmount;
        this.description = description;
    }

    public Long getPolicyId() {
        return policyId;
    }

    public void setPolicyId(Long policyId) {
        this.policyId = policyId;
    }

    public double getClaimAmount() {
        return claimAmount;
    }

    public void setClaimAmount(double claimAmount) {
        this.claimAmount = claimAmount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
