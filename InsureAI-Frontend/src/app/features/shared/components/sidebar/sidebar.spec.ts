import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Sidebar } from './sidebar';
import { provideRouter, Router } from '@angular/router';

describe('Sidebar Component', () => {
  let component: Sidebar;
  let fixture: ComponentFixture<Sidebar>;
  let router: Router;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [Sidebar],
      providers: [
        provideRouter([])
      ]
    }).compileComponents();

    router = TestBed.inject(Router);
    spyOn(router, 'navigate');

    fixture = TestBed.createComponent(Sidebar);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should compute company links by default', () => {
    expect(component.links()).toEqual(component.companyLinks);
  });

  it('should compute admin links when role is admin', async () => {
    fixture.componentRef.setInput('role', 'admin');
    fixture.detectChanges();
    expect(component.links()).toEqual(component.adminLinks);
  });

  it('should logout and navigate to login', () => {
    const clearSpy = spyOn(Storage.prototype, 'clear').and.callFake(() => {});
    component.logout();
    expect(clearSpy).toHaveBeenCalled();
    expect(router.navigate).toHaveBeenCalledWith(['/login']); });
});


