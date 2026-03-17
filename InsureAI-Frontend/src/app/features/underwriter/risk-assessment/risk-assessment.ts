import { Component, inject, OnInit, AfterViewInit, signal, OnDestroy } from '@angular/core';
import { FormBuilder, Validators, ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { UnderwriterService } from '../../../services/underwriter/underwriter.service';
import { AdminService } from '../../../services/admin/admin.service';
import { Subscription } from 'rxjs';
import { CommonModule } from '@angular/common';
import { ToastService } from '../../../services/toast/toast.service';

declare const lucide: any;

@Component({
  selector: 'app-risk-assessment',
  standalone: true,
  imports: [ReactiveFormsModule, CommonModule],
  templateUrl: './risk-assessment.html',
  styleUrl: './risk-assessment.css'
})
export class RiskAssessment implements OnInit, AfterViewInit, OnDestroy {

  private fb = inject(FormBuilder);
  private router = inject(Router);
  private route = inject(ActivatedRoute);
  private underwriterService = inject(UnderwriterService);
  private adminService = inject(AdminService);
  private toastService = inject(ToastService);
  private paramSub?: Subscription;

  policy = signal<any>(null);
  riskResult = signal<any>(null);

  policyId = signal<number | null>(null);
  submitting = signal(false);
  loading = signal(true);

  get f() { return this.riskForm.controls; }

  riskForm = this.fb.group({
    humanOversight: [false],
    biasTesting: [false],
    auditLogsMaintained: [false],
    dataExposureCategory: ['', Validators.required],
    lifeCriticalUsage: [false],
    financialImpactLevel: ['', Validators.required],
    pastIncidentCount: [0],
    remarks: ['']
  });

  ngOnInit() {
    this.adminService.title.set('Policy Risk Audit');

    this.paramSub = this.route.paramMap.subscribe(params => {
      const id = params.get('id');
      console.log(id);
      if (id) {
        this.policyId.set(Number(id));
        this.loadPolicy(this.policyId()!);
      }
    });
  }

  loadPolicy(id: number) {
    this.loading.set(true);
    
    this.underwriterService.getPolicyById(id).subscribe({
      next: (data) => {

        // lifecycle guard
        if (data?.status !== 'UNDER_REVIEW') {
          this.toastService.warning("This policy is not in underwriting stage.");
          this.router.navigate(['/underwriter/policies']);
          return;
        }

        this.policy.set(data);
        this.loading.set(false);

        this.refreshIcons();
      },
      error: () => {
        this.loading.set(false);
      }
    });
  }

  refreshIcons() {
    setTimeout(() => lucide?.createIcons(), 0);
  }

  ngAfterViewInit() {
    lucide?.createIcons();
  }

  ngOnDestroy(): void {
    this.paramSub?.unsubscribe();
  }

  submitRisk() {

    if (this.riskForm.invalid || !this.policyId()) return;

    this.submitting.set(true);

    this.underwriterService.submitRisk(
      this.policyId()!,
      this.riskForm.value
    ).subscribe({
      next: () => {
        this.toastService.success("Risk assessment submitted successfully");
        this.router.navigate(['/underwriter/policies']);
      },
      error: (err) => {
        console.error("Risk submission failed", err);
        this.toastService.error(err.error?.message || "Submission failed");
        this.submitting.set(false);
      }
    });
  }

  cancel() {
    this.router.navigate(['/underwriter/policies']);
  }

}