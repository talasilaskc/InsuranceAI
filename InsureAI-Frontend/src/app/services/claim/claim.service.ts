import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Claim, ClaimDocument } from '../../models/claim.model';
import { Observable } from 'rxjs';
import { ToastService } from '../toast/toast.service';

@Injectable({
  providedIn: 'root',
})
export class ClaimService {
  private http = inject(HttpClient);
  private toastService = inject(ToastService);
  private baseApi = "http://localhost:8080/api/claims";
  private adminApi = "http://localhost:8080/api/admin/claims";
  private officerApi = "http://localhost:8080/api/officer/claims";

  // -----------------------------
  // Customer Actions
  // -----------------------------
  submitClaim(data: any): Observable<Claim> {
    return this.http.post<Claim>(`${this.baseApi}/submit`, data);
  }

  getCompanyClaims(): Observable<Claim[]> {
    return this.http.get<Claim[]>(`${this.baseApi}/company`);
  }

  getClaim(id: number): Observable<Claim> {
    return this.http.get<Claim>(`${this.baseApi}/${id}`);
  }

  getClaimsForPolicy(policyId: number): Observable<Claim[]> {
    return this.http.get<Claim[]>(`${this.baseApi}/policy/${policyId}`);
  }

  // -----------------------------
  // Admin Actions
  // -----------------------------
  getAllClaims(): Observable<Claim[]> {
    return this.http.get<Claim[]>(`${this.baseApi}/admin/all`);
  }

  getOfficers(): Observable<any[]> {
    return this.http.get<any[]>(`${this.adminApi}/officers`);
  }

  markUnderReview(claimId: number) {
    return this.http.put(`${this.adminApi}/${claimId}/mark-under-review`, {});
  }

  assignOfficer(claimId: number, officerId: number) {
    return this.http.put(`${this.adminApi}/${claimId}/assign/${officerId}`, {});
  }

  finalApprove(claimId: number, data: { payoutAmount: number, remarks: string }) {
    return this.http.put(`${this.adminApi}/${claimId}/final-approve`, data);
  }

  finalReject(claimId: number, data: { remarks: string }) {
    return this.http.put(`${this.adminApi}/${claimId}/final-reject`, data);
  }

  markSettled(claimId: number) {
    return this.http.put(`${this.adminApi}/${claimId}/mark-settled`, {});
  }

  // -----------------------------
  // Officer Actions
  // -----------------------------
  getAssignedClaims(): Observable<Claim[]> {
    return this.http.get<Claim[]>(`${this.officerApi}/assigned`);
  }

  startInvestigation(claimId: number) {
    return this.http.put(`${this.officerApi}/${claimId}/start-investigation`, {});
  }

  submitRecommendation(claimId: number, data: { recommendedPayoutAmount: number, officerRemarks: string }) {
    return this.http.put(`${this.officerApi}/${claimId}/recommend`, data);
  }

  // -----------------------------
  // Document Actions
  // -----------------------------
  uploadDocument(claimId: number, file: File): Observable<ClaimDocument> {
    const formData = new FormData();
    formData.append('file', file);
    return this.http.post<ClaimDocument>(`${this.baseApi}/${claimId}/documents/upload`, formData);
  }

  getDocuments(claimId: number): Observable<ClaimDocument[]> {
    return this.http.get<ClaimDocument[]>(`${this.baseApi}/${claimId}/documents`);
  }

  downloadDocument(docId: number) {
    this.http.get(`${this.baseApi}/documents/${docId}/download`, {
      responseType: 'blob',
      observe: 'response'
    }).subscribe({
      next: (response) => {
        const contentDisposition = response.headers.get('content-disposition');
        let fileName = 'document';
        if (contentDisposition) {
          const fileNameMatch = contentDisposition.match(/filename="(.+)"/);
          if (fileNameMatch && fileNameMatch.length > 1) {
            fileName = fileNameMatch[1];
          }
        }
        
        const blob = response.body as Blob;
        const url = window.URL.createObjectURL(blob);
        const a = document.createElement('a');
        a.href = url;
        a.download = fileName;
        document.body.appendChild(a);
        a.click();
        window.URL.revokeObjectURL(url);
        a.remove();
      },
      error: (err) => {
        console.error("Download failed", err);
        this.toastService.error("Failed to download document. Please check your permissions.");
      }
    });
  }
}