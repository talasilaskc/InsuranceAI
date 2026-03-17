import { Component, OnInit, inject, signal, computed, AfterViewInit } from '@angular/core';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { UnderwriterService } from '../../../services/underwriter/underwriter.service';
import { AdminService } from '../../../services/admin/admin.service';
import { FormsModule } from '@angular/forms';
import { ToastService } from '../../../services/toast/toast.service';

declare const lucide: any;

@Component({
  selector: 'app-assigned-policies',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './assigned-policies.html',
  styleUrl: './assigned-policies.css',
})
export class AssignedPolicies implements OnInit, AfterViewInit {

  private underwriterService = inject(UnderwriterService);
  private adminService = inject(AdminService);
  private router = inject(Router);
  private toastService = inject(ToastService);

  policies = signal<any[]>([]);
  loading = signal(true);
  searchTerm = signal('');

  filteredPolicies = computed(() => {
    const term = this.searchTerm().toLowerCase();
    const all = this.policies();
    if (!term) return all;
    return all.filter(p => 
      p.id.toString().includes(term) || 
      p.aiSystem?.name?.toLowerCase().includes(term)
    );
  });

  ngOnInit(): void {
    this.adminService.title.set('My Assignments');
    this.loadPolicies();
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

  loadPolicies() {
    this.loading.set(true);
    this.underwriterService.getMyPolicies().subscribe({
      next: (data: any) => {
        this.policies.set(data || []);
        this.loading.set(false);
        this.refreshIcons();
      },
      error: (err) => {
        console.error("Failed to load assigned policies", err);
        this.toastService.error("Failed to load policies");
        this.loading.set(false);
      }
    });
  }

  goToRisk(policyId: number) {
    console.log(policyId);
    this.router.navigate(['/underwriter/policy', policyId, 'risk']);
  }
}
