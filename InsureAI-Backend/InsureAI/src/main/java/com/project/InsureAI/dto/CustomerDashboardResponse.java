package com.project.InsureAI.dto;

import java.util.List;

public class CustomerDashboardResponse {

    private long activePolicies;
    private long claimsSubmitted;
    private double remainingCoverage;
    private long aiSystems;

    private double lastPayout;

    private List<String> alerts;
    private List<PolicySummaryDTO> policies;
    private List<AiRiskSnapshotDTO> aiRiskSnapshot;

    public CustomerDashboardResponse(
            long activePolicies,
            long claimsSubmitted,
            double remainingCoverage,
            long aiSystems,
            double lastPayout,
            List<String> alerts,
            List<PolicySummaryDTO> policies,
            List<AiRiskSnapshotDTO> aiRiskSnapshot) {

        this.activePolicies = activePolicies;
        this.claimsSubmitted = claimsSubmitted;
        this.remainingCoverage = remainingCoverage;
        this.aiSystems = aiSystems;
        this.lastPayout = lastPayout;
        this.alerts = alerts;
        this.policies = policies;
        this.aiRiskSnapshot = aiRiskSnapshot;
    }

    public long getActivePolicies() { return activePolicies; }
    public long getClaimsSubmitted() { return claimsSubmitted; }
    public double getRemainingCoverage() { return remainingCoverage; }
    public long getAiSystems() { return aiSystems; }
    public double getLastPayout() { return lastPayout; }
    public List<String> getAlerts() { return alerts; }
    public List<PolicySummaryDTO> getPolicies() { return policies; }
    public List<AiRiskSnapshotDTO> getAiRiskSnapshot() { return aiRiskSnapshot; }
}