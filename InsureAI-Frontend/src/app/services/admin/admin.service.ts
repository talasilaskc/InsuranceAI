import { HttpClient } from '@angular/common/http';
import { inject, Injectable, signal } from '@angular/core';

@Injectable({
  providedIn: 'root',
})
export class AdminService {
  public title = signal('');
  private http= inject(HttpClient);

  getPendingPolicies(){
    return this.http.get<any[]>("http://localhost:8080/api/admin/policies/pending");
  }

  approvePolicy(id:number){
    return this.http.put(`http://localhost:8080/api/admin/policies/${id}/approve`,{});
  }

  rejectPolicy(id:number){
    return this.http.put(`http://localhost:8080/api/admin/policies/${id}/reject`,{});
  }

  getPolicyById(id:number){
    return this.http.get<any>(`http://localhost:8080/api/admin/policies/${id}`);
  }

  getDashboard(){
    return this.http.get("http://localhost:8080/api/admin/dashboard");
  } 

  getAllPolicies(){
  return this.http.get<any[]>("http://localhost:8080/api/admin/policies");
  }

  createUnderwriter(payload: any) {
    return this.http.post(
      'http://localhost:8080/api/admin/create-underwriter',
      payload,
      { responseType: 'text' }
    );
  }

  createClaimsOfficer(payload: any) {
    return this.http.post(
      'http://localhost:8080/api/admin/create-claims-officer',
      payload,
      { responseType: 'text' }
    );
  }

  getAllUnderwriters() {
    return this.http.get<any[]>('http://localhost:8080/api/admin/underwriters');
  }

  getAllClaimsOfficers() {
    return this.http.get<any[]>('http://localhost:8080/api/admin/claims/officers');
  }

  assignUnderwriter(policyId: number, underwriterId: number) {
    return this.http.put(`http://localhost:8080/api/admin/assign-underwriter/${policyId}/${underwriterId}`, {}, { responseType: 'text' as 'json' });
  }

  overridePremium(policyId: number, newPremium: number) {
    return this.http.put(`http://localhost:8080/api/admin/policies/${policyId}/override-premium`, {
      premiumAmount: newPremium
    });
  }
}
