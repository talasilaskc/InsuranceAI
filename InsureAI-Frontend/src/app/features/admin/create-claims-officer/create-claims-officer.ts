import { Component, inject, signal, AfterViewInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { AdminService } from '../../../services/admin/admin.service';

declare const lucide: any;

@Component({
  selector: 'app-create-claims-officer',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './create-claims-officer.html',
  styleUrl: './create-claims-officer.css'
})
export class CreateClaimsOfficerComponent implements AfterViewInit {
  private fb = inject(FormBuilder);
  private adminService = inject(AdminService);

  submitting = signal<boolean>(false);

  form = this.fb.group({
    fullName: ['', [Validators.required]],
    email: ['', [Validators.required, Validators.email]],
    password: ['', [Validators.required, Validators.minLength(6)]]
  });

  constructor() {
    this.adminService.title.set('Create Claims Officer');
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

  get f() {
    return this.form.controls;
  }

  onSubmit() {
    if (this.form.invalid || this.submitting()) {
      this.form.markAllAsTouched();
      return;
    }

    this.submitting.set(true);
    const payload = this.form.value;

    this.adminService.createClaimsOfficer(payload).subscribe({
      next: () => {
        alert('Claims Officer created successfully');
        this.form.reset();
        this.submitting.set(false);
      },
      error: (err) => {
        console.error('Failed to create claims officer', err);
        const msg = err.error?.message || 'Error creating claims officer user.';
        alert(msg);
        this.submitting.set(false);
      }
    });
  }
}
