import { Component, inject, OnInit, signal } from '@angular/core';
import { RouterLink } from '@angular/router';
import { UnderwriterService } from '../../../services/underwriter/underwriter.service';
import { AdminService } from '../../../services/admin/admin.service';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-underwriter-dashboard',
  standalone: true,
  imports: [RouterLink, CommonModule],
  templateUrl: './underwriter-dashboard.html',
  styleUrl: './underwriter-dashboard.css',
})
export class UnderwriterDashboard implements OnInit {
  private underwriterService = inject(UnderwriterService);
  private adminService = inject(AdminService);
  
  stats = signal<any>({
    assignedTasks: 0,
    pendingReviews: 0,
    completedToday: 0
  });
  assignedPolicies = signal<any[]>([]);
  loading = signal(true);

  ngOnInit(): void {
    this.adminService.title.set('Underwriter Dashboard');
    this.underwriterService.getDashboardStats().subscribe({
      next: (data) => {
        this.stats.set(data);
        this.loadRecentPolicies();
      },
      error: () => {
        this.loading.set(false);
      }
    });
  }

  loadRecentPolicies() {
    this.underwriterService.getMyPolicies().subscribe({
      next: (data) => {
        this.assignedPolicies.set(data?.slice(0, 5) || []);
        this.loading.set(false);
        this.refreshIcons();
      },
      error: () => {
        this.loading.set(false);
      }
    });
  }

  private refreshIcons() {
    setTimeout(() => {
      // @ts-ignore
      if (typeof lucide !== 'undefined') lucide.createIcons();
    }, 0);
  }
}

