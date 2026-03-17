import { Component, inject, OnInit, AfterViewInit, signal, computed, ChangeDetectionStrategy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AdminService } from '../../../../services/admin/admin.service';
import { ClaimService } from '../../../../services/claim/claim.service';
import { Claim } from '../../../../models/claim.model';
import { RouterLink } from '@angular/router';

declare const lucide: any;

@Component({
  selector: 'app-claim-history',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './claim-history.html',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class ClaimHistory implements OnInit, AfterViewInit {
  private adminService = inject(AdminService);
  private claimService = inject(ClaimService);

  claims = signal<Claim[]>([]);
  loading = signal<boolean>(true);

  totalClaimed = computed(() =>
    this.claims().reduce((sum, c) => sum + (c.claimAmount || 0), 0)
  );

  approvedAmount = computed(() =>
    this.claims()
      .filter(c => c.status === 'SETTLED' || c.status === 'PAYMENT_IN_PROGRESS')
      .reduce((sum, c) => sum + (c.payoutAmount || 0), 0)
  );

  ngOnInit(): void {
    this.adminService.title.set('Claim Records');
    this.loadHistory();
  }

  loadHistory() {
    this.claimService.getCompanyClaims().subscribe({
      next: (res: any) => {
        this.claims.set(res || []);
        this.loading.set(false);
        this.refreshIcons();
      },
      error: (err) => {
        console.error("Failed to fetch claims", err);
        this.loading.set(false);
      }
    });
  }


  ngAfterViewInit(): void {
    this.refreshIcons();
  }

  private refreshIcons(): void {
    setTimeout(() => {
      if (typeof lucide !== 'undefined') lucide.createIcons();
    }, 0);
  }
}
