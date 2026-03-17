import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root',
})
export class PolicyService {
  http = inject(HttpClient);
  private baseApi = "http://localhost:8080/api/policies";

  getPolicyTypes() {
    return this.http.get<any[]>("http://localhost:8080/api/policy-types");
  }

  issuePolicy(data: any) {
    return this.http.post(`${this.baseApi}/issue`, data);
  }

  getMyPolicies() {
    return this.http.get<any[]>(`${this.baseApi}/company`);
  }

  payPolicy(policyId: number) {
    return this.http.put(`${this.baseApi}/${policyId}/pay`, {});
  }

  cancelPayment(policyId: number) {
    return this.http.put(`${this.baseApi}/${policyId}/cancel-payment`, {});
  }
}
