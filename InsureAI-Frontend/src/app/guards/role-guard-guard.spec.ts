import { TestBed } from '@angular/core/testing';
import { Router } from '@angular/router';
import { roleGuard } from './role-guard';
import { AuthService } from '../services/auth/auth.service';

describe('roleGuard', () => {
  let authServiceSpy: any;
  let routerSpy: any;

  beforeEach(() => {
    authServiceSpy = { getUserRole: jasmine.createSpy() };
    routerSpy = { parseUrl: jasmine.createSpy() };

    TestBed.configureTestingModule({
      providers: [
        { provide: AuthService, useValue: authServiceSpy },
        { provide: Router, useValue: routerSpy }
      ]
    });
  });

  it('should allow activation if user has correct role', () => {
    authServiceSpy.getUserRole.and.returnValue('ROLE_ADMIN');
    const route = { data: { role: 'ROLE_ADMIN' } } as any;
    const result = TestBed.runInInjectionContext(() => roleGuard(route, {} as any));
    expect(result).toBeTruthy();
  });

  it('should redirect if user has wrong role', () => {
    authServiceSpy.getUserRole.and.returnValue('ROLE_USER');
    routerSpy.parseUrl.and.returnValue('/login' as any);
    const route = { data: { role: 'ROLE_ADMIN' } } as any;
    const result = TestBed.runInInjectionContext(() => roleGuard(route, {} as any));
    expect(result as any).toBe('/login');
  });
});


