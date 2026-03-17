package com.project.InsureAI.dto;

public class AdminDecisionRequest {
    private double payoutAmount;
    private String remarks;
    public AdminDecisionRequest() {}
    public double getPayoutAmount() { return payoutAmount; }
    public void setPayoutAmount(double amt) { this.payoutAmount = amt; }
    public String getRemarks() { return remarks; }
    public void setRemarks(String rem) { this.remarks = rem; }
}
