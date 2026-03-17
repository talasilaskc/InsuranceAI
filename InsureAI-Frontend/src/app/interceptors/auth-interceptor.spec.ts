import { TestBed } from '@angular/core/testing';
import { HttpRequest, HttpHandlerFn, HttpEvent } from '@angular/common/http';
import { authInterceptor } from './auth-interceptor';
import { Router } from '@angular/router';
import { of, throwError } from 'rxjs';

describe('authInterceptor', () => {
  let routerSpy: any;

  beforeEach(() => {
    routerSpy = { navigate: jasmine.createSpy() };
    TestBed.configureTestingModule({
      providers: [
        { provide: Router, useValue: routerSpy }
      ]
    });
    localStorage.clear();
  });

  it('should add Authorization header if token exists', () => {
    localStorage.setItem('token', 'fake-token');
    const req = new HttpRequest('GET', '/api/data');
    const next: HttpHandlerFn = (request: HttpRequest<any>) => {
      expect(request.headers.get('Authorization')).toBe('Bearer fake-token');
      return of({} as HttpEvent<any>);
    };

    TestBed.runInInjectionContext(() => authInterceptor(req, next));
  });

  it('should redirect to login on 401 error', () => {
    localStorage.setItem('token', 'fake-token');
    const req = new HttpRequest('GET', '/api/data');
    const next: HttpHandlerFn = () => throwError(() => ({ status: 401 }));

    TestBed.runInInjectionContext(() => authInterceptor(req, next).subscribe({
      error: () => {
        expect(localStorage.getItem('token')).toBeNull();
        expect(routerSpy.navigate).toHaveBeenCalledWith(['/login']);
      }
    }));
  });
});


