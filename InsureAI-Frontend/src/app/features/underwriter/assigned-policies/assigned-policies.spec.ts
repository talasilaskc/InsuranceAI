import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AssignedPolicies } from './assigned-policies';

describe('AssignedPolicies', () => {
  let component: AssignedPolicies;
  let fixture: ComponentFixture<AssignedPolicies>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [AssignedPolicies]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AssignedPolicies);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
