import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Navbar } from './navbar';
import { AuthService } from '../../../../services/auth/auth.service';
import { Router } from '@angular/router';
import { provideRouter } from '@angular/router';

describe('Navbar Component', () => {
  let component: Navbar;
  let fixture: ComponentFixture<Navbar>;
  let authServiceSpy: any;
  let routerSpy: any;

  beforeEach(async () => {
    authServiceSpy = {
      getUserEmail: jasmine.createSpy(),
      getUserRole: jasmine.createSpy(),
      logout: jasmine.createSpy()
    };
    routerSpy = {
      navigate: jasmine.createSpy()
    };

    await TestBed.configureTestingModule({
      imports: [Navbar],
      providers: [
        { provide: AuthService, useValue: authServiceSpy },
        { provide: Router, useValue: routerSpy },
        provideRouter([])
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(Navbar);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should set email and role on init', () => {
    authServiceSpy.getUserEmail.and.returnValue('test@test.com');
    authServiceSpy.getUserRole.and.returnValue('ROLE_ADMIN');
    
    component.ngOnInit();
    
    expect(component.email()).toBe('test@test.com');
    expect(component.role()).toBe('ROLE_ADMIN');
  });

  it('should logout and navigate to login', () => {
    component.logout();
    expect(authServiceSpy.logout).toHaveBeenCalled();
    expect(routerSpy.navigate).toHaveBeenCalledWith(['/login']);
  });
});


