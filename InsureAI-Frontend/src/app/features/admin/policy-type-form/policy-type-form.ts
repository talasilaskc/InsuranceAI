import { Component, inject, OnInit, signal, ChangeDetectionStrategy, AfterViewInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { AdminService } from '../../../services/admin/admin.service';
import { PolicyTypeService } from '../../../services/policy-type/policy-type.service';

declare const lucide: any;

@Component({
  selector: 'app-policy-type-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterLink],
  templateUrl: './policy-type-form.html',
  styleUrl: './policy-type-form.css',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class PolicyTypeForm implements OnInit, AfterViewInit {
  private fb = inject(FormBuilder);
  private route = inject(ActivatedRoute);
  private router = inject(Router);
  private adminService = inject(AdminService);
  private policyTypeService = inject(PolicyTypeService);
  private http = inject(HttpClient);

  isEditMode = signal<boolean>(false);
  policyTypeId = signal<number | null>(null);
  loading = signal<boolean>(false);
  submitting = signal<boolean>(false);

  get f() { return this.form.controls; }

  form = this.fb.group({
    name: ['', Validators.required],
    description: ['', [Validators.required, Validators.maxLength(500)]],
    basePrice: [0, [Validators.required, Validators.min(0)]],
    minCoverage: [0, [Validators.required, Validators.min(0)]],
    maxCoverage: [0, [Validators.required, Validators.min(0)]],
    active: [true]
  });

  ngOnInit(): void {
    const idParam = this.route.snapshot.paramMap.get('id');

    if (idParam) {
      this.isEditMode.set(true);
      this.policyTypeId.set(+idParam);
      this.adminService.title.set('Edit Product Configuration');
      this.loadPolicyType(+idParam);
    } else {
      this.isEditMode.set(false);
      this.adminService.title.set('Create New Product');
    }
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

  loadPolicyType(id: number) {
    this.loading.set(true);

    // Fetch all policy types and filter locally to avoid potential 404s if direct GET /id isn't available
    this.policyTypeService.getPolicyTypes().subscribe({
      next: (data: any[]) => {
        const targetPolicy = data.find(p => p.id === id);

        if (targetPolicy) {
          this.form.patchValue({
            name: targetPolicy.name,
            description: targetPolicy.description,
            basePrice: targetPolicy.basePrice,
            minCoverage: targetPolicy.minCoverage,
            maxCoverage: targetPolicy.maxCoverage,
            active: targetPolicy.isActive !== undefined ? targetPolicy.isActive : targetPolicy.active // Handle mismatch in isActive vs active naming
          });
        } else {
          console.error("Policy type not found in list");
          alert("Policy product not found. Taking you back.");
          this.router.navigate(['/admin/all-policies']);
        }
        this.loading.set(false);
      },
      error: (err) => {
        console.error("Failed to load policy types", err);
        const msg = err.error?.message || "Failed to load policy product. Taking you back.";
        alert(msg);
        this.router.navigate(['/admin/all-policies']);
        this.loading.set(false);
      }
    });
  }

  submit() {
    if (this.form.invalid || this.submitting()) {
      this.form.markAllAsTouched();
      return;
    }

    this.submitting.set(true);
    const data = this.form.value;

    if (this.isEditMode() && this.policyTypeId()) {
      this.policyTypeService.updatePolicyType(this.policyTypeId()!, data).subscribe({
        next: () => {
          this.router.navigate(['/admin/all-policies']);
        },
        error: (err) => {
          console.error("Failed to update policy product", err);
          const msg = err.error?.message || "Error saving configuration.";
          alert(msg);
          this.submitting.set(false);
        }
      });
    } else {
      this.policyTypeService.createPolicyType(data).subscribe({
        next: () => {
          this.router.navigate(['/admin/all-policies']);
        },
        error: (err) => {
          console.error("Failed to create policy product", err);
          const msg = err.error?.message || "Error creating configuration.";
          alert(msg);
          this.submitting.set(false);
        }
      });
    }
  }

}
