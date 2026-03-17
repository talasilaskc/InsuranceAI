import { Component, inject, OnInit, signal, AfterViewInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { CompanyService } from '../../../services/company/company.service';
import { ToastService } from '../../../services/toast/toast.service';

declare const lucide:any;

@Component({
  selector: 'app-company-dashboard',
  standalone: true,
  imports:[CommonModule, RouterLink],
  templateUrl: './dashboard.html',
  styleUrl: './dashboard.css'
})
export class CompanyDashboard implements OnInit, AfterViewInit {

  companyService = inject(CompanyService);
  toastService = inject(ToastService);

  dashboard:any = null;

  loading = signal(true);
  error = signal(false);   // ⭐ NEW

  ngOnInit(): void {
    this.companyService.title.set('Dashboard'); 
    this.companyService.getDashboard().subscribe({
      next:(data:any)=>{
        this.dashboard = data;
        this.loading.set(false);

        setTimeout(()=>{
          if(typeof lucide !== 'undefined'){
            lucide.createIcons();
          }
        });
      },
      error:(err)=>{
        console.error("Dashboard load failed", err);
        const msg = err.error?.message || "Dashboard load failed.";
        this.toastService.error(msg);
        this.loading.set(false);
        this.error.set(true);   // ⭐ NEW
      }
    });

  }

  ngAfterViewInit(): void {
    if(typeof lucide !== 'undefined'){
      lucide.createIcons();
    }
  }

}