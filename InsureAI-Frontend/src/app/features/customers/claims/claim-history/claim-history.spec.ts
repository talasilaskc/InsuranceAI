import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ClaimHistory } from './claim-history';
import { AdminService } from '../../../../services/admin/admin.service';
import { ClaimService } from '../../../../services/claim/claim.service';
import { of } from 'rxjs';

describe('ClaimHistory Component', () => {
  let component: ClaimHistory;
  let fixture: ComponentFixture<ClaimHistory>;
  let adminServiceSpy: any;
  let claimServiceSpy: any;

  beforeEach(async () => {
    adminServiceSpy = { title: { set: jasmine.createSpy() } };
    claimServiceSpy = { getCompanyClaims: jasmine.createSpy() };

    await TestBed.configureTestingModule({
      imports: [ClaimHistory],
      providers: [
        { provide: AdminService, useValue: adminServiceSpy },
        { provide: ClaimService, useValue: claimServiceSpy }
      ]
    }).compileComponents();

    claimServiceSpy.getCompanyClaims.and.returnValue(of([{ claimId: 1, claimAmount: 1000, status: 'APPROVED', payoutAmount: 800 }]));

    fixture = TestBed.createComponent(ClaimHistory);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should load claims and compute totals', () => {
    expect(claimServiceSpy.getCompanyClaims).toHaveBeenCalled();
    expect(component.claims().length).toBe(1);
    expect(component.totalClaimed()).toBe(1000);
    expect(component.approvedAmount()).toBe(800);
  });
});


