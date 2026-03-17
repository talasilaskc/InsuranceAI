import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ApplyPolicy } from './apply-policy';
import { AiSystemService } from '../../../../services/ai-system/ai-system.service';
import { PolicyService } from '../../../../services/policy/policy.service';
import { CompanyService } from '../../../../services/company/company.service';
import { ActivatedRoute, Router } from '@angular/router';
import { of } from 'rxjs';
import { provideRouter } from '@angular/router';
import { signal } from '@angular/core';

describe('ApplyPolicy Component', () => {
  let component: ApplyPolicy;
  let fixture: ComponentFixture<ApplyPolicy>;
  let aiServiceMock: any;
  let policyServiceMock: any;
  let companyServiceMock: any;
  let router: Router;

  beforeEach(async () => {
    aiServiceMock = { getSystems: jasmine.createSpy() };
    policyServiceMock = { getPolicyTypes: jasmine.createSpy(), issuePolicy: jasmine.createSpy() };
    companyServiceMock = { 
      title: signal('') 
    };

    await TestBed.configureTestingModule({
      imports: [ApplyPolicy],
      providers: [
        { provide: AiSystemService, useValue: aiServiceMock },
        { provide: PolicyService, useValue: policyServiceMock },
        { provide: CompanyService, useValue: companyServiceMock },
        provideRouter([]),
        {
          provide: ActivatedRoute,
          useValue: {
            snapshot: { 
              paramMap: { get: () => '1' } 
            },
            queryParamMap: of({ get: () => '123' })
          }
        }
      ]
    }).compileComponents();

    router = TestBed.inject(Router);
    spyOn(router, 'navigate');

    aiServiceMock.getSystems.and.returnValue(of([{ id: 123, name: 'S1' }]));
    policyServiceMock.getPolicyTypes.and.returnValue(of([{ id: 1, name: 'P1', minCoverage: 10000 }]));

    fixture = TestBed.createComponent(ApplyPolicy);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should load data and pre-fill form on init', () => {
    expect(aiServiceMock.getSystems).toHaveBeenCalled();
    expect(policyServiceMock.getPolicyTypes).toHaveBeenCalled();
    expect(component.selectedPolicy().name).toBe('P1');
    expect(component.form.value.coverageLimit).toBe(10000);
    expect(component.form.value.aiSystemId).toBe('123');
  });

  it('should submit policy application', () => {
    const alertSpy = spyOn(window, 'alert').and.callFake(() => {});
    policyServiceMock.issuePolicy.and.returnValue(of({}));
    component.form.patchValue({ aiSystemId: '123' });
    
    component.submit();
    
    expect(policyServiceMock.issuePolicy).toHaveBeenCalled();
    expect(router.navigate).toHaveBeenCalledWith(['/company/my-policies']); });
});


