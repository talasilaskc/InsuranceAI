import { Component, inject } from '@angular/core';
import { Sidebar } from "../../components/sidebar/sidebar";
import { RouterOutlet } from "@angular/router";
import { Navbar } from '../../components/navbar/navbar';
import { AdminService } from '../../../../services/admin/admin.service';

@Component({
  selector: 'app-admin-layout',
  standalone: true,
  imports: [Sidebar, RouterOutlet, Navbar],
  templateUrl: './admin-layout.html',
  styleUrl: './admin-layout.css',
})
export class AdminLayout {
  public adminService: AdminService = inject(AdminService);

}