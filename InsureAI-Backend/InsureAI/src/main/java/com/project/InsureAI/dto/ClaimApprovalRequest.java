package com.project.InsureAI.dto;

public class ClaimApprovalRequest {

    private double verifiedLoss;

    public ClaimApprovalRequest() {}

    public double getVerifiedLoss() {
        return verifiedLoss;
    }

    public void setVerifiedLoss(double verifiedLoss) {
        this.verifiedLoss = verifiedLoss;
    }
}