import { Component, inject, OnInit, signal, computed, AfterViewInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ClaimService } from '../../../services/claim/claim.service';
import { Claim, ClaimStatus } from '../../../models/claim.model';
import { RouterLink } from '@angular/router';
import { FormsModule } from '@angular/forms';

declare const lucide: any;

@Component({
  selector: 'app-officer-dashboard',
  standalone: true,
  imports: [CommonModule, RouterLink, FormsModule],
  templateUrl: './dashboard.html',
  styleUrl: './dashboard.css'
})
export class OfficerDashboard implements OnInit, AfterViewInit {
  private claimService = inject(ClaimService);
  
  claims = signal<Claim[]>([]);
  loading = signal<boolean>(true);
  searchTerm = signal<string>('');
  statusFilter = signal<string>('ALL');

  stats = computed(() => {
    const all = this.claims();
    return {
      total: all.length,
      pending: all.filter(c => c.status === ClaimStatus.ASSIGNED_TO_OFFICER).length,
      investigating: all.filter(c => c.status === ClaimStatus.UNDER_INVESTIGATION).length,
      recommended: all.filter(c => c.status === ClaimStatus.OFFICER_RECOMMENDED).length
    };
  });

  filteredClaims = computed(() => {
    let list = this.claims();
    
    // Filter by status
    if (this.statusFilter() !== 'ALL') {
      list = list.filter(c => c.status === this.statusFilter());
    }

    // Filter by search term
    const search = this.searchTerm().toLowerCase();
    if (search) {
      list = list.filter(c => 
        c.claimId.toString().includes(search) || 
        c.companyName?.toLowerCase().includes(search) ||
        c.aiSystemName?.toLowerCase().includes(search) ||
        c.description.toLowerCase().includes(search)
      );
    }

    return list;
  });

  ngOnInit(): void {
    this.loadAssignedClaims();
  }

  ngAfterViewInit() {
    if (typeof lucide !== 'undefined') lucide.createIcons();
  }

  loadAssignedClaims() {
    this.loading.set(true);
    this.claimService.getAssignedClaims().subscribe({
      next: (res: Claim[]) => {
        this.claims.set(res || []);
        this.loading.set(false);
        setTimeout(() => {
          if (typeof lucide !== 'undefined') lucide.createIcons();
        }, 0);
      },
      error: (err: any) => {
        console.error("Dashboard failed to load", err);
        this.loading.set(false);
      }
    });
  }

  getStatusClass(status: string): string {
    switch (status) {
      case 'ASSIGNED_TO_OFFICER': return 'text-amber-600 border-amber-500 bg-amber-50/50';
      case 'UNDER_INVESTIGATION': return 'text-primary border-primary bg-primary/5';
      case 'OFFICER_RECOMMENDED': return 'text-emerald-600 border-emerald-500 bg-emerald-50/50';
      case 'REJECTED': return 'text-rose-600 border-rose-500 bg-rose-50/50';
      default: return 'text-muted border-line bg-surface';
    }
  }
}
