import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Claims } from './claims';
import { AdminService } from '../../../../services/admin/admin.service';
import { signal } from '@angular/core';

describe('Claims Component', () => {
  let component: Claims;
  let fixture: ComponentFixture<Claims>;
  let adminServiceMock: any;

  beforeEach(async () => {
    adminServiceMock = { 
      title: signal('') 
    };

    await TestBed.configureTestingModule({
      imports: [Claims],
      providers: [
        { provide: AdminService, useValue: adminServiceMock }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(Claims);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should set title on init', () => {
    expect(adminServiceMock.title()).toBe('All Claims');
  });
});


