import { Component, inject, AfterViewInit } from '@angular/core';
import { ReactiveFormsModule, FormBuilder, Validators, AbstractControl } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../../../../services/auth/auth.service';
import { CommonModule } from '@angular/common';
import { ToastService } from '../../../../services/toast/toast.service';

declare const lucide: any;

@Component({
  selector: 'app-register',
  imports: [ReactiveFormsModule, RouterLink, CommonModule],
  templateUrl: './register.html',
  styleUrl: './register.css',
})
export class Register implements AfterViewInit {
  private fb = inject(FormBuilder);
  private router = inject(Router);
  private authService = inject(AuthService);
  private toastService = inject(ToastService);

  ngAfterViewInit() {
    if (typeof lucide !== 'undefined') {
      lucide.createIcons();
    }
  }

  get f() {
    return this.registerForm.controls;
  }

  registerForm = this.fb.group({
    fullName: ['', Validators.required],
    companyName: ['', Validators.required],
    industry: ['', Validators.required],
    country: ['', Validators.required],

    companySize: ['', Validators.required],
    annualRevenue: ['', Validators.required],

    email: ['', [Validators.required, Validators.email]],
    password: ['', [Validators.required, Validators.minLength(6)]],
    confirmPassword: ['', Validators.required],

    termsAccepted: [false, Validators.requiredTrue]

  }, {
    validators: this.passwordMatchValidator
  });

  register() {
    if (this.registerForm.invalid) {
      return;
    }

    const formData = this.registerForm.value;

    const payload = {
      fullName: formData.fullName,
      companyName: formData.companyName,
      industry: formData.industry,
      country: formData.country,
      companySize: formData.companySize,
      annualRevenue: Number(formData.annualRevenue),
      email: formData.email,
      password: formData.password
    };

    this.authService.register(payload).subscribe({
      next: () => {
        this.toastService.success('Registration successful!');
        this.router.navigate(['/login']);
      },
      error: (err) => {
        const msg = err.error?.message || 'Registration failed. Please try again.';
        this.toastService.error(msg);
        console.error('Registration error:', err);
      }
    });
  }

  passwordMatchValidator(form: AbstractControl) {

    const password = form.get('password')?.value;
    const confirmPassword = form.get('confirmPassword')?.value;

    if (password !== confirmPassword) {
      form.get('confirmPassword')?.setErrors({ passwordMismatch: true });
      return { passwordMismatch: true };
    } else {
      return null;
    }

  }
}
