import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UnderwriterDashboard } from './underwriter-dashboard';

describe('UnderwriterDashboard', () => {
  let component: UnderwriterDashboard;
  let fixture: ComponentFixture<UnderwriterDashboard>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [UnderwriterDashboard]
    })
    .compileComponents();

    fixture = TestBed.createComponent(UnderwriterDashboard);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
