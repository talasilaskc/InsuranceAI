package com.project.InsureAI.dto;

import java.util.List;

public class AdminDashboardResponse {

    private long totalCompanies;
    private long activePolicies;
    private long pendingPolicies;
    private long pendingClaims;
    private double totalPayout;
    private List<PolicyRevenueDTO> revenueByProduct;
    private List<ClaimTrendDTO> claimTrend;

    public AdminDashboardResponse(long totalCompanies,
                                  long activePolicies,
                                  long pendingPolicies,
                                  long pendingClaims,
                                  double totalPayout,
                                  List<PolicyRevenueDTO> revenueByProduct,
                                  List<ClaimTrendDTO> claimTrend) {
        this.totalCompanies = totalCompanies;
        this.activePolicies = activePolicies;
        this.pendingPolicies = pendingPolicies;
        this.pendingClaims = pendingClaims;
        this.totalPayout = totalPayout;
        this.revenueByProduct = revenueByProduct;
        this.claimTrend = claimTrend;
    }

    public long getTotalCompanies() { return totalCompanies; }
    public long getActivePolicies() { return activePolicies; }
    public long getPendingPolicies() { return pendingPolicies; }
    public long getPendingClaims() { return pendingClaims; }
    public double getTotalPayout() { return totalPayout; }

    public List<PolicyRevenueDTO> getRevenueByProduct() {
        return revenueByProduct;
    }

    public List<ClaimTrendDTO> getClaimTrend() {
        return claimTrend;
    }


}