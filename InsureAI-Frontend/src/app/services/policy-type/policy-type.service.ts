import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root',
})
export class PolicyTypeService {
  http=inject(HttpClient);
  private baseUrl = "http://localhost:8080/api/admin/policy-types";

  getPolicyTypes(){
    return this.http.get<any[]>(this.baseUrl);
  }

  createPolicyType(data:any){
    return this.http.post(this.baseUrl, data);
  }

  updatePolicyType(id:number,data:any){
    return this.http.put(`${this.baseUrl}/${id}`, data);
  }

  deletePolicyType(id:number){
    return this.http.delete(`${this.baseUrl}/${id}`);
  }

  // getPoliciesByType(id:number){
  //   return this.http.get<any[]>(`${this.baseUrl}/${id}/policies`);
  // }

}
