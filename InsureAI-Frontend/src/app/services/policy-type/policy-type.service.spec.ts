import { TestBed } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting, HttpTestingController } from '@angular/common/http/testing';
import { PolicyTypeService } from './policy-type.service';

describe('PolicyTypeService', () => {
  let service: PolicyTypeService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        PolicyTypeService,
        provideHttpClient(),
        provideHttpClientTesting()
      ]
    });
    service = TestBed.inject(PolicyTypeService);
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
    const req = httpMock.expectOne('http://localhost:8080/api/admin/policy-types');
    expect(req.request.method).toBe('GET');
    req.flush([]);
  });

  it('should create policy type', () => {
    const data = { name: 'New Type' };
    service.createPolicyType(data).subscribe();
    const req = httpMock.expectOne('http://localhost:8080/api/admin/policy-types');
    expect(req.request.method).toBe('POST');
    req.flush({});
  });
});


