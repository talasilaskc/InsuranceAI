import { Component, inject, signal, AfterViewInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { AdminService } from '../../../services/admin/admin.service';

declare const lucide: any;

@Component({
  selector: 'app-create-underwriter',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './create-underwriter.html',
  styleUrl: './create-underwriter.css'
})
export class CreateUnderwriterComponent implements AfterViewInit {
  private fb = inject(FormBuilder);
  private adminService = inject(AdminService);

  submitting = signal<boolean>(false);

  form = this.fb.group({
    fullName: ['', [Validators.required]],
    email: ['', [Validators.required, Validators.email]],
    password: ['', [Validators.required, Validators.minLength(6)]]
  });

  constructor() {
    this.adminService.title.set('Create Underwriter');
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
    console.log(payload);

    this.adminService.createUnderwriter(payload).subscribe({
      next: () => {
        alert('Underwriter created successfully');
        this.form.reset();
        this.submitting.set(false);
      },
      error: (err) => {
        console.error('Failed to create underwriter', err);
        const msg = err.error?.message || 'Error creating underwriter user.';
        alert(msg);
        this.submitting.set(false);
      }
    });
  }
}
