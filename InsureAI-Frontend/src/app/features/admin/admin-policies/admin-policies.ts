import { Component, inject, OnInit, signal, AfterViewInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { AdminService } from '../../../services/admin/admin.service';
import { ToastService } from '../../../services/toast/toast.service';

declare const lucide: any;

@Component({
  selector: 'app-admin-policies',
  standalone: true,
  imports:[CommonModule, RouterLink],
  templateUrl: './admin-policies.html',
  styleUrl: './admin-policies.css'
})
export class AdminPolicies implements OnInit, AfterViewInit {

  adminService = inject(AdminService);
  toastService = inject(ToastService);

  policies:any[] = [];
  loading = signal(true);
  error = signal(false);

  ngOnInit(): void {

    this.adminService.title.set("Policy Portfolio");

    this.adminService.getAllPolicies().subscribe({
      next:(data:any[])=>{
        this.policies = data;
        this.loading.set(false);
        setTimeout(() => {
          if(typeof lucide !== 'undefined') {
            lucide.createIcons();
          }
        });
      },
      error:(err)=>{
        console.error(err);
        const msg = err.error?.message || "Failed to load policy portfolio.";
        this.toastService.error(msg);
        this.loading.set(false);
        this.error.set(true);
      }
    });

  }

  ngAfterViewInit(): void {
    if(typeof lucide !== 'undefined') {
      lucide.createIcons();
    }
  }

}