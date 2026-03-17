import { Component, inject, Input, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ClaimService } from '../../../../services/claim/claim.service';
import { ClaimDocument } from '../../../../models/claim.model';
import { FormsModule } from '@angular/forms';
import { ToastService } from '../../../../services/toast/toast.service';

@Component({
  selector: 'app-claim-documents',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './claim-documents.html'
})
export class ClaimDocumentsComponent implements OnInit {
  @Input() claimId!: number;
  @Input() allowUpload: boolean = true;

  private claimService = inject(ClaimService);
  private toastService = inject(ToastService);
  
  documents = signal<ClaimDocument[]>([]);
  loading = signal<boolean>(true);
  uploading = signal<boolean>(false);
  selectedFile: File | null = null;

  ngOnInit(): void {
    if (this.claimId) {
      this.loadDocuments();
    }
  }

  loadDocuments() {
    this.loading.set(true);
    this.claimService.getDocuments(this.claimId).subscribe({
      next: (res: ClaimDocument[]) => {
        this.documents.set(res || []);
        this.loading.set(false);
      },
      error: (err: any) => {
        console.error("Failed to load documents", err);
        this.loading.set(false);
      }
    });
  }

  onFileSelected(event: any) {
    this.selectedFile = event.target.files[0];
  }

  upload() {
    if (!this.selectedFile) return;

    this.uploading.set(true);
    this.claimService.uploadDocument(this.claimId, this.selectedFile).subscribe({
      next: () => {
        this.uploading.set(false);
        this.selectedFile = null;
        this.loadDocuments();
      },
      error: (err: any) => {
        const msg = err.error?.message || "Upload failed";
        this.toastService.error(msg);
        this.uploading.set(false);
      }
    });
  }

  download(docId: number) {
    this.claimService.downloadDocument(docId);
  }
}
