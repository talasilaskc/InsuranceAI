import { ComponentFixture, TestBed } from '@angular/core/testing';
import { RiskAssessment } from './risk-assessment';
import { AiSystemService } from '../../../../services/ai-system/ai-system.service';
import { RiskService } from '../../../../services/risk/risk.service';
import { CompanyService } from '../../../../services/company/company.service';
import { of } from 'rxjs';
import { provideRouter } from '@angular/router';
import { signal } from '@angular/core';

describe('RiskAssessment Component', () => {
  let component: RiskAssessment;
  let fixture: ComponentFixture<RiskAssessment>;
  let aiServiceMock: any;
  let riskServiceMock: any;
  let companyServiceMock: any;

  beforeEach(async () => {
    aiServiceMock = { getSystems: jasmine.createSpy() };
    riskServiceMock = { createRisk: jasmine.createSpy() };
    companyServiceMock = { 
      title: signal('') 
    };

    await TestBed.configureTestingModule({
      imports: [RiskAssessment],
      providers: [
        { provide: AiSystemService, useValue: aiServiceMock },
        { provide: RiskService, useValue: riskServiceMock },
        { provide: CompanyService, useValue: companyServiceMock },
        provideRouter([])
      ]
    }).compileComponents();

    aiServiceMock.getSystems.and.returnValue(of([{ id: 1, name: 'S1' }]));

    fixture = TestBed.createComponent(RiskAssessment);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should load systems on init', () => {
    expect(aiServiceMock.getSystems).toHaveBeenCalled();
    expect(component.systems().length).toBe(1);
  });

  it('should submit risk assessment', () => {
    riskServiceMock.createRisk.and.returnValue(of({ riskScore: 75 }));
    component.riskForm.patchValue({
      aiSystemId: '1',
      dataExposureCategory: 'Public',
      financialImpactLevel: 'High'
    });
    
    component.submit();
    
    expect(riskServiceMock.createRisk).toHaveBeenCalled();
    expect(component.riskResult()).toEqual({ riskScore: 75 });
  });
});


