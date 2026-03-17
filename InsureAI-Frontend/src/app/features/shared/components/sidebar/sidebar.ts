import { Component, AfterViewInit, input, computed, inject } from '@angular/core';
import { RouterLink, Router, RouterLinkActive } from '@angular/router';

declare const lucide: any;

@Component({
  selector: 'app-sidebar',
  standalone: true,
  imports: [RouterLink, RouterLinkActive],
  templateUrl: './sidebar.html',
  styleUrl: './sidebar.css'
})
export class Sidebar implements AfterViewInit {

  role = input<'company' | 'admin' | 'underwriter' | 'officer'>('company');

  companyLinks = [
    { name: 'Dashboard', path: '/company/dashboard', icon: 'layout-dashboard' },
    { name: 'AI Systems', path: '/company/ai-system', icon: 'cpu' },
    { name: 'Available Policies', path: '/company/policy-types', icon: 'shield' },
    { name: 'My Policies', path: '/company/my-policies', icon: 'file-text' },

    { name: 'Claim History', path: '/company/claim-history', icon: 'history' },
    { name: 'Initiate Claim', path: '/company/submit-claim', icon: 'alert-circle' }
  ];

  adminLinks = [
    { name: 'Dashboard', path: '/admin/dashboard', icon: 'layout-dashboard' },
    { name: 'Pending Reviews', path: '/admin/pending-policies', icon: 'clipboard-check' },
    { name: 'Insurance Products', path: '/admin/all-policies', icon: 'shield-plus' },
    { name: 'All Claims', path: '/admin/all-claims', icon: 'file-search' },
    { name: 'Policy Portfolio', path: '/admin/admin-policies', icon: 'file-text' },
    { name: 'Create Underwriter', path: '/admin/create-underwriter', icon: 'user-plus' },
    { name: 'Create Claims Officer', path: '/admin/create-claims-officer', icon: 'users' }
  ];

  underwriterLinks = [
    { name: 'Dashboard', path: '/underwriter/dashboard', icon: 'layout-dashboard' },
    { name: 'My Assignments', path: '/underwriter/policies', icon: 'clipboard-list' }
  ];

  officerLinks = [
    { name: 'Dashboard', path: '/officer/dashboard', icon: 'layout-dashboard' }
  ];

  links = computed(() => {
    if (this.role() === 'admin') return this.adminLinks;
    if (this.role() === 'underwriter') return this.underwriterLinks;
    if (this.role() === 'officer') return this.officerLinks;
    return this.companyLinks;
  });

  private router = inject(Router);

  ngAfterViewInit() {
    if (typeof lucide !== 'undefined') {
      lucide.createIcons();
    }
  }

  logout() {
    localStorage.clear();
    this.router.navigate(['/login']);
  }

}