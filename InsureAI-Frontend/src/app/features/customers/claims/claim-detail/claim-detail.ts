import { Component, inject, OnInit, signal, AfterViewInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { ClaimService } from '../../../../services/claim/claim.service';
import { AdminService } from '../../../../services/admin/admin.service';
import { Claim } from '../../../../models/claim.model';
import { ClaimTimelineComponent } from '../../../shared/components/claim-timeline/claim-timeline';
import { ClaimDocumentsComponent } from '../../../shared/components/claim-documents/claim-documents';

declare const lucide: any;

@Component({
  selector: 'app-claim-detail',
  standalone: true,
  imports: [CommonModule, RouterLink, ClaimTimelineComponent, ClaimDocumentsComponent],
  templateUrl: './claim-detail.html',
  styleUrl: './claim-detail.css'
})
export class ClaimDetailComponent implements OnInit, AfterViewInit {
  private route = inject(ActivatedRoute);
  private claimService = inject(ClaimService);
  private adminService = inject(AdminService);

  claimId = Number(this.route.snapshot.params['id']);
  claim = signal<Claim | null>(null);
  loading = signal(true);

  ngOnInit(): void {
    this.adminService.title.set("Claim Lifecycle Detail");
    this.loadClaim();
  }

  loadClaim() {
    this.loading.set(true);
    this.claimService.getClaim(this.claimId).subscribe({
      next: (data) => {
        this.claim.set(data);
        this.loading.set(false);
        this.refreshIcons();
      },
      error: (err) => {
        console.error(err);
        this.loading.set(false);
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
}
