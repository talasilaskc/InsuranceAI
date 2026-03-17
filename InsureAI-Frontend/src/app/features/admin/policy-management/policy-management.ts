import { Component, inject, OnInit, signal, computed, ChangeDetectionStrategy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { AdminService } from '../../../services/admin/admin.service';
import { PolicyTypeService } from '../../../services/policy-type/policy-type.service';

declare const lucide: any;

@Component({
  selector: 'app-policy-management',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './policy-management.html',
  styleUrl: './policy-management.css',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class PolicyManagement implements OnInit {

  adminService: AdminService = inject(AdminService);
  policyTypeService: PolicyTypeService = inject(PolicyTypeService);

  policyTypes = signal<any[]>([]);
  loading = signal(true);

  activeCount = computed(() => this.policyTypes().filter(p => p.active).length);

  ngOnInit(): void {
    this.adminService.title.set('Insurance Products');

    this.policyTypeService.getPolicyTypes().subscribe({
      next: (data: any) => {
        this.policyTypes.set(data || []);
        this.loading.set(false);
        this.refreshIcons();
      },
      error: (err) => {
        console.error('Failed to load policy types:', err);
        const msg = err.error?.message || "Failed to load policy types";
        alert(msg);
        this.loading.set(false);
      }
    });
  }

  private refreshIcons(): void {
    setTimeout(() => {
      if (typeof lucide !== 'undefined') {
        lucide.createIcons();
      }
    }, 0);
  }

  deactivate(id: number) {
    if (!confirm("Deactivate this policy type? It will not be available for new applications.")) return;

    this.policyTypeService.deletePolicyType(id).subscribe({
      next: () => {
        // Optimistically update the list by setting active to false rather than removing it entirely
        // or removing it if the backend deletes it. Let's just remove it or set it to inactive.
        this.policyTypes.update(types => types.map(t => t.id === id ? { ...t, active: false } : t));
      },
      error: (err) => {
        console.error('Failed to deactivate policy type:', err);
        const msg = err.error?.message || "Failed to deactivate policy product.";
        alert(msg);
      }
    });
  }

}
