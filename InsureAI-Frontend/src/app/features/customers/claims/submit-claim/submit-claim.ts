import { Component, inject, OnInit, AfterViewInit, signal, ChangeDetectionStrategy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, RouterLink, ActivatedRoute } from '@angular/router';
import { ClaimService } from '../../../../services/claim/claim.service';
import { PolicyService } from '../../../../services/policy/policy.service';
import { AdminService } from '../../../../services/admin/admin.service';
import { ToastService } from '../../../../services/toast/toast.service';

declare const lucide: any;

@Component({
  selector: 'app-submit-claim',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterLink],
  templateUrl: './submit-claim.html',
  styleUrl: './submit-claim.css',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class SubmitClaim implements OnInit, AfterViewInit {
  private fb = inject(FormBuilder);
  private claimService = inject(ClaimService);
  private policyService = inject(PolicyService);
  private adminService = inject(AdminService);
  private router = inject(Router);
  private route = inject(ActivatedRoute);
  private toastService = inject(ToastService);

  policies = signal<any[]>([]);
  loading = signal<boolean>(true);
  submitting = signal<boolean>(false);
  selectedFile: File | null = null;

  form = this.fb.group({
    policyId: ['', Validators.required],
    claimAmount: ['', [Validators.required, Validators.min(1)]],
    description: ['', [Validators.required, Validators.minLength(10)]]
  });

  get f() { return this.form.controls; }

  ngOnInit(): void {
    this.adminService.title.set('Submit Claim');
    const preSelectedPolicy = this.route.snapshot.queryParamMap.get('policyId');

    this.policyService.getMyPolicies().subscribe({
      next: (res: any[]) => {
        const processed = (res || []).map(p => ({
          ...p,
          isSelectable: p.status === 'ACTIVE' || (p.status?.toString().toUpperCase() === 'ACTIVE')
        }));
        
        this.policies.set(processed);
        this.loading.set(false);
        
        if (preSelectedPolicy) {
          const exists = processed.find(p => p.policyId == preSelectedPolicy);
          if (exists && exists.isSelectable) {
            this.form.patchValue({ policyId: preSelectedPolicy });
          } else if (exists) {
            console.warn("Pre-selected policy found but is not ACTIVE.");
          }
        }
      },
      error: (err) => {
        console.error("Failed to load policies", err);
        this.loading.set(false);
      }
    });
  }

  ngAfterViewInit(): void {
    this.refreshIcons();
  }

  onFileSelected(event: any) {
    this.selectedFile = event.target.files[0];
  }

  submit(): void {
    if (this.form.invalid || this.submitting()) return;
    
    const rawValue = this.form.value;
    const policyId = Number(rawValue.policyId);
    const claimAmount = Number(rawValue.claimAmount);

    if (isNaN(policyId) || isNaN(claimAmount)) {
      this.toastService.warning("Invalid numeric values in form.");
      return;
    }

    this.submitting.set(true);
    
    const payload = {
      policyId: policyId,
      claimAmount: claimAmount,
      description: rawValue.description
    };

    this.claimService.submitClaim(payload).subscribe({
      next: (claim: any) => {
        if (this.selectedFile) {
          this.claimService.uploadDocument(claim.claimId, this.selectedFile).subscribe({
            next: () => this.finishSubmission(),
            error: (err) => {
              const msg = err.error?.message || "Document upload failed";
              this.toastService.warning("Claim raised, but: " + msg);
              this.finishSubmission();
            }
          });
        } else {
          this.finishSubmission();
        }
      },
      error: (err) => {
        const msg = err.error?.message || "The server rejected this claim. Please ensure the policy is active and amount is valid.";
        this.toastService.error("Submission Failed: " + msg);
        this.submitting.set(false);
      }
    });
  }

  private finishSubmission() {
    this.toastService.success("Claim submitted successfully.");
    this.router.navigate(['/company/claim-history']);
  }

  private refreshIcons(): void {
    setTimeout(() => {
      if (typeof lucide !== 'undefined') lucide.createIcons();
    }, 0);
  }
}
