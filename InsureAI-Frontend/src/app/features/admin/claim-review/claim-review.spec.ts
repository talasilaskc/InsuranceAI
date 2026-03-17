import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ClaimReview } from './claim-review';
import { ClaimService } from '../../../services/claim/claim.service';
import { AdminService } from '../../../services/admin/admin.service';
import { ActivatedRoute, Router } from '@angular/router';
import { of } from 'rxjs';
import { provideRouter } from '@angular/router';
import { signal } from '@angular/core';

describe('ClaimReview Component', () => {
  let component: ClaimReview;
  let fixture: ComponentFixture<ClaimReview>;
  let claimServiceMock: any;
  let adminServiceMock: any;
  let router: Router;

  beforeEach(async () => {
    claimServiceMock = {
      getAllClaims: jasmine.createSpy(),
      approveClaim: jasmine.createSpy(),
      rejectClaim: jasmine.createSpy()
    };
    adminServiceMock = { 
      title: signal('') 
    };

    await TestBed.configureTestingModule({
      imports: [ClaimReview],
      providers: [
        { provide: ClaimService, useValue: claimServiceMock },
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

    claimServiceMock.getAllClaims.and.returnValue(of([{ claimId: 1, verifiedLoss: 500 }]));

    fixture = TestBed.createComponent(ClaimReview);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should load claim on init', () => {
    expect(component.claim()).toEqual({ claimId: 1, verifiedLoss: 500 });
    expect(component.verifiedLoss()).toBe(500);
  });

  it('should navigate away if claim not found', () => {
    claimServiceMock.getAllClaims.and.returnValue(of([{ claimId: 2 }]));
    const alertSpy = spyOn(window, 'alert').and.callFake(() => {});
    component.ngOnInit();
    expect(alertSpy).toHaveBeenCalled();
    expect(router.navigate).toHaveBeenCalledWith(['/admin/all-claims']); });
});


