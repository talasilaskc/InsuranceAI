import { ComponentFixture, TestBed } from '@angular/core/testing';
import { MyPolicies } from './my-policies';
import { PolicyService } from '../../../../services/policy/policy.service';
import { CompanyService } from '../../../../services/company/company.service';
import { of } from 'rxjs';
import { provideRouter } from '@angular/router';

describe('MyPolicies Component', () => {
  let component: MyPolicies;
  let fixture: ComponentFixture<MyPolicies>;
  let policyServiceSpy: any;
  let companyServiceSpy: any;

  beforeEach(async () => {
    policyServiceSpy = { getMyPolicies: jasmine.createSpy() };
    companyServiceSpy = { title: { set: jasmine.createSpy() } };

    await TestBed.configureTestingModule({
      imports: [MyPolicies],
      providers: [
        { provide: PolicyService, useValue: policyServiceSpy },
        { provide: CompanyService, useValue: companyServiceSpy },
        provideRouter([])
      ]
    }).compileComponents();

    policyServiceSpy.getMyPolicies.and.returnValue(of([{ policyId: 1, status: 'ACTIVE', aiSystemId: 101 }]));

    fixture = TestBed.createComponent(MyPolicies);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should load policies on init', () => {
    expect(policyServiceSpy.getMyPolicies).toHaveBeenCalled();
    expect(component.policies().length).toBe(1);
    expect(component.loading()).toBeFalsy();
  });

  it('should filter policies by search term', () => {
    component.searchTerm.set('ACTIVE');
    expect(component.filteredPolicies().length).toBe(1);
    
    component.searchTerm.set('NONEXISTENT');
    expect(component.filteredPolicies().length).toBe(0);
  });
});


