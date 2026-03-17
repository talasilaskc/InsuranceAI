import { Component, inject, OnInit, signal, ChangeDetectionStrategy } from '@angular/core';
import { FormBuilder, Validators, ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { AiSystemService } from '../../../../services/ai-system/ai-system.service';
import { PolicyService } from '../../../../services/policy/policy.service';
import { CommonModule } from '@angular/common';
import { CompanyService } from '../../../../services/company/company.service';
import { ToastService } from '../../../../services/toast/toast.service';

@Component({
  selector: 'app-apply-policy',
  standalone: true,
  imports: [ReactiveFormsModule, CommonModule],
  templateUrl: './apply-policy.html',
  styleUrl: './apply-policy.css',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class ApplyPolicy implements OnInit {

  private fb = inject(FormBuilder);
  private route = inject(ActivatedRoute);
  private router = inject(Router);
  private aiService = inject(AiSystemService);
  private policyService = inject(PolicyService);
  companyService = inject(CompanyService);
  private toastService = inject(ToastService);

  systems = signal<any[]>([]);
  policyTypeId = signal<number>(0);
  selectedPolicy = signal<any>(null);
  loading = signal<boolean>(true);

  get f() { return this.form.controls; }

  form = this.fb.group({
    aiSystemId: ['', Validators.required],
    coverageLimit: [50000, Validators.required],
    deductibleAmount: [1000, Validators.required],
    tenureYears: [1, Validators.required]
  });

  ngOnInit() {
    this.companyService.title.set('Apply Policy');
    const id = Number(this.route.snapshot.paramMap.get('policyId'));
    this.policyTypeId.set(id);

    console.log("ApplyPolicy init - fetching systems and policy info...");

    // Fetch systems
    this.aiService.getSystems().subscribe({
      next: (res) => this.systems.set(res || []),
      error: (err) => console.error("Failed to fetch systems", err)
    });

    // Fetch policy types to find the selected one
    this.policyService.getPolicyTypes().subscribe({
      next: (types) => {
        const found = types.find((t: any) => t.id === id);
        if (found) {
          this.selectedPolicy.set(found);
          // Set coverage default if available
          if (found.minCoverage) {
            this.form.patchValue({ coverageLimit: found.minCoverage });
          }
        }
        this.loading.set(false);
      },
      error: (err) => {
        console.error("Failed to fetch policy types", err);
        this.loading.set(false);
      }
    });

    // Handle safety fallback separately if needed, but here we have two calls.
    // I'll keep the logic simple.

    // If systemId provided in queryParams, pre-select it
    this.route.queryParamMap.subscribe(params => {
      const selectedSystem = params.get('systemId');
      if (selectedSystem) {
        this.form.patchValue({ aiSystemId: selectedSystem });
      }
    });
  }

  submit() {
    if (this.form.invalid) return;

    const request = {
      ...this.form.value,
      policyTypeId: this.policyTypeId()
    };

    this.policyService.issuePolicy(request).subscribe({
      next: () => {
        this.toastService.success("Policy application submitted successfully. Waiting for administrator approval.");
        this.router.navigate(['/company/my-policies']);
      },
      error: (err) => {
        console.error("Failed to submit policy", err);
        const msg = err.error?.message || "We encountered an issue submitting your application. Please verify details and try again.";
        this.toastService.error(msg);
      }
    });
  }

}