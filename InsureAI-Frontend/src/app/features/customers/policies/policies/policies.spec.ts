import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Policies } from './policies';
import { PolicyService } from '../../../../services/policy/policy.service';
import { CompanyService } from '../../../../services/company/company.service';
import { Router } from '@angular/router';
import { of } from 'rxjs';
import { provideRouter } from '@angular/router';
import { signal } from '@angular/core';

describe('Policies Component', () => {
  let component: Policies;
  let fixture: ComponentFixture<Policies>;
  let policyServiceMock: any;
  let companyServiceMock: any;
  let router: Router;

  beforeEach(async () => {
    policyServiceMock = { 
      getPolicyTypes: jasmine.createSpy() 
    };
    companyServiceMock = { 
      title: signal(''),
      getRiskScore: jasmine.createSpy().and.returnValue(of(null))
    };

    await TestBed.configureTestingModule({
      imports: [Policies],
      providers: [
        { provide: PolicyService, useValue: policyServiceMock },
        { provide: CompanyService, useValue: companyServiceMock },
        provideRouter([])
      ]
    }).compileComponents();

    router = TestBed.inject(Router);
    spyOn(router, 'navigate');

    policyServiceMock.getPolicyTypes.and.returnValue(of([{ 
      id: 1, 
      name: 'Policy 1', 
      minCoverage: 1000, 
      maxCoverage: 5000, 
      basePrice: 500,
      description: 'Test description'
    }]));

    fixture = TestBed.createComponent(Policies);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should load policies on init', () => {
    expect(policyServiceMock.getPolicyTypes).toHaveBeenCalled();
    expect(component.policies().length).toBe(1);
    expect(component.loading()).toBeFalsy();
  });

  it('should navigate to apply policy', () => {
    component.applyPolicy(1);
    expect(router.navigate).toHaveBeenCalledWith(['/company/apply-policy', 1], { queryParams: {} });
  });
});


