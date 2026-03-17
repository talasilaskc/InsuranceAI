import { TestBed } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting, HttpTestingController } from '@angular/common/http/testing';
import { RiskService } from './risk.service';

describe('RiskService', () => {
  let service: RiskService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        RiskService,
        provideHttpClient(),
        provideHttpClientTesting()
      ]
    });
    service = TestBed.inject(RiskService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should create risk assessment', () => {
    const data = { score: 80 };
    service.createRisk(data).subscribe();
    const req = httpMock.expectOne('http://localhost:8080/api/risk');
    expect(req.request.method).toBe('POST');
    req.flush({});
  });
});


