import { Routes } from '@angular/router';

import { authGuard } from './guards/auth-guard';
import { roleGuard } from './guards/role-guard';

import { guestGuard } from './guards/guest-guard';

export const routes: Routes = [
  {
    path: '',
    loadComponent: () => import('./features/shared/home/home').then(m => m.Home)
  },
  {
    path: 'login',
    loadComponent: () => import('./features/shared/auth/login/login').then(m => m.Login),
    canActivate: [guestGuard]
  },
  {
    path: 'register',
    loadComponent: () => import('./features/shared/auth/register/register').then(m => m.Register),
    canActivate: [guestGuard]
  },
  {
    path: 'company',
    loadComponent: () => import('./features/shared/layout/company-layout/company-layout').then(m => m.CompanyLayout),
    canActivate: [authGuard, roleGuard],
    data: { role: 'ROLE_COMPANY' },
    children: [
      {
        path: 'dashboard',
        loadComponent: () => import('./features/customers/dashboard/dashboard').then(m => m.CompanyDashboard)
      },
      {
        path: 'ai-system',
        loadComponent: () => import('./features/customers/ai-systems/ai-systems/ai-systems').then(m => m.AiSystems)
      },
      {
        path: 'register-ai-system',
        loadComponent: () => import('./features/customers/ai-systems/register-ai-system/register-ai-system').then(m => m.RegisterAiSystem)
      },
      {
        path: 'policy-types',
        loadComponent: () => import('./features/customers/policies/policies/policies').then(m => m.Policies)
      },

      {
        path: 'my-policies',
        loadComponent: () => import('./features/customers/policies/my-policies/my-policies').then(m => m.MyPolicies)
      },
      {
        path: 'submit-claim',
        loadComponent: () => import('./features/customers/claims/submit-claim/submit-claim').then(m => m.SubmitClaim)
      },
      {
        path: 'claim-history',
        loadComponent: () => import('./features/customers/claims/claim-history/claim-history').then(m => m.ClaimHistory)
      },
      {
        path: 'claim-detail/:id',
        loadComponent: () => import('./features/customers/claims/claim-detail/claim-detail').then(m => m.ClaimDetailComponent)
      },
      {
        path: 'apply-policy/:policyId',
        loadComponent: () => import('./features/customers/policies/apply-policy/apply-policy').then(m => m.ApplyPolicy)
      }
    ]
  },
  {
    path: 'admin',
    loadComponent: () => import('./features/shared/layout/admin-layout/admin-layout').then(m => m.AdminLayout),
    canActivate: [authGuard, roleGuard],
    data: { role: 'ROLE_ADMIN' },
    children: [
      {
        path: 'dashboard',
        loadComponent: () => import('./features/admin/dashboard/dashboard').then(m => m.AdminDashboard)
      },
      {
        path: 'pending-policies',
        loadComponent: () => import('./features/admin/pending-policies/pending-policies').then(m => m.PendingPolicies)
      },
      {
        path: 'all-policies',
        loadComponent: () => import('./features/admin/policy-management/policy-management').then(m => m.PolicyManagement)
      },
      {
        path: 'admin-policies',
        loadComponent: () => import('./features/admin/admin-policies/admin-policies').then(m => m.AdminPolicies)
      },
      {
        path: 'all-claims',
        loadComponent: () => import('./features/admin/claims/claims').then(m => m.Claims)
      },
      {
        path: 'claim-review/:id',
        loadComponent: () => import('./features/admin/claim-review/claim-review').then(m => m.ClaimReview)
      },
      {
        path: 'policy-review/:id',
        loadComponent: () => import('./features/admin/policy-review/policy-review').then(m => m.PolicyReview)
      },
      {
        path: 'policy-types/create',
        loadComponent: () => import('./features/admin/policy-type-form/policy-type-form').then(m => m.PolicyTypeForm)
      },
      {
        path: 'policy-types/edit/:id',
        loadComponent: () => import('./features/admin/policy-type-form/policy-type-form').then(m => m.PolicyTypeForm)
      },
      {
        path: 'create-underwriter',
        loadComponent: () => import('./features/admin/create-underwriter/create-underwriter').then(m => m.CreateUnderwriterComponent)
      },
      {
        path: 'create-claims-officer',
        loadComponent: () => import('./features/admin/create-claims-officer/create-claims-officer').then(m => m.CreateClaimsOfficerComponent)
      }
    ]
  },
  {
    path: 'underwriter',
    loadComponent: () => import('./features/shared/layout/underwriter-layout/underwriter-layout').then(m => m.UnderwriterLayout),
    canActivate: [authGuard, roleGuard],
    data: { role: 'ROLE_UNDERWRITER' },
    children: [
      { path: '', redirectTo: 'dashboard', pathMatch: 'full' },
      {
        path: 'dashboard',
        loadComponent: () => import('./features/underwriter/underwriter-dashboard/underwriter-dashboard').then(m => m.UnderwriterDashboard)
      },
      {
        path: 'policies',
        loadComponent: () => import('./features/underwriter/assigned-policies/assigned-policies').then(m => m.AssignedPolicies)
      },
      {
        path: 'policy/:id/risk',
        loadComponent: () => import('./features/underwriter/risk-assessment/risk-assessment').then(m => m.RiskAssessment)
      }
    ]
  },
  {
    path: 'officer',
    loadComponent: () => import('./features/shared/layout/officer-layout/officer-layout').then(m => m.OfficerLayout),
    canActivate: [authGuard, roleGuard],
    data: { role: 'ROLE_CLAIMS_OFFICER' },
    children: [
      { path: '', redirectTo: 'dashboard', pathMatch: 'full' },
      {
        path: 'dashboard',
        loadComponent: () => import('./features/officer/dashboard/dashboard').then(m => m.OfficerDashboard)
      },
      {
        path: 'investigate/:id',
        loadComponent: () => import('./features/officer/investigation/investigation').then(m => m.OfficerInvestigation)
      }
    ]
  }
];
