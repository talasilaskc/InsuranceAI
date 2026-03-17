import { ComponentFixture, TestBed } from '@angular/core/testing';
import { AdminDashboard } from './dashboard';
import { AdminService } from '../../../services/admin/admin.service';
import { of, throwError } from 'rxjs';
import { provideRouter } from '@angular/router';
import { signal } from '@angular/core';

describe('AdminDashboard Component', () => {
  let component: AdminDashboard;
  let fixture: ComponentFixture<AdminDashboard>;
  let adminServiceMock: any;

  beforeEach(async () => {
    adminServiceMock = {
      getDashboard: jasmine.createSpy(),
      title: signal('')
    };

    await TestBed.configureTestingModule({
      imports: [AdminDashboard],
      providers: [
        { provide: AdminService, useValue: adminServiceMock },
        provideRouter([])
      ]
    }).compileComponents();

    adminServiceMock.getDashboard.and.returnValue(of({ totalPolicies: 10 }));

    fixture = TestBed.createComponent(AdminDashboard);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should load dashboard data on init', () => {
    expect(adminServiceMock.getDashboard).toHaveBeenCalled();
    expect(component.dashboard.totalPolicies).toBe(10);
    expect(component.loading()).toBeFalsy();
  });

  it('should handle error on init', () => {
    const alertSpy = spyOn(window, 'alert').and.callFake(() => {});
    adminServiceMock.getDashboard.and.returnValue(throwError(() => ({ error: { message: 'Error' } })));
    component.ngOnInit();
    expect(alertSpy).toHaveBeenCalledWith('Error');
    expect(component.loading()).toBeFalsy(); });
});


