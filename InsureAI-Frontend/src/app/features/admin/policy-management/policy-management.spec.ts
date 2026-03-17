import { ComponentFixture, TestBed } from '@angular/core/testing';
import { PolicyManagement } from './policy-management';
import { AdminService } from '../../../services/admin/admin.service';
import { PolicyTypeService } from '../../../services/policy-type/policy-type.service';
import { of } from 'rxjs';
import { provideRouter } from '@angular/router';

describe('PolicyManagement Component', () => {
  let component: PolicyManagement;
  let fixture: ComponentFixture<PolicyManagement>;
  let adminServiceSpy: any;
  let policyTypeServiceSpy: any;

  beforeEach(async () => {
    adminServiceSpy = { title: { set: jasmine.createSpy() } };
    policyTypeServiceSpy = {
      getPolicyTypes: jasmine.createSpy(),
      deletePolicyType: jasmine.createSpy()
    };

    await TestBed.configureTestingModule({
      imports: [PolicyManagement],
      providers: [
        { provide: AdminService, useValue: adminServiceSpy },
        { provide: PolicyTypeService, useValue: policyTypeServiceSpy },
        provideRouter([])
      ]
    }).compileComponents();

    policyTypeServiceSpy.getPolicyTypes.and.returnValue(of([{ id: 1, name: 'P1', active: true }]));

    fixture = TestBed.createComponent(PolicyManagement);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should load policy types on init', () => {
    expect(policyTypeServiceSpy.getPolicyTypes).toHaveBeenCalled();
    expect(component.policyTypes().length).toBe(1);
    expect(component.activeCount()).toBe(1);
  });

  it('should deactivate policy type', () => {
    spyOn(window, 'confirm').and.returnValue(true);
    policyTypeServiceSpy.deletePolicyType.and.returnValue(of({}));
    
    component.deactivate(1);
    
    expect(policyTypeServiceSpy.deletePolicyType).toHaveBeenCalledWith(1);
    expect(component.policyTypes()[0].active).toBeFalsy();
  });
});


