package com.project.InsureAI.dto;

import com.project.InsureAI.entity.PolicyStatus;

public class PolicySummaryDTO {

    private String policyTypeName;
    private double coverage;
    private double remaining;
    private PolicyStatus status;

    public PolicySummaryDTO(String policyTypeName,
                            double coverage,
                            double remaining,
                            PolicyStatus status) {
        this.policyTypeName = policyTypeName;
        this.coverage = coverage;
        this.remaining = remaining;
        this.status = status;
    }

    public String getPolicyTypeName() { return policyTypeName; }
    public double getCoverage() { return coverage; }
    public double getRemaining() { return remaining; }
    public PolicyStatus getStatus() { return status; }
}