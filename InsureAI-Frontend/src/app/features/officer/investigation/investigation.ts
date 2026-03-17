import { Component, inject, OnInit, signal, AfterViewInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { ClaimService } from '../../../services/claim/claim.service';
import { Claim } from '../../../models/claim.model';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { ToastService } from '../../../services/toast/toast.service';
import { ClaimTimelineComponent } from '../../shared/components/claim-timeline/claim-timeline';
import { ClaimDocumentsComponent } from '../../shared/components/claim-documents/claim-documents';

declare const lucide: any;

@Component({
  selector: 'app-officer-investigation',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterLink, ClaimTimelineComponent, ClaimDocumentsComponent],
  templateUrl: './investigation.html'
})
export class OfficerInvestigation implements OnInit, AfterViewInit {
  private route = inject(ActivatedRoute);
  private router = inject(Router);
  private claimService = inject(ClaimService);
  private fb = inject(FormBuilder);
  private toastService = inject(ToastService);

  claimId = Number(this.route.snapshot.paramMap.get('id'));
  claim = signal<Claim | null>(null);
  loading = signal<boolean>(true);
  submitting = signal<boolean>(false);

  recommendForm = this.fb.group({
    recommendedPayoutAmount: [0, [Validators.required, Validators.min(0)]],
    officerRemarks: ['', [Validators.required, Validators.minLength(20)]]
  });

  ngOnInit(): void {
    this.loadClaimDetails();
  }

  ngAfterViewInit(): void {
    if (typeof lucide !== 'undefined') lucide.createIcons();
  }

  loadClaimDetails() {
    this.claimService.getAssignedClaims().subscribe({
      next: (res: Claim[]) => {
        const found = res.find(c => c.claimId === this.claimId);
        if (found) {
          this.claim.set(found);
          this.recommendForm.patchValue({
            recommendedPayoutAmount: found.recommendedPayoutAmount || found.claimAmount
          });

          if (found.status === 'OFFICER_RECOMMENDED') {
            this.recommendForm.disable();
          } else {
            this.recommendForm.enable();
          }

          setTimeout(() => {
            if (typeof lucide !== 'undefined') lucide.createIcons();
          }, 0);
        }
        this.loading.set(false);
      },
      error: (err: any) => {
        console.error("Failed to load claim", err);
        this.loading.set(false);
      }
    });
  }

  startInvestigation() {
    this.claimService.startInvestigation(this.claimId).subscribe({
      next: () => {
        this.toastService.success("Investigation started formally.");
        this.loadClaimDetails();
      },
      error: (err: any) => this.toastService.error(err.error?.message || "Action failed")
    });
  }

  submitRecommendation() {
    if (this.recommendForm.invalid || this.submitting()) return;

    this.submitting.set(true);
    const payload = this.recommendForm.getRawValue() as { recommendedPayoutAmount: number; officerRemarks: string };
    this.claimService.submitRecommendation(this.claimId, payload).subscribe({
      next: () => {
        this.toastService.success("Recommendation submitted to administration.");
        this.router.navigate(['/officer/dashboard']);
      },
      error: (err: any) => {
        this.toastService.error(err.error?.message || "Submission failed");
        this.submitting.set(false);
      }
    });
  }
  
  getStatusClass(status?: string): string {
    if (!status) return 'text-muted border-line bg-surface';
    switch (status) {
      case 'ASSIGNED_TO_OFFICER': return 'text-amber-600 border-amber-200 bg-amber-50';
      case 'UNDER_INVESTIGATION': return 'text-primary border-primary bg-primary/5';
      case 'OFFICER_RECOMMENDED': return 'text-emerald-700 border-emerald-700 bg-emerald-50';
      case 'REJECTED': return 'text-rose-600 border-rose-600 bg-rose-50';
      default: return 'text-muted border-line bg-surface';
    }
  }
}
