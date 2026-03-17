import { Component, inject, OnInit, signal } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { PolicyService } from '../../../../services/policy/policy.service';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-policies',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './policies.html',
  styleUrl: './policies.css',
})
export class Policies implements OnInit {
  private policyService = inject(PolicyService);
  private router = inject(Router);
  private route = inject(ActivatedRoute);

  policies = signal<any[]>([]);
  riskScore = signal<any>(null);
  loading = signal<boolean>(true);
  systemId = signal<string | null>(null);

  ngOnInit(): void {
    const sId = this.route.snapshot.queryParamMap.get('systemId');
    this.systemId.set(sId);

    console.log("Policies component init - fetching data...");

    // Safety fallback: ensure loading is cleared even if subscription hangs
    setTimeout(() => {
      if (this.loading()) {
        console.warn("Policies: API call taking too long, force-clearing loading state.");
        this.loading.set(false);
      }
    }, 5000);

    this.policyService.getPolicyTypes().subscribe({
      next: (data: any) => {
        console.log("Policies: received data", data);
        this.policies.set(data || []);
        this.loading.set(false);
      },
      error: (err: any) => {
        console.error("Failed to load policies", err);
        const msg = err.error?.message || "Failed to load policies";
        alert(msg);
        this.loading.set(false);
      }
    });

    // Optionally fetch risk score if systemId is available
    // For now keeping it simple as per existing flow
  }

  applyPolicy(policyId: number) {
    const queryParams: any = {};
    if (this.systemId()) {
      queryParams.systemId = this.systemId();
    }

    this.router.navigate(['/company/apply-policy', policyId], {
      queryParams: queryParams
    });
  }
}
