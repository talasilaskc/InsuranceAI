import { Component, inject, OnInit, signal, computed, ChangeDetectionStrategy, AfterViewInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { AdminService } from '../../../services/admin/admin.service';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Subscription } from 'rxjs';
import { ToastService } from '../../../services/toast/toast.service';

declare const lucide: any;

@Component({
  selector: 'app-policy-review',
  standalone: true,
  imports: [CommonModule, RouterLink, FormsModule],
  templateUrl: './policy-review.html',
  styleUrl: './policy-review.css',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class PolicyReview implements OnInit, AfterViewInit, OnDestroy {

  private adminService = inject(AdminService);
  private route = inject(ActivatedRoute);
  private router = inject(Router);
  private toastService = inject(ToastService);
  private paramSub?: Subscription;

  policy = signal<any>(null);
  loading = signal(true);
  submitting = signal(false);

  underwriters = signal<any[]>([]);
  selectedUnderwriterId = signal<number | null>(null);
  assigning = signal(false);

  overridePremiumValue = signal<number | null>(null);
  overriding = signal(false);

  availableUnderwriters = computed(() => {
    const all = this.underwriters();
    const assignedId = this.policy()?.underwriter?.id;
    return assignedId ? all.filter(u => u.id !== assignedId) : all;
  });

  ngOnInit(): void {
    this.adminService.title.set('Review Application');
    this.loadUnderwriters();

    this.paramSub = this.route.paramMap.subscribe(params => {
      const id = Number(params.get('id'));
      if (id) this.loadPolicyData(id);
    });
  }

  loadUnderwriters() {
    this.adminService.getAllUnderwriters().subscribe({
      next: data => this.underwriters.set(data || []),
      error: err => console.error("Failed to load underwriters", err)
    });
  }

  loadPolicyData(id: number) {
    this.loading.set(true);
    this.adminService.getPolicyById(id).subscribe({
      next: (data: any) => {
        this.policy.set(data);
        this.loading.set(false);
        if (data?.underwriter?.id) {
          this.selectedUnderwriterId.set(data.underwriter.id);
        }
        if (data?.premiumAmount) {
          this.overridePremiumValue.set(data.premiumAmount);
        }
        console.log("Policy loaded:", { id: data.id, status: data.status, underwriter: data.underwriter?.fullName });
        console.log("Policy status:", this.policy()?.status);
        this.refreshIcons();
      },
      error: err => {
        console.error("Failed to load policy details", err);
        this.loading.set(false);
      }
    });
  }

  ngAfterViewInit(): void {
    this.refreshIcons();
  }

  ngOnDestroy(): void {
    this.paramSub?.unsubscribe();
  }

  private refreshIcons(): void {
    setTimeout(() => lucide?.createIcons(), 0);
  }

  assignUnderwriter() {
    console.log("Assigning underwriter...", {
      policyId: this.policy()?.id,
      underwriterId: this.selectedUnderwriterId(),
      status: this.policy()?.status
    });

    if (!this.selectedUnderwriterId() || !this.policy()) {
      console.warn("Assignment blocked: Missing selection or policy");
      return;
    }

    this.assigning.set(true);

    this.adminService.assignUnderwriter(
      this.policy().id,
      this.selectedUnderwriterId()!
    ).subscribe({
      next: (res) => {
        console.log("Assignment successful", res);
        this.toastService.success("Underwriter assigned successfully");
        this.assigning.set(false);
        this.loadPolicyData(this.policy().id);
      },
      error: err => {
        console.error("Assignment failed", err);
        this.toastService.error(err.error?.message || "Assignment failed. Please check if the API exists.");
        this.assigning.set(false);
      }
    });
  }

  overridePremium() {
    if (!this.overridePremiumValue() || !this.policy()) return;
    if (!confirm("Override calculated premium?")) return;

    this.overriding.set(true);

    this.adminService.overridePremium(
      this.policy().id,
      this.overridePremiumValue()!
    ).subscribe({
      next: () => {
        this.toastService.success("Premium updated successfully");
        this.overridePremiumValue.set(null);
        this.overriding.set(false);
        this.loadPolicyData(this.policy().id);
      },
      error: err => {
        console.error("Premium override failed", err);
        this.toastService.error(err.error?.message || "Premium update failed");
        this.overriding.set(false);
      }
    });
  }

  approvePolicy() {
    if (this.submitting() || !this.policy()) return;
    if (!confirm("Confirm approval of this policy application?")) return;

    this.submitting.set(true);

    this.adminService.approvePolicy(this.policy().id).subscribe({
      next: () => this.router.navigate(['/admin/pending-policies']),
      error: err => {
        console.error("Failed to approve policy", err);
        this.toastService.error(err.error?.message || "Approval failed");
        this.submitting.set(false);
      }
    });
  }

  rejectPolicy() {
    if (this.submitting() || !this.policy()) return;
    if (!confirm("Are you sure you want to reject this policy application?")) return;

    this.submitting.set(true);

    this.adminService.rejectPolicy(this.policy().id).subscribe({
      next: () => this.router.navigate(['/admin/pending-policies']),
      error: err => {
        console.error("Failed to reject policy", err);
        this.toastService.error(err.error?.message || "Rejection failed");
        this.submitting.set(false);
      }
    });
  }

}