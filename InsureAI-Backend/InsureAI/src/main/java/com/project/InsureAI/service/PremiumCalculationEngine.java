package com.project.InsureAI.service;

import com.project.InsureAI.entity.RiskLevel;
import org.springframework.stereotype.Component;

@Component
public class PremiumCalculationEngine {

    private static final double STANDARD_COVERAGE = 1_000_000; // 10L
    private static final double GST_RATE = 0.18;

    public PremiumBreakdown calculate(
            double basePrice,
            double coverageLimit,
            RiskLevel riskLevel,
            int claimCountPreviousTerm,
            double deductibleAmount,
            int tenureYears) {

        System.out.println("========== PREMIUM CALCULATION START ==========");
        System.out.println("Base Price: " + basePrice);
        System.out.println("Coverage Limit: " + coverageLimit);
        System.out.println("Risk Level: " + riskLevel);
        System.out.println("Previous Claims: " + claimCountPreviousTerm);
        System.out.println("Deductible Amount: " + deductibleAmount);
        System.out.println("Tenure Years: " + tenureYears);

        // 1️⃣ Exposure Scaling
        double coverageFactor = coverageLimit / STANDARD_COVERAGE;
        double exposurePremium = basePrice * coverageFactor;

        System.out.println("\n--- Exposure Scaling ---");
        System.out.println("Coverage Factor: " + coverageFactor);
        System.out.println("Exposure Premium: " + exposurePremium);

        // 2️⃣ Risk Multiplier
        double riskMultiplier = switch (riskLevel) {
            case LOW -> 1.10;
            case MEDIUM -> 1.35;
            case HIGH -> 1.75;
        };

        double riskAdjusted = exposurePremium * riskMultiplier;

        System.out.println("\n--- Risk Adjustment ---");
        System.out.println("Risk Multiplier: " + riskMultiplier);
        System.out.println("Risk Adjusted Premium: " + riskAdjusted);

        // 3️⃣ Experience Loading
        double experienceLoading = switch (claimCountPreviousTerm) {
            case 0 -> 0.0;
            case 1, 2 -> 0.05;
            case 3, 4 -> 0.12;
            default -> 0.25;
        };

        double experienceAdjusted =
                riskAdjusted * (1 + experienceLoading);

        System.out.println("\n--- Experience Loading ---");
        System.out.println("Experience Loading %: " + (experienceLoading * 100) + "%");
        System.out.println("Experience Adjusted Premium: " + experienceAdjusted);

        // 4️⃣ Deductible Discount
        double deductibleRatio = deductibleAmount / coverageLimit;

        double deductibleDiscount = 0.0;

        if (deductibleRatio >= 0.10) {
            deductibleDiscount = 0.08;
        } else if (deductibleRatio >= 0.05) {
            deductibleDiscount = 0.05;
        }

        double deductibleAdjusted =
                experienceAdjusted * (1 - deductibleDiscount);

        System.out.println("\n--- Deductible Discount ---");
        System.out.println("Deductible Ratio: " + deductibleRatio);
        System.out.println("Deductible Discount %: " + (deductibleDiscount * 100) + "%");
        System.out.println("Deductible Adjusted Premium: " + deductibleAdjusted);

        // 5️⃣ Multi-Year Discount
        double multiYearDiscount = switch (tenureYears) {
            case 2 -> 0.04;
            case 3 -> 0.07;
            default -> 0.0;
        };

        double technicalPremium =
                deductibleAdjusted * (1 - multiYearDiscount);

        System.out.println("\n--- Multi-Year Discount ---");
        System.out.println("Multi-Year Discount %: " + (multiYearDiscount * 100) + "%");
        System.out.println("Technical Premium: " + technicalPremium);

        // 6️⃣ Tax
        double tax = technicalPremium * GST_RATE;
        double totalPayable = technicalPremium + tax;

        System.out.println("\n--- Tax Calculation ---");
        System.out.println("GST (" + (GST_RATE * 100) + "%): " + tax);
        System.out.println("Total Payable Premium: " + totalPayable);

        System.out.println("========== PREMIUM CALCULATION END ==========\n");

        return new PremiumBreakdown(
                basePrice,
                exposurePremium,
                riskAdjusted,
                experienceAdjusted,
                deductibleAdjusted,
                technicalPremium,
                tax,
                totalPayable
        );
    }
}