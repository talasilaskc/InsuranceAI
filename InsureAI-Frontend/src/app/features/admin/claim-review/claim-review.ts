import { Component, OnInit, inject, signal, ChangeDetectionStrategy, AfterViewInit } from '@angular/core';
import { ActivatedRoute, RouterLink, Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ClaimService } from '../../../services/claim/claim.service';
import { AdminService } from '../../../services/admin/admin.service';
import { ToastService } from '../../../services/toast/toast.service';
import { Claim, ClaimStatus } from '../../../models/claim.model';
import { ClaimTimelineComponent } from '../../shared/components/claim-timeline/claim-timeline';
import { ClaimDocumentsComponent } from '../../shared/components/claim-documents/claim-documents';

declare const lucide: any;

@Component({
  selector: 'app-claim-review',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink, ClaimTimelineComponent, ClaimDocumentsComponent],
  templateUrl: './claim-review.html',
  styleUrl: './claim-review.css',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class ClaimReview implements OnInit, AfterViewInit {
  private claimService = inject(ClaimService);
  private adminService = inject(AdminService);
  private route = inject(ActivatedRoute);
  private router = inject(Router);
  private toastService = inject(ToastService);

  claimId = Number(this.route.snapshot.params['id']);
  claim = signal<Claim | null>(null);
  payoutAmount = signal<number>(0);
  remarks = signal<string>('');
  loading = signal(true);
  processing = signal(false);

  ngOnInit(): void {
    this.adminService.title.set("Final Claim Determination");
    this.loadClaim();
  }

  loadClaim() {
    this.loading.set(true);
    this.claimService.getAllClaims().subscribe({
      next: (data) => {
        const found = data.find(c => c.claimId === this.claimId);
        if (found) {
          this.claim.set(found);
          this.payoutAmount.set(found.recommendedPayoutAmount || found.claimAmount);
          this.remarks.set(found.adminRemarks || '');
        } else {
          this.toastService.error("Claim not found");
          this.router.navigate(['/admin/all-claims']);
        }
        this.loading.set(false);
        this.refreshIcons();
      },
      error: (err) => {
        console.error(err);
        this.loading.set(false);
        this.router.navigate(['/admin/all-claims']);
      }
    });
  }

  ngAfterViewInit(): void {
    this.refreshIcons();
  }

  private refreshIcons(): void {
    setTimeout(() => {
      if (typeof lucide !== 'undefined') {
        lucide.createIcons();
      }
    }, 0);
  }

  approve() {
    if (this.processing() || !this.claim()) return;
    
    this.processing.set(true);
    this.claimService.finalApprove(this.claimId, {
      payoutAmount: this.payoutAmount(),
      remarks: this.remarks()
    }).subscribe({
      next: () => {
        this.toastService.success("Claim approved successfully.");
        this.router.navigate(['/admin/all-claims']);
      },
      error: (err) => {
        this.toastService.error(err.error?.message || "Approval failed");
        this.processing.set(false);
      }
    });
  }

  reject() {
    if (this.processing() || !this.claim()) return;

    this.processing.set(true);
    this.claimService.finalReject(this.claimId, {
      remarks: this.remarks()
    }).subscribe({
      next: () => {
        this.toastService.info("Claim rejected.");
        this.router.navigate(['/admin/all-claims']);
      },
      error: (err) => {
        this.toastService.error(err.error?.message || "Rejection failed");
        this.processing.set(false);
      }
    });
  }
}