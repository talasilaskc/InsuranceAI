import { ComponentFixture, TestBed } from '@angular/core/testing';
import { AdminLayout } from './admin-layout';
import { provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { provideRouter } from '@angular/router';
import { AuthService } from '../../../../services/auth/auth.service';

describe('AdminLayout Component', () => {
  let component: AdminLayout;
  let fixture: ComponentFixture<AdminLayout>;
  let authServiceMock: any;

  beforeEach(async () => {
    authServiceMock = {
      getUserEmail: jasmine.createSpy().and.returnValue('admin@test.com'),
      getUserRole: jasmine.createSpy().and.returnValue('ROLE_ADMIN'),
      logout: jasmine.createSpy()
    };

    await TestBed.configureTestingModule({
      imports: [AdminLayout],
      providers: [
        { provide: AuthService, useValue: authServiceMock },
        provideHttpClient(),
        provideHttpClientTesting(),
        provideRouter([])
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(AdminLayout);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});


