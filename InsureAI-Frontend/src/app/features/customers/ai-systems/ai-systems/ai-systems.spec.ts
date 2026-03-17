import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { AiSystems } from './ai-systems';
import { AiSystemService } from '../../../../services/ai-system/ai-system.service';
import { CompanyService } from '../../../../services/company/company.service';
import { of } from 'rxjs';
import { provideRouter } from '@angular/router';
import { signal } from '@angular/core';

describe('AiSystems Component', () => {
  let component: AiSystems;
  let fixture: ComponentFixture<AiSystems>;
  let aiServiceMock: any;
  let companyServiceMock: any;

  beforeEach(async () => {
    aiServiceMock = { getSystems: jasmine.createSpy() };
    companyServiceMock = { 
      title: signal('') 
    };

    await TestBed.configureTestingModule({
      imports: [AiSystems],
      providers: [
        { provide: AiSystemService, useValue: aiServiceMock },
        { provide: CompanyService, useValue: companyServiceMock },
        provideRouter([])
      ]
    }).compileComponents();

    aiServiceMock.getSystems.and.returnValue(of([{ id: 1, name: 'S1', modelType: 'LLM' }]));

    fixture = TestBed.createComponent(AiSystems);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should load systems on init', () => {
    expect(aiServiceMock.getSystems).toHaveBeenCalled();
    expect(component.systems().length).toBe(1);
    expect(component.loading()).toBeFalsy();
  });

  it('should return correct icon for model type', () => {
    expect(component.getSystemIcon('LLM')).toBe('bot');
    expect(component.getSystemIcon('NLP')).toBe('message-square');
    expect(component.getSystemIcon('UNKNOWN')).toBe('cpu');
  });
});


