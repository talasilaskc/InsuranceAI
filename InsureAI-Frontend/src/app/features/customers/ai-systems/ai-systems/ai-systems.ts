import { Component, inject, OnInit, AfterViewInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AdminService } from '../../../../services/admin/admin.service';
import { AiSystemService } from '../../../../services/ai-system/ai-system.service';
import { RouterLink } from '@angular/router';
import { CompanyService } from '../../../../services/company/company.service';
import { PolicyService } from '../../../../services/policy/policy.service';
import { forkJoin } from 'rxjs';

declare const lucide: any;

@Component({
  selector: 'app-ai-systems',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './ai-systems.html',
  styleUrl: './ai-systems.css',
})
export class AiSystems implements OnInit, AfterViewInit {
  companyService = inject(CompanyService);
  aiService: AiSystemService = inject(AiSystemService);
  policyService = inject(PolicyService);

  systems = signal<any[]>([]);
  policies = signal<any[]>([]);
  loading = signal<boolean>(true);

  ngAfterViewInit() {
    if (typeof lucide !== 'undefined') {
      lucide.createIcons();
    }
  }

  ngOnInit(): void {
    this.companyService.title.set('AI Systems');
    console.log("AiSystems init - fetching from service...");

    // Timeout fallback to prevent stuck spinner if call hangs indefinitely
    setTimeout(() => {
      if (this.loading()) {
        console.warn("AiSystems: API call taking too long, force-clearing loading state.");
        this.loading.set(false);
      }
    }, 5000);

    // Fetch both systems and policies
    forkJoin({
      systems: this.aiService.getSystems(),
      policies: this.policyService.getMyPolicies()
    }).subscribe({
      next: (res: any) => {
        console.log("AiSystems dashboard data:", res);
        this.systems.set(res.systems || []);
        this.policies.set(res.policies || []);
        this.loading.set(false);
        this.refreshIcons();
      },
      error: (err: any) => {
        console.error("AiSystems: fetch error", err);
        this.loading.set(false);
      }
    });
  }

  getActivePolicyForSystem(systemId: number) {
    return this.policies().find(p => p.aiSystemId === systemId && p.status === 'ACTIVE');
  }

  private refreshIcons(): void {
    setTimeout(() => {
      if (typeof lucide !== 'undefined') {
        lucide.createIcons();
      }
    }, 0);
  }

  getSystemIcon(modelType: string): string {
    switch (modelType) {
      case 'LLM': return 'bot';
      case 'NLP': return 'message-square';
      case 'CV': return 'eye';
      case 'RISK_ENGINE': return 'shield-alert';
      default: return 'cpu';
    }
  }

  getSystemColor(modelType: string): string {
    switch (modelType) {
      case 'LLM': return 'bg-purple-100 text-purple-600';
      case 'NLP': return 'bg-blue-100 text-blue-600';
      case 'CV': return 'bg-emerald-100 text-emerald-600';
      case 'RISK_ENGINE': return 'bg-amber-100 text-amber-600';
      default: return 'bg-slate-100 text-slate-600';
    }
  }
}
