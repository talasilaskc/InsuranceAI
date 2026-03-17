package com.project.InsureAI.dto;

public class AssignOfficerResponse {
    private Long claimId;
    private Long officerId;
    private String officerName;
    private String status;

    public AssignOfficerResponse() {}
    public AssignOfficerResponse(Long cId, Long oId, String name, String status) {
        this.claimId = cId;
        this.officerId = oId;
        this.officerName = name;
        this.status = status;
    }

    public Long getClaimId() { return claimId; }
    public void setClaimId(Long id) { this.claimId = id; }
    public Long getOfficerId() { return officerId; }
    public void setOfficerId(Long id) { this.officerId = id; }
    public String getOfficerName() { return officerName; }
    public void setOfficerName(String name) { this.officerName = name; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
