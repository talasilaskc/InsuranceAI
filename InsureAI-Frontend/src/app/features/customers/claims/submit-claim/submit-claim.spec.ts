import { ComponentFixture, TestBed } from '@angular/core/testing';
import { SubmitClaim } from './submit-claim';
import { ClaimService } from '../../../../services/claim/claim.service';
import { PolicyService } from '../../../../services/policy/policy.service';
import { AdminService } from '../../../../services/admin/admin.service';
import { Router, ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';
import { provideRouter } from '@angular/router';
import { signal } from '@angular/core';

describe('SubmitClaim Component', () => {
  let component: SubmitClaim;
  let fixture: ComponentFixture<SubmitClaim>;
  let claimServiceMock: any;
  let policyServiceMock: any;
  let adminServiceMock: any;
  let router: Router;

  beforeEach(async () => {
    claimServiceMock = { submitClaim: jasmine.createSpy() };
    policyServiceMock = { getMyPolicies: jasmine.createSpy() };
    adminServiceMock = { 
      title: signal('') 
    };

    await TestBed.configureTestingModule({
      imports: [SubmitClaim],
      providers: [
        { provide: ClaimService, useValue: claimServiceMock },
        { provide: PolicyService, useValue: policyServiceMock },
        { provide: AdminService, useValue: adminServiceMock },
        provideRouter([]),
        {
          provide: ActivatedRoute,
          useValue: { 
            snapshot: { 
              queryParamMap: { get: () => '1' } 
            },
            queryParamMap: of({ get: () => '1' })
          }
        }
      ]
    }).compileComponents();

    router = TestBed.inject(Router);
    spyOn(router, 'navigate');

    policyServiceMock.getMyPolicies.and.returnValue(of([{ id: 1, name: 'P1' }]));

    fixture = TestBed.createComponent(SubmitClaim);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should load policies and pre-select on init', () => {
    expect(policyServiceMock.getMyPolicies).toHaveBeenCalled();
    expect(component.policies().length).toBe(1);
    expect(component.form.value.policyId).toBe('1');
  });

  it('should submit claim successfully', () => {
    const alertSpy = spyOn(window, 'alert').and.callFake(() => {});
    claimServiceMock.submitClaim.and.returnValue(of({}));
    component.form.patchValue({
      policyId: '1',
      claimAmount: '500',
      claimDescription: 'Test claim description longer than 10'
    });
    
    component.submit();
    
    expect(claimServiceMock.submitClaim).toHaveBeenCalled();
    expect(alertSpy).toHaveBeenCalled();
    expect(router.navigate).toHaveBeenCalledWith(['/company/claim-history']); });
});


