package com.project.InsureAI.dto;

public class PremiumCalculationResponse {

    private double basePrice;
    private double exposurePremium;
    private double riskAdjustedPremium;
    private double experienceAdjustedPremium;
    private double deductibleAdjustedPremium;
    private double technicalPremium;

    private double tax;
    private double totalPayable;

    public PremiumCalculationResponse(double basePrice,
                                      double exposurePremium,
                                      double riskAdjustedPremium,
                                      double experienceAdjustedPremium,
                                      double deductibleAdjustedPremium,
                                      double technicalPremium,
                                      double tax,
                                      double totalPayable) {
        this.basePrice = basePrice;
        this.exposurePremium = exposurePremium;
        this.riskAdjustedPremium = riskAdjustedPremium;
        this.experienceAdjustedPremium = experienceAdjustedPremium;
        this.deductibleAdjustedPremium = deductibleAdjustedPremium;
        this.technicalPremium = technicalPremium;
        this.tax = tax;
        this.totalPayable = totalPayable;
    }

    // getters only


    public double getBasePrice() {
        return basePrice;
    }

    public double getExposurePremium() {
        return exposurePremium;
    }

    public double getRiskAdjustedPremium() {
        return riskAdjustedPremium;
    }

    public double getExperienceAdjustedPremium() {
        return experienceAdjustedPremium;
    }

    public double getDeductibleAdjustedPremium() {
        return deductibleAdjustedPremium;
    }

    public double getTechnicalPremium() {
        return technicalPremium;
    }

    public double getTax() {
        return tax;
    }

    public double getTotalPayable() {
        return totalPayable;
    }
}