import { ComponentFixture, TestBed } from '@angular/core/testing';
import { RegisterAiSystem } from './register-ai-system';
import { AiSystemService } from '../../../../services/ai-system/ai-system.service';
import { CompanyService } from '../../../../services/company/company.service';
import { Router } from '@angular/router';
import { of } from 'rxjs';
import { provideRouter } from '@angular/router';

describe('RegisterAiSystem Component', () => {
  let component: RegisterAiSystem;
  let fixture: ComponentFixture<RegisterAiSystem>;
  let aiServiceSpy: any;
  let companyServiceSpy: any;
  let routerSpy: any;

  beforeEach(async () => {
    aiServiceSpy = { createSystem: jasmine.createSpy() };
    companyServiceSpy = { title: { set: jasmine.createSpy() } };
    routerSpy = { navigate: jasmine.createSpy() };

    await TestBed.configureTestingModule({
      imports: [RegisterAiSystem],
      providers: [
        { provide: AiSystemService, useValue: aiServiceSpy },
        { provide: CompanyService, useValue: companyServiceSpy },
        { provide: Router, useValue: routerSpy },
        provideRouter([])
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(RegisterAiSystem);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should register AI system on submit', () => {
    aiServiceSpy.createSystem.and.returnValue(of({}));
    component.aiForm.patchValue({
      name: 'New AI',
      ownershipType: 'In-house',
      deploymentType: 'Cloud',
      modelType: 'LLM',
      dataExposureCategory: 'Public'
    });
    
    component.submit();
    
    expect(aiServiceSpy.createSystem).toHaveBeenCalled();
    expect(routerSpy.navigate).toHaveBeenCalledWith(['/company/ai-system']);
  });
});


