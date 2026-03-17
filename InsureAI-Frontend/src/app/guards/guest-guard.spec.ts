import { TestBed } from '@angular/core/testing';
import { Router } from '@angular/router';
import { guestGuard } from './guest-guard';
import { AuthService } from '../services/auth/auth.service';

describe('guestGuard', () => {
  let authServiceSpy: any;
  let routerSpy: any;

  beforeEach(() => {
    authServiceSpy = { isloggedIn: jasmine.createSpy(), getUserRole: jasmine.createSpy() };
    routerSpy = { parseUrl: jasmine.createSpy() };

    TestBed.configureTestingModule({
      providers: [
        { provide: AuthService, useValue: authServiceSpy },
        { provide: Router, useValue: routerSpy }
      ]
    });
  });

  it('should allow activation if not logged in', () => {
    authServiceSpy.isloggedIn.and.returnValue(false);
    const result = TestBed.runInInjectionContext(() => guestGuard({} as any, {} as any));
    expect(result).toBeTruthy();
  });

  it('should redirect to admin dashboard if logged in as admin', () => {
    authServiceSpy.isloggedIn.and.returnValue(true);
    authServiceSpy.getUserRole.and.returnValue('ROLE_ADMIN');
    routerSpy.parseUrl.and.returnValue('/admin/dashboard' as any);
    
    const result = TestBed.runInInjectionContext(() => guestGuard({} as any, {} as any));
    expect(result as any).toBe('/admin/dashboard');
  });

  it('should redirect to company dashboard if logged in as company', () => {
    authServiceSpy.isloggedIn.and.returnValue(true);
    authServiceSpy.getUserRole.and.returnValue('ROLE_COMPANY');
    routerSpy.parseUrl.and.returnValue('/company/dashboard' as any);
    
    const result = TestBed.runInInjectionContext(() => guestGuard({} as any, {} as any));
    expect(result as any).toBe('/company/dashboard');
  });
});


