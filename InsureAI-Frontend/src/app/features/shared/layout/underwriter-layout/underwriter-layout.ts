import { Component, AfterViewInit, inject } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { Sidebar } from '../../components/sidebar/sidebar';
import { Navbar } from '../../components/navbar/navbar';
import { AdminService } from '../../../../services/admin/admin.service';

@Component({
  selector: 'app-underwriter-layout',
  standalone: true,
  imports: [RouterOutlet, Sidebar, Navbar],
  templateUrl: './underwriter-layout.html',
  styleUrl: './underwriter-layout.css',
})
export class UnderwriterLayout {
  public adminService = inject(AdminService);
}

