package com.project.InsureAI.dto;

public class PolicyRevenueDTO {

    private String policyTypeName;
    private long policiesSold;
    private double totalRevenue;


    public PolicyRevenueDTO(String policyTypeName,
                            long policiesSold,
                            double totalRevenue) {
        this.policyTypeName = policyTypeName;
        this.policiesSold = policiesSold;
        this.totalRevenue = totalRevenue;
    }

    public String getPolicyTypeName() { return policyTypeName; }
    public long getPoliciesSold() { return policiesSold; }
    public double getTotalRevenue() { return totalRevenue; }
}