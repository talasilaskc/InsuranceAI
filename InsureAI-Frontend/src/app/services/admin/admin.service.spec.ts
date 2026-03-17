import { TestBed } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting, HttpTestingController } from '@angular/common/http/testing';
import { AdminService } from './admin.service';

describe('AdminService', () => {
  let service: AdminService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        AdminService,
        provideHttpClient(),
        provideHttpClientTesting()
      ]
    });
    service = TestBed.inject(AdminService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should fetch pending policies', () => {
    const mockPolicies = [{ id: 1, name: 'Policy 1' }];
    
    service.getPendingPolicies().subscribe(policies => {
      expect(policies.length).toBe(1);
      expect(policies).toEqual(mockPolicies);
    });

    const req = httpMock.expectOne('http://localhost:8080/api/admin/policies/pending');
    expect(req.request.method).toBe('GET');
    req.flush(mockPolicies);
  });
});


