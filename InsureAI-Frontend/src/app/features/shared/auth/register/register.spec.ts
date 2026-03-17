import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Register } from './register';
import { ReactiveFormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../../../services/auth/auth.service';
import { of } from 'rxjs';
import { provideRouter } from '@angular/router';

describe('Register Component', () => {
  let component: Register;
  let fixture: ComponentFixture<Register>;
  let authServiceMock: any;
  let router: Router;

  beforeEach(async () => {
    authServiceMock = {
      register: jasmine.createSpy()
    };

    await TestBed.configureTestingModule({
      imports: [Register, ReactiveFormsModule],
      providers: [
        { provide: AuthService, useValue: authServiceMock },
        provideRouter([])
      ]
    }).compileComponents();

    router = TestBed.inject(Router);
    spyOn(router, 'navigate');

    fixture = TestBed.createComponent(Register);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should register successfully and navigate to login', () => {
    const alertSpy = spyOn(window, 'alert').and.callFake(() => {});
    authServiceMock.register.and.returnValue(of({}));

    component.registerForm.patchValue({
      fullName: 'John Doe',
      companyName: 'Test Company',
      industry: 'Tech',
      country: 'USA',
      companySize: 'Small',
      annualRevenue: '100000',
      email: 'test@comp.com',
      password: 'password123',
      confirmPassword: 'password123',
      termsAccepted: true
    });

    component.register();

    expect(authServiceMock.register).toHaveBeenCalled();
    expect(alertSpy).toHaveBeenCalledWith('Registration successful!');
    expect(router.navigate).toHaveBeenCalledWith(['/login']); });

  it('should not call register if form is invalid', () => {
    component.registerForm.patchValue({ email: 'invalid-email' });
    component.register();
    expect(authServiceMock.register).not.toHaveBeenCalled();
  });
});


