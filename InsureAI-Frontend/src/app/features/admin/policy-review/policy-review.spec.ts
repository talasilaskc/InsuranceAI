import { ComponentFixture, TestBed } from '@angular/core/testing';
import { PolicyReview } from './policy-review';
import { AdminService } from '../../../services/admin/admin.service';
import { ActivatedRoute, Router } from '@angular/router';
import { of } from 'rxjs';
import { provideRouter } from '@angular/router';
import { signal } from '@angular/core';

describe('PolicyReview Component', () => {
  let component: PolicyReview;
  let fixture: ComponentFixture<PolicyReview>;
  let adminServiceMock: any;
  let router: Router;

  beforeEach(async () => {
    adminServiceMock = {
      getPolicyById: jasmine.createSpy(),
      approvePolicy: jasmine.createSpy(),
      rejectPolicy: jasmine.createSpy(),
      title: signal('')
    };

    await TestBed.configureTestingModule({
      imports: [PolicyReview],
      providers: [
        { provide: AdminService, useValue: adminServiceMock },
        provideRouter([]),
        {
          provide: ActivatedRoute,
          useValue: { 
            snapshot: { 
              params: { id: '1' },
              paramMap: { get: () => '1' } 
            } 
          }
        }
      ]
    }).compileComponents();

    router = TestBed.inject(Router);
    spyOn(router, 'navigate');

    adminServiceMock.getPolicyById.and.returnValue(of({ id: 1, name: 'Test Policy' }));

    fixture = TestBed.createComponent(PolicyReview);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should load policy details on init', () => {
    expect(adminServiceMock.getPolicyById).toHaveBeenCalledWith('1');
    expect(component.policy().id).toBe(1);
    expect(component.loading()).toBeFalsy();
  });

  it('should approve policy and navigate', () => {
    spyOn(window, 'confirm').and.returnValue(true);
    adminServiceMock.approvePolicy.and.returnValue(of({}));
    
    component.approvePolicy();
    
    expect(adminServiceMock.approvePolicy).toHaveBeenCalledWith(1);
    expect(router.navigate).toHaveBeenCalledWith(['/admin/pending-policies']);
  });
});


