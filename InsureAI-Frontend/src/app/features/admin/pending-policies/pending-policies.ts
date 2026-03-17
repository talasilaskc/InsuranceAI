import { Component, inject, OnInit, signal, computed, ChangeDetectionStrategy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { AdminService } from '../../../services/admin/admin.service';
import { ToastService } from '../../../services/toast/toast.service';

@Component({
  selector: 'app-pending-policies',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './pending-policies.html',
  styleUrl: './pending-policies.css',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class PendingPolicies implements OnInit {
  private adminService = inject(AdminService);
  private toastService = inject(ToastService);

  pendingPolicies = signal<any[]>([]);
  loading = signal(true);

  highRiskCount = computed(() =>
    this.pendingPolicies().filter(p => p.riskSnapshot?.riskLevel === 'HIGH').length
  );

  totalPendingCount = computed(() => this.pendingPolicies().length);

  ngOnInit(): void {
    this.adminService.title.set('Pending Policies');
    this.loadPolicies();
  }

  loadPolicies() {
    this.loading.set(true);
    this.adminService.getPendingPolicies().subscribe({
      next: (data: any) => {
        this.pendingPolicies.set(data || []);
        console.log(this.pendingPolicies());
        this.loading.set(false);
      },
      error: (error) => {
        console.error("Failed to load pending policies", error);
        const msg = error.error?.message || "Failed to load pending policies";
        this.toastService.error(msg);
        this.loading.set(false);
      }
    });
  }

  approve(id: number) {
    if (!confirm("Are you sure you want to approve this policy application?")) return;

    this.adminService.approvePolicy(id).subscribe({
      next: () => {
        this.toastService.success("Policy approved successfully.");
        this.loadPolicies();
      },
      error: (err) => {
        console.error("Failed to approve policy", err);
        const msg = err.error?.message || "Action failed. Please try again.";
        this.toastService.error(msg);
      }
    });
  }

  reject(id: number) {
    if (!confirm("Are you sure you want to reject this policy application?")) return;

    this.adminService.rejectPolicy(id).subscribe({
      next: () => {
        this.toastService.info("Policy application rejected.");
        this.loadPolicies();
      },
      error: (err) => {
        console.error("Failed to reject policy", err);
        const msg = err.error?.message || "Action failed. Please try again.";
        this.toastService.error(msg);
      }
    });
  }
}
