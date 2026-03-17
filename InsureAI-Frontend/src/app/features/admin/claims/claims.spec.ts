import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Claims } from './claims';
import { ClaimService } from '../../../services/claim/claim.service';
import { AdminService } from '../../../services/admin/admin.service';
import { of } from 'rxjs';

describe('Admin Claims Component', () => {
  let component: Claims;
  let fixture: ComponentFixture<Claims>;
  let claimServiceSpy: any;
  let adminServiceSpy: any;

  beforeEach(async () => {
    claimServiceSpy = {
      getAllClaims: jasmine.createSpy(),
      approveClaim: jasmine.createSpy(),
      rejectClaim: jasmine.createSpy()
    };
    adminServiceSpy = { title: { set: jasmine.createSpy() } };

    await TestBed.configureTestingModule({
      imports: [Claims],
      providers: [
        { provide: ClaimService, useValue: claimServiceSpy },
        { provide: AdminService, useValue: adminServiceSpy }
      ]
    }).compileComponents();

    claimServiceSpy.getAllClaims.and.returnValue(of([{ claimId: 1, status: 'SUBMITTED' }]));

    fixture = TestBed.createComponent(Claims);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should load claims on init', () => {
    expect(claimServiceSpy.getAllClaims).toHaveBeenCalled();
    expect(component.claims().length).toBe(1);
    expect(component.totalClaims()).toBe(1);
  });

  it('should filter claims by status', () => {
    component.statusFilter.set('APPROVED');
    expect(component.filteredClaims().length).toBe(0);
    
    component.statusFilter.set('SUBMITTED');
    expect(component.filteredClaims().length).toBe(1);
  });

  it('should approve claim', () => {
    claimServiceSpy.approveClaim.and.returnValue(of({}));
    component.verifiedLossInputs[1] = 1000;
    
    component.approveClaim({ claimId: 1 });
    
    expect(claimServiceSpy.approveClaim).toHaveBeenCalledWith(1, 1000);
    expect(component.claims()[0].status).toBe('APPROVED');
  });
});


