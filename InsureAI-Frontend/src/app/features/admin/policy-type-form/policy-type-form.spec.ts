import { ComponentFixture, TestBed } from '@angular/core/testing';
import { PolicyTypeForm } from './policy-type-form';
import { AdminService } from '../../../services/admin/admin.service';
import { PolicyTypeService } from '../../../services/policy-type/policy-type.service';
import { ActivatedRoute, Router } from '@angular/router';
import { of } from 'rxjs';
import { provideRouter } from '@angular/router';
import { provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { signal } from '@angular/core';

describe('PolicyTypeForm Component', () => {
  let component: PolicyTypeForm;
  let fixture: ComponentFixture<PolicyTypeForm>;
  let adminServiceMock: any;
  let policyTypeServiceMock: any;
  let router: Router;

  beforeEach(async () => {
    adminServiceMock = { 
      title: signal('') 
    };
    policyTypeServiceMock = {
      getPolicyTypes: jasmine.createSpy(),
      createPolicyType: jasmine.createSpy(),
      updatePolicyType: jasmine.createSpy()
    };

    await TestBed.configureTestingModule({
      imports: [PolicyTypeForm],
      providers: [
        { provide: AdminService, useValue: adminServiceMock },
        { provide: PolicyTypeService, useValue: policyTypeServiceMock },
        {
          provide: ActivatedRoute,
          useValue: { snapshot: { paramMap: { get: () => null } } }
        },
        provideRouter([]),
        provideHttpClient(),
        provideHttpClientTesting()
      ]
    }).compileComponents();

    router = TestBed.inject(Router);
    spyOn(router, 'navigate');

    fixture = TestBed.createComponent(PolicyTypeForm);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create in create mode', () => {
    expect(component).toBeTruthy();
    expect(component.isEditMode()).toBeFalsy();
  });

  it('should create policy type on submit', () => {
    policyTypeServiceMock.createPolicyType.and.returnValue(of({}));
    component.form.patchValue({
      name: 'New',
      description: 'Desc',
      basePrice: 100,
      minCoverage: 50,
      maxCoverage: 200
    });
    
    component.submit();
    
    expect(policyTypeServiceMock.createPolicyType).toHaveBeenCalled();
    expect(router.navigate).toHaveBeenCalledWith(['/admin/all-policies']);
  });
});


