import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';

@Injectable({
  providedIn: 'root',
})
export class AiSystemService {
  http = inject(HttpClient);

  // Endpoint URL - check backend configuration (singular/plural)
  // Standard Spring Boot controllers usually pluralize, but based on your recent route changes, using singular 'ai-system'
  private api = 'http://localhost:8080/api/ai-system';

  getSystems() {
    return this.http.get<any[]>(this.api);
  } 

  createSystem(data: any) {
    return this.http.post(this.api, data);
  }

  getSystemById(id: number) {
    return this.http.get<any>(`${this.api}/${id}`);
  }
}
