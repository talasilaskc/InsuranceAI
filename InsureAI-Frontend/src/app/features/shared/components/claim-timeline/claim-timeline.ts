import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ClaimStatus } from '../../../../models/claim.model';

@Component({
  selector: 'app-claim-timeline',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './claim-timeline.html'
})
export class ClaimTimelineComponent {
  @Input() currentStatus!: ClaimStatus;

  statuses = [
    { key: ClaimStatus.CLAIM_RAISED, label: 'Raised', icon: 'ri-file-add-line' },
    { key: ClaimStatus.UNDER_ADMIN_REVIEW, label: 'Admin Review', icon: 'ri-search-eye-line' },
    { key: ClaimStatus.ASSIGNED_TO_OFFICER, label: 'Assigned', icon: 'ri-user-shared-line' },
    { key: ClaimStatus.UNDER_INVESTIGATION, label: 'Investigating', icon: 'ri-microscope-line' },
    { key: ClaimStatus.OFFICER_RECOMMENDED, label: 'Recommended', icon: 'ri-thumb-up-line' },
    { key: ClaimStatus.PAYMENT_IN_PROGRESS, label: 'Payment', icon: 'ri-refund-line' },
    { key: ClaimStatus.SETTLED, label: 'Settled', icon: 'ri-checkbox-circle-line' }
  ];

  get progressWidth(): number {
    if (this.currentStatus === ClaimStatus.REJECTED) return 0;
    const index = this.statuses.findIndex(s => s.key === this.currentStatus);
    if (index === -1) return 0;
    return (index / (this.statuses.length - 1)) * 100;
  }

  isCompleted(statusKey: ClaimStatus): boolean {
    if (this.currentStatus === ClaimStatus.REJECTED) return false;
    const currentIndex = this.statuses.findIndex(s => s.key === this.currentStatus);
    const targetIndex = this.statuses.findIndex(s => s.key === statusKey);
    return targetIndex <= currentIndex && currentIndex !== -1;
  }

  isCurrent(statusKey: ClaimStatus): boolean {
    return this.currentStatus === statusKey;
  }
}
