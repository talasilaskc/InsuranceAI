import { ComponentFixture, TestBed } from '@angular/core/testing';
import { CompanyLayout } from './company-layout';
import { provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { provideRouter } from '@angular/router';
import { AuthService } from '../../../../services/auth/auth.service';

describe('CompanyLayout Component', () => {
  let component: CompanyLayout;
  let fixture: ComponentFixture<CompanyLayout>;
  let authServiceMock: any;

  beforeEach(async () => {
    authServiceMock = {
      getUserEmail: jasmine.createSpy().and.returnValue('user@test.com'),
      getUserRole: jasmine.createSpy().and.returnValue('ROLE_COMPANY'),
      logout: jasmine.createSpy()
    };

    await TestBed.configureTestingModule({
      imports: [CompanyLayout],
      providers: [
        { provide: AuthService, useValue: authServiceMock },
        provideHttpClient(),
        provideHttpClientTesting(),
        provideRouter([])
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(CompanyLayout);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});


