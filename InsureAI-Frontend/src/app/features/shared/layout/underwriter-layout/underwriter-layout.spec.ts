import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UnderwriterLayout } from './underwriter-layout';

describe('UnderwriterLayout', () => {
  let component: UnderwriterLayout;
  let fixture: ComponentFixture<UnderwriterLayout>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [UnderwriterLayout]
    })
    .compileComponents();

    fixture = TestBed.createComponent(UnderwriterLayout);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
