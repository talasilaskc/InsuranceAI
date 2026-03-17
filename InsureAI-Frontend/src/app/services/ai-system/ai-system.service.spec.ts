import { TestBed } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting, HttpTestingController } from '@angular/common/http/testing';
import { AiSystemService } from './ai-system.service';

describe('AiSystemService', () => {
  let service: AiSystemService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        AiSystemService,
        provideHttpClient(),
        provideHttpClientTesting()
      ]
    });
    service = TestBed.inject(AiSystemService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should fetch systems', () => {
    const mockSystems = [{ id: 1, name: 'AI 1' }];
    
    service.getSystems().subscribe(systems => {
      expect(systems.length).toBe(1);
      expect(systems).toEqual(mockSystems);
    });

    const req = httpMock.expectOne('http://localhost:8080/api/ai-system');
    expect(req.request.method).toBe('GET');
    req.flush(mockSystems);
  });
});


