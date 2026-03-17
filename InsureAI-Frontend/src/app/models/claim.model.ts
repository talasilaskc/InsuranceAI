export enum ClaimStatus {
  CLAIM_RAISED = 'CLAIM_RAISED',
  UNDER_ADMIN_REVIEW = 'UNDER_ADMIN_REVIEW',
  ASSIGNED_TO_OFFICER = 'ASSIGNED_TO_OFFICER',
  UNDER_INVESTIGATION = 'UNDER_INVESTIGATION',
  OFFICER_RECOMMENDED = 'OFFICER_RECOMMENDED',
  APPROVED = 'APPROVED',
  REJECTED = 'REJECTED',
  PAYMENT_IN_PROGRESS = 'PAYMENT_IN_PROGRESS',
  SETTLED = 'SETTLED'
}

export interface Claim {
  claimId: number;
  policyId: number;
  claimAmount: number;
  payoutAmount: number;
  status: ClaimStatus;
  claimDate: string;
  verifiedLoss: number;
  description: string;
  assignedOfficerName?: string;
  officerRemarks?: string;
  recommendedPayoutAmount?: number;
  adminRemarks?: string;
  investigationStartedAt?: string;
  recommendationDate?: string;
  approvalDate?: string;
  policyName?: string;
  companyName?: string;
  aiSystemName?: string;
  coverageAmount?: number;
  remainingCoverage?: number;
}

export interface ClaimDocument {
  id: number;
  fileName: string;
  fileType: string;
  uploadedAt: string;
}

export interface Policy {
  id: number;
  policyType: any;
  premiumAmount: number;
  coverageAmount: number;
  remainingCoverage: number;
  startDate: string;
  endDate: string;
  status: string;
  active: boolean;
  aiSystem?: {
    id: number;
    name: string;
  }
}
