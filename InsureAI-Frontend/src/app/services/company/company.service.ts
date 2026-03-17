import { HttpClient } from '@angular/common/http';
import { inject, Injectable, signal } from '@angular/core';

@Injectable({
  providedIn: 'root',
})
export class CompanyService {
  http = inject(HttpClient);
  public title = signal('');

  getDashboard(){
  return this.http.get("http://localhost:8080/api/company/dashboard");
}
}
