package com.project.InsureAI.entity;

public enum ClaimStatus {

    CLAIM_RAISED,              // Customer filed claim
    UNDER_ADMIN_REVIEW,        // Admin initial validation
    ASSIGNED_TO_OFFICER,       // Admin assigned claims officer
    UNDER_INVESTIGATION,       // Officer reviewing documents
    OFFICER_RECOMMENDED,       // Officer suggested payout
    APPROVED,                  // Admin approved claim
    REJECTED,                  // Admin rejected claim
    PAYMENT_IN_PROGRESS,       // Settlement initiated
    SETTLED                    // Amount credited

}