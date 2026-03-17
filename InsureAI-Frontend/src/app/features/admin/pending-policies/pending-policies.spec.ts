import { ComponentFixture, TestBed } from '@angular/core/testing';
import { PendingPolicies } from './pending-policies';
import { AdminService } from '../../../services/admin/admin.service';
import { of } from 'rxjs';
import { provideRouter } from '@angular/router';

describe('PendingPolicies Component', () => {
  let component: PendingPolicies;
  let fixture: ComponentFixture<PendingPolicies>;
  let adminServiceSpy: any;

  beforeEach(async () => {
    adminServiceSpy = {
      getPendingPolicies: jasmine.createSpy(),
      approvePolicy: jasmine.createSpy(),
      rejectPolicy: jasmine.createSpy(),
      title: { set: jasmine.createSpy() }
    };

    await TestBed.configureTestingModule({
      imports: [PendingPolicies],
      providers: [
        { provide: AdminService, useValue: adminServiceSpy },
        provideRouter([])
      ]
    }).compileComponents();

    adminServiceSpy.getPendingPolicies.and.returnValue(of([{ id: 1, riskSnapshot: { riskLevel: 'HIGH' } }]));

    fixture = TestBed.createComponent(PendingPolicies);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should load pending policies on init', () => {
    expect(adminServiceSpy.getPendingPolicies).toHaveBeenCalled();
    expect(component.pendingPolicies().length).toBe(1);
    expect(component.highRiskCount()).toBe(1);
  });

  it('should approve policy', () => {
    spyOn(window, 'confirm').and.returnValue(true);
    adminServiceSpy.approvePolicy.and.returnValue(of({}));
    
    component.approve(1);
    
    expect(adminServiceSpy.approvePolicy).toHaveBeenCalledWith(1);
    expect(adminServiceSpy.getPendingPolicies).toHaveBeenCalledTimes(2);
  });
});


