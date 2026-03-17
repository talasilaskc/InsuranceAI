import { TestBed } from '@angular/core/testing';
import { Router } from '@angular/router';
import { authGuard } from './auth-guard';
import { AuthService } from '../services/auth/auth.service';

describe('authGuard', () => {
  let authServiceSpy: any;
  let routerSpy: any;

  beforeEach(() => {
    authServiceSpy = { isloggedIn: jasmine.createSpy() };
    routerSpy = { parseUrl: jasmine.createSpy() };

    TestBed.configureTestingModule({
      providers: [
        { provide: AuthService, useValue: authServiceSpy },
        { provide: Router, useValue: routerSpy }
      ]
    });
  });

  it('should allow activation if logged in', () => {
    authServiceSpy.isloggedIn.and.returnValue(true);
    const result = TestBed.runInInjectionContext(() => authGuard({} as any, {} as any));
    expect(result).toBeTruthy();
  });

  it('should redirect to login if not logged in', () => {
    authServiceSpy.isloggedIn.and.returnValue(false);
    routerSpy.parseUrl.and.returnValue('/login' as any);
    const result = TestBed.runInInjectionContext(() => authGuard({} as any, {} as any));
    expect(result as any).toBe('/login');
  });
});


