import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  http = inject(HttpClient);
  private api = 'http://localhost:8080/api/auth';

  login(credentials: any) {
    return this.http.post(`${this.api}/login`, credentials);
  }

  logout() {
    localStorage.removeItem('token');
  }

  getToken() {
    return localStorage.getItem('token');
  }

  isloggedIn() {
    return !!this.getToken();
  }

  getUserRole() {
    const token = this.getToken();
    if (!token) return null;
    try {
      const payload = token.split('.')[1];
      const decoded = JSON.parse(atob(payload));
      return decoded.role;
    } catch (e) {
      return null;
    }
  }

  getUserEmail() {
    const token = this.getToken();
    if (!token) return null;
    try {
      const payload = token.split('.')[1];
      const decoded = JSON.parse(atob(payload));
      return decoded.sub || decoded.email;
    } catch (e) {
      return null;
    }
  }

  register(data: any) {
    return this.http.post(`${this.api}/register`, data);
  }

  

}
