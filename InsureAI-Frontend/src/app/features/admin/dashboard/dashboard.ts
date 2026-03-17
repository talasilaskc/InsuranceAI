import { Component, inject, OnInit, signal, AfterViewInit } from '@angular/core';
import { AdminService } from '../../../services/admin/admin.service';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';

declare const lucide: any;

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './dashboard.html',
  styleUrl: './dashboard.css',
})
export class AdminDashboard implements OnInit, AfterViewInit {

  adminService:AdminService=inject(AdminService);
  dashboard:any = null;
  loading=signal(true);

  ngOnInit(): void {
      this.adminService.title.set('Dashboard');
      console.log('Dashboard initialized, title set to:', this.adminService.title());
      this.adminService.getDashboard().subscribe({
      next:(data:any)=>{
        this.dashboard = data;
        this.loading.set(false);
      },
      error:(err)=>{
        console.error(err);
        const msg = err.error?.message || "Failed to load dashboard metrics.";
        alert(msg);
        this.loading.set(false);
      }
    });
  }

  ngAfterViewInit(): void {
    if (typeof lucide !== 'undefined') {
      lucide.createIcons();
    }
  }

}
