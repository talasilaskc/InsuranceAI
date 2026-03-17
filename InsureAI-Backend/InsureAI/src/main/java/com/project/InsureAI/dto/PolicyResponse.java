package com.project.InsureAI.dto;

import com.project.InsureAI.entity.PolicyStatus;

import java.time.LocalDate;

public class PolicyResponse {

    private Long policyId;
    private Long aiSystemId;
    private String aiSystemName;
    private Long policyTypeId;

    private double coverageLimit;
    private double deductibleAmount;

    private double premiumAmount;

    private LocalDate startDate;
    private LocalDate endDate;
    private PolicyStatus status;

    public PolicyResponse(Long policyId,
                          Long aiSystemId,
                          String aiSystemName,
                          Long policyTypeId,
                          double coverageLimit,
                          double deductibleAmount,
                          double premiumAmount,
                          LocalDate startDate,
                          LocalDate endDate,
                          PolicyStatus status) {

        this.policyId = policyId;
        this.aiSystemId = aiSystemId;
        this.aiSystemName = aiSystemName;
        this.policyTypeId = policyTypeId;
        this.coverageLimit = coverageLimit;
        this.deductibleAmount = deductibleAmount;
        this.premiumAmount = premiumAmount;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
    }

    public Long getPolicyId() { return policyId; }
    public Long getAiSystemId() { return aiSystemId; }
    public String getAiSystemName() { return aiSystemName; }
    public Long getPolicyTypeId() { return policyTypeId; }
    public double getCoverageLimit() { return coverageLimit; }
    public double getDeductibleAmount() { return deductibleAmount; }
    public double getPremiumAmount() { return premiumAmount; }
    public LocalDate getStartDate() { return startDate; }
    public LocalDate getEndDate() { return endDate; }
    public PolicyStatus getStatus() { return status; }
}