package com.project.InsureAI.service;

public class PremiumBreakdown {

    public double basePrice;
    public double exposurePremium;
    public double riskAdjusted;
    public double experienceAdjusted;
    public double deductibleAdjusted;
    public double technicalPremium;
    public double tax;
    public double totalPayable;

    public PremiumBreakdown() {
    }

    public PremiumBreakdown(double basePrice,
                            double exposurePremium,
                            double riskAdjusted,
                            double experienceAdjusted,
                            double deductibleAdjusted,
                            double technicalPremium,
                            double tax,
                            double totalPayable) {
        this.basePrice = basePrice;
        this.exposurePremium = exposurePremium;
        this.riskAdjusted = riskAdjusted;
        this.experienceAdjusted = experienceAdjusted;
        this.deductibleAdjusted = deductibleAdjusted;
        this.technicalPremium = technicalPremium;
        this.tax = tax;
        this.totalPayable = totalPayable;
    }

    public double getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(double basePrice) {
        this.basePrice = basePrice;
    }

    public double getExposurePremium() {
        return exposurePremium;
    }

    public void setExposurePremium(double exposurePremium) {
        this.exposurePremium = exposurePremium;
    }

    public double getRiskAdjusted() {
        return riskAdjusted;
    }

    public void setRiskAdjusted(double riskAdjusted) {
        this.riskAdjusted = riskAdjusted;
    }

    public double getExperienceAdjusted() {
        return experienceAdjusted;
    }

    public void setExperienceAdjusted(double experienceAdjusted) {
        this.experienceAdjusted = experienceAdjusted;
    }

    public double getDeductibleAdjusted() {
        return deductibleAdjusted;
    }

    public void setDeductibleAdjusted(double deductibleAdjusted) {
        this.deductibleAdjusted = deductibleAdjusted;
    }

    public double getTechnicalPremium() {
        return technicalPremium;
    }

    public void setTechnicalPremium(double technicalPremium) {
        this.technicalPremium = technicalPremium;
    }

    public double getTax() {
        return tax;
    }

    public void setTax(double tax) {
        this.tax = tax;
    }

    public double getTotalPayable() {
        return totalPayable;
    }

    public void setTotalPayable(double totalPayable) {
        this.totalPayable = totalPayable;
    }
}