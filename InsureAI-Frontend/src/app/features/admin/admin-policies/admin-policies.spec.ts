import { ComponentFixture, TestBed } from '@angular/core/testing';
import { AdminPolicies } from './admin-policies';
import { AdminService } from '../../../services/admin/admin.service';
import { of, throwError } from 'rxjs';
import { provideRouter } from '@angular/router';

describe('AdminPolicies Component', () => {
  let component: AdminPolicies;
  let fixture: ComponentFixture<AdminPolicies>;
  let adminServiceSpy: any;

  beforeEach(async () => {
    adminServiceSpy = {
      getAllPolicies: jasmine.createSpy(),
      title: { set: jasmine.createSpy() }
    };

    await TestBed.configureTestingModule({
      imports: [AdminPolicies],
      providers: [
        { provide: AdminService, useValue: adminServiceSpy },
        provideRouter([])
      ]
    }).compileComponents();

    adminServiceSpy.getAllPolicies.and.returnValue(of([{ id: 1, name: 'Policy 1' }]));

    fixture = TestBed.createComponent(AdminPolicies);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should load policies on init', () => {
    expect(adminServiceSpy.getAllPolicies).toHaveBeenCalled();
    expect(component.policies.length).toBe(1);
    expect(component.loading()).toBeFalsy();
  });

  it('should handle error on init', () => {
    const alertSpy = spyOn(window, 'alert').and.callFake(() => {});
    adminServiceSpy.getAllPolicies.and.returnValue(throwError(() => ({ error: { message: 'Error' } })));
    component.ngOnInit();
    expect(alertSpy).toHaveBeenCalledWith('Error');
    expect(component.loading()).toBeFalsy();
    expect(component.error()).toBeTruthy(); });
});


