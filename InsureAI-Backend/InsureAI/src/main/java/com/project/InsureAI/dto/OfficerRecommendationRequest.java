package com.project.InsureAI.dto;

public class OfficerRecommendationRequest {
    private double recommendedPayoutAmount;
    private String officerRemarks;

    public OfficerRecommendationRequest() {}

    public double getRecommendedPayoutAmount() { return recommendedPayoutAmount; }
    public void setRecommendedPayoutAmount(double amount) { this.recommendedPayoutAmount = amount; }

    public String getOfficerRemarks() { return officerRemarks; }
    public void setOfficerRemarks(String remarks) { this.officerRemarks = remarks; }
}
