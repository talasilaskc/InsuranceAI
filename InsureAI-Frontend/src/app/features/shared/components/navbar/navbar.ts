import { Component, AfterViewInit, OnInit, inject, signal, input } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../../../../services/auth/auth.service';

declare const lucide: any;

@Component({
  selector: 'app-navbar',
  standalone: true,
  templateUrl: './navbar.html',
  styleUrl: './navbar.css'
})

export class Navbar implements AfterViewInit, OnInit {

  title = input<string>('');
  private authService = inject(AuthService);
  private router = inject(Router);
  public email = signal("");
  public role = signal("");

  ngOnInit() {
    this.email.set(this.authService.getUserEmail() || "");
    this.role.set(this.authService.getUserRole() || "");
  }

  logout() {
    this.authService.logout();
    this.router.navigate(['/login']);
  }

  ngAfterViewInit() {
    if (typeof lucide !== 'undefined') {
      lucide.createIcons();
    }
  }

}