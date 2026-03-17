import { Component, inject, OnInit, signal, computed, ChangeDetectionStrategy, AfterViewInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ClaimService } from '../../../services/claim/claim.service';
import { AdminService } from '../../../services/admin/admin.service';
import { Claim, ClaimStatus } from '../../../models/claim.model';
import { RouterLink } from '@angular/router';
import { ToastService } from '../../../services/toast/toast.service';

declare const lucide: any;

@Component({
  selector: 'app-claims',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './claims.html',
  styleUrl: './claims.css',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class Claims implements OnInit, AfterViewInit {
  private claimService = inject(ClaimService);
  private adminService = inject(AdminService);
  private toastService = inject(ToastService);

  claims = signal<Claim[]>([]);
  officers = signal<any[]>([]);
  loading = signal(true);
  statusFilter = signal<string>('');
  
  // Modal State
  selectedClaimId = signal<number | null>(null);
  selectedOfficerId = signal<number | null>(null);
  showAssignModal = signal(false);

  filteredClaims = computed(() => {
    const status = this.statusFilter();
    if (!status) return this.claims();
    return this.claims().filter(c => c.status === status);
  });

  ngOnInit(): void {
    this.adminService.title.set("Claims Management");
    this.loadData();
  }

  loadData() {
    this.loading.set(true);
    this.claimService.getAllClaims().subscribe({
      next: (data) => {
        this.claims.set(data || []);
        this.loading.set(false);
        this.refreshIcons();
      },
      error: (err) => {
        console.error("Failed to load claims", err);
        this.loading.set(false);
      }
    });

    this.claimService.getOfficers().subscribe({
      next: (res) => this.officers.set(res),
      error: (err) => console.error("Failed to load officers", err)
    });
  }

  ngAfterViewInit(): void {
    this.refreshIcons();
  }

  filterClaims(event: Event) {
    const target = event.target as HTMLSelectElement;
    this.statusFilter.set(target.value);
    this.refreshIcons();
  }

  private refreshIcons(): void {
    setTimeout(() => {
      if (typeof lucide !== 'undefined') {
        lucide.createIcons();
      }
    }, 0);
  }

  openAssignModal(claimId: number) {
    this.selectedClaimId.set(claimId);
    this.showAssignModal.set(true);
  }

  confirmAssignment() {
    if (!this.selectedClaimId() || !this.selectedOfficerId()) return;

    this.claimService.assignOfficer(this.selectedClaimId()!, this.selectedOfficerId()!).subscribe({
      next: () => {
        this.toastService.success("Officer assigned successfully.");
        this.showAssignModal.set(false);
        this.loadData();
      },
      error: (err) => this.toastService.error(err.error?.message || "Assignment failed")
    });
  }

  markUnderReview(claimId: number) {
    this.claimService.markUnderReview(claimId).subscribe({
      next: () => {
        this.toastService.success("Claim moved to Admin Review.");
        this.loadData();
      },
      error: (err) => this.toastService.error(err.error?.message || "Action failed")
    });
  }

  markSettled(claimId: number) {
    this.claimService.markSettled(claimId).subscribe({
      next: () => {
        this.toastService.success("Claim marked as settled.");
        this.loadData();
      },
      error: (err) => this.toastService.error(err.error?.message || "Action failed")
    });
  }
}
