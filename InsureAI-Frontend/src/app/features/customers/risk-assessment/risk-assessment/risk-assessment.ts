import { Component, inject, OnInit, AfterViewInit, signal } from '@angular/core';
import { FormBuilder, Validators, ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { AiSystemService } from '../../../../services/ai-system/ai-system.service';
import { RiskService } from '../../../../services/risk/risk.service';
import { CommonModule } from '@angular/common';
import { ToastService } from '../../../../services/toast/toast.service';

declare const lucide: any;

@Component({
  selector: 'app-risk-assessment',
  imports: [ReactiveFormsModule, CommonModule],
  templateUrl: './risk-assessment.html',
  styleUrl: './risk-assessment.css'
})
export class RiskAssessment implements OnInit, AfterViewInit {

  ngAfterViewInit() {
    if (typeof lucide !== 'undefined') {
      lucide.createIcons();
    }
  }

  fb = inject(FormBuilder);
  router = inject(Router);
  route = inject(ActivatedRoute);
  aiService = inject(AiSystemService);
  riskService = inject(RiskService);
  toastService = inject(ToastService);

  systems = signal<any[]>([]);
  riskResult = signal<any>(null);

  get f() { return this.riskForm.controls; }

  riskForm = this.fb.group({

    aiSystemId: ['', Validators.required],

    humanOversight: [false],
    biasTesting: [false],
    auditLogsMaintained: [false],

    dataExposureCategory: ['', Validators.required],

    lifeCriticalUsage: [false],

    financialImpactLevel: ['', Validators.required],

    pastIncidentCount: [0]

  });

  ngOnInit() {

    // load AI systems for dropdown
    this.aiService.getSystems().subscribe({
      next: (res) => {
        this.systems.set(res || []);
      }
    });

    this.route.paramMap.subscribe(params => {
      const systemId = params.get('systemId');
      if (systemId) {
        this.riskForm.patchValue({ aiSystemId: systemId });
      }
    });

  }

  submit() {

    if (this.riskForm.invalid) return;

    this.riskService.createRisk(this.riskForm.value).subscribe({

      next: (res) => {
        this.riskResult.set(res);
      },
      error: (err) => {
        console.error("Risk assessment failed", err);
        const msg = err.error?.message || "Failed to evaluate risk. Please ensure all values form a valid system request.";
        this.toastService.error(msg);
      }

    });

  }

  cancel() {
    this.router.navigate(['/company/ai-system']);
  }

}