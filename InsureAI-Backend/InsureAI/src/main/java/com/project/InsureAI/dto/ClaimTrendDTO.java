package com.project.InsureAI.dto;

import com.project.InsureAI.entity.ClaimStatus;

public class ClaimTrendDTO {

    private ClaimStatus status;
    private Long count;

    public ClaimTrendDTO(ClaimStatus status, Long count) {
        this.status = status;
        this.count = count;
    }

    public ClaimStatus getStatus() { return status; }
    public Long getCount() { return count; }


}