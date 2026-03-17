import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root',
})
export class RiskService {
  http = inject(HttpClient);
  private api= 'http://localhost:8080/api/risk';

  createRisk(data:any){
    return this.http.post(this.api,data);
  }
}
