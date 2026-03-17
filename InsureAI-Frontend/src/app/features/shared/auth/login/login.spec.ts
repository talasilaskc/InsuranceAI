import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Login } from './login';
import { AuthService } from '../../../../services/auth/auth.service';
import { Router } from '@angular/router';
import { of, throwError } from 'rxjs';
import { provideRouter } from '@angular/router';

describe('Login Component', () => {
  let component: Login;
  let fixture: ComponentFixture<Login>;
  let authServiceMock: any;
  let router: Router;

  beforeEach(async () => {
    authServiceMock = {
      login: jasmine.createSpy(),
      getUserRole: jasmine.createSpy()
    };

    await TestBed.configureTestingModule({
      imports: [Login],
      providers: [
        { provide: AuthService, useValue: authServiceMock },
        provideRouter([])
      ]
    }).compileComponents();

    router = TestBed.inject(Router);
    spyOn(router, 'navigate');

    fixture = TestBed.createComponent(Login);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should navigate to admin dashboard on successful admin login', () => {
    const mockResponse = { token: 'admin-token' };
    authServiceMock.login.and.returnValue(of(mockResponse));
    authServiceMock.getUserRole.and.returnValue('ROLE_ADMIN');

    component.email.set('admin@test.com');
    component.password.set('password');
    component.login();

    expect(authServiceMock.login).toHaveBeenCalled();
    expect(router.navigate).toHaveBeenCalledWith(['/admin/dashboard']);
  });

  it('should navigate to company dashboard on successful user login', () => {
    const mockResponse = { token: 'user-token' };
    authServiceMock.login.and.returnValue(of(mockResponse));
    authServiceMock.getUserRole.and.returnValue('ROLE_USER');

    component.email.set('user@test.com');
    component.password.set('password');
    component.login();

    expect(authServiceMock.login).toHaveBeenCalled();
    expect(router.navigate).toHaveBeenCalledWith(['/company/dashboard']);
  });

  it('should alert on login failure', () => {
    const alertSpy = spyOn(window, 'alert').and.callFake(() => {});
    authServiceMock.login.and.returnValue(throwError(() => ({ error: { message: 'Invalid credentials' } })));

    component.login();

    expect(alertSpy).toHaveBeenCalledWith('Invalid credentials'); });
});


