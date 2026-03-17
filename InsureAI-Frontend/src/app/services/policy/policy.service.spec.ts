import { TestBed } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting, HttpTestingController } from '@angular/common/http/testing';
import { PolicyService } from './policy.service';

describe('PolicyService', () => {
  let service: PolicyService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        PolicyService,
        provideHttpClient(),
        provideHttpClientTesting()
      ]
    });
    service = TestBed.inject(PolicyService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should fetch policy types', () => {
    service.getPolicyTypes().subscribe();
    const req = httpMock.expectOne('http://localhost:8080/api/policy-types');
    expect(req.request.method).toBe('GET');
    req.flush([]);
  });

  it('should issue policy', () => {
    const data = { typeId: 1 };
    service.issuePolicy(data).subscribe();
    const req = httpMock.expectOne('http://localhost:8080/api/policies/issue');
    expect(req.request.method).toBe('POST');
    expect(req.request.body).toEqual(data);
    req.flush({});
  });
});


