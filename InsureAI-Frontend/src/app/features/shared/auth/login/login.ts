import { Component, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { AuthService } from '../../../../services/auth/auth.service';
import { Router, RouterLink } from '@angular/router';
import { CommonModule } from '@angular/common';
import { ToastService } from '../../../../services/toast/toast.service';

@Component({
  selector: 'app-login',
  imports: [FormsModule, RouterLink, CommonModule],
  templateUrl: './login.html',
  styleUrl: './login.css',
})
export class Login {
  email = signal<string>('');
  password = signal<string>('');

  authService = inject(AuthService);
  toastService = inject(ToastService);
  router = inject(Router);

  login() {

    const credentials = {
      email: this.email(),
      password: this.password()
    }

    this.authService.login(credentials).subscribe({

      next: (response: any) => {

        const token = response.token;

        localStorage.setItem("token", token);

        const role = this.authService.getUserRole();

        if (role === "ROLE_ADMIN") {
          this.router.navigate(['/admin/dashboard']);
        }
        else if (role === "ROLE_UNDERWRITER") {
          this.router.navigate(['/underwriter/dashboard']);
        }
        else if (role === "ROLE_CLAIMS_OFFICER") {
          this.router.navigate(['/officer/dashboard']);
        }
        else {
          this.router.navigate(['/company/dashboard']);
        }

      },

      error: (err) => {
        console.log("Login failed", err);
        const msg = err?.error?.message || "Login Failed";
        this.toastService.error(msg);
      }

    })

  }

}
