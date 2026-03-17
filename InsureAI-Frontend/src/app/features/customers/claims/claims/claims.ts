import { Component, inject, OnInit } from '@angular/core';
import { AdminService } from '../../../../services/admin/admin.service';

@Component({
  selector: 'app-claims',
  imports: [],
  templateUrl: './claims.html',
  styleUrl: './claims.css',
})
export class Claims implements OnInit {
  adminService:AdminService=inject(AdminService);

  ngOnInit(): void {
      this.adminService.title.set('All Claims');
      console.log('All Claims initialized, title set to:', this.adminService.title());
  }  
}
