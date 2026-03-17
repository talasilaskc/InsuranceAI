import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class UnderwriterService {
  private http = inject(HttpClient);
  private apiUrl = `http://localhost:8080/api/underwriter`;

  getMyPolicies(): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/my-policies`);
  }

  getPolicyById(id: number): Observable<any> {
  return this.http.get<any>(`http://localhost:8080/api/underwriter/policies/${id}`);
}

  getDashboardStats(): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/dashboard-stats`);
  }

  submitRisk(policyId: number, payload: any): Observable<any> {
    return this.http.post<any>(`${this.apiUrl}/policies/${policyId}/risk`, payload);
  }
}
