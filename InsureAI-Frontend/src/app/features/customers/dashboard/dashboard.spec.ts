import { ComponentFixture, TestBed } from '@angular/core/testing';
import { CompanyDashboard } from './dashboard';
import { CompanyService } from '../../../services/company/company.service';
import { of, throwError } from 'rxjs';
import { provideRouter } from '@angular/router';

describe('CompanyDashboard Component', () => {
  let component: CompanyDashboard;
  let fixture: ComponentFixture<CompanyDashboard>;
  let companyServiceSpy: any;

  beforeEach(async () => {
    companyServiceSpy = {
      getDashboard: jasmine.createSpy(),
      title: { set: jasmine.createSpy() }
    };

    await TestBed.configureTestingModule({
      imports: [CompanyDashboard],
      providers: [
        { provide: CompanyService, useValue: companyServiceSpy },
        provideRouter([])
      ]
    }).compileComponents();

    companyServiceSpy.getDashboard.and.returnValue(of({ activePolicies: 5 }));

    fixture = TestBed.createComponent(CompanyDashboard);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should load dashboard data on init', () => {
    expect(companyServiceSpy.getDashboard).toHaveBeenCalled();
    expect(component.dashboard.activePolicies).toBe(5);
    expect(component.loading()).toBeFalsy();
  });

  it('should handle error on init', () => {
    const alertSpy = spyOn(window, 'alert').and.callFake(() => {});
    companyServiceSpy.getDashboard.and.returnValue(throwError(() => ({ error: { message: 'Error' } })));
    component.ngOnInit();
    expect(alertSpy).toHaveBeenCalledWith('Error');
    expect(component.loading()).toBeFalsy();
    expect(component.error()).toBeTruthy(); });
});


