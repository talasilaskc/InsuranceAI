import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RiskAssessment } from './risk-assessment';

describe('RiskAssessment', () => {
  let component: RiskAssessment;
  let fixture: ComponentFixture<RiskAssessment>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [RiskAssessment]
    })
    .compileComponents();

    fixture = TestBed.createComponent(RiskAssessment);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
