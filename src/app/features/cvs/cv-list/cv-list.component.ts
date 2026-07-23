import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CvService } from '../../../core/services/cv.service';
import { CvResponse, SourceCandidat } from '../../../core/models/cv.model';
import { RouterLink } from '@angular/router';
@Component({
  selector: 'app-cv-list',
  standalone: true,
  imports: [FormsModule, RouterLink],
  templateUrl: './cv-list.component.html',
  styleUrl: './cv-list.component.css'
})
export class CvListComponent implements OnInit {
  cvs: CvResponse[] = [];
  isLoading = true;
  isUploading = false;
  errorMessage = '';
  successMessage = '';

  selectedFile: File | null = null;
  selectedSource: SourceCandidat = 'LINKEDIN';
  commentaire = '';
  isDragOver = false;

  sources: SourceCandidat[] = ['LINKEDIN', 'CABINET', 'COOPTATION', 'CANDIDATURE_SPONTANEE', 'JOBBOARD', 'AUTRE'];

  constructor(private cvService: CvService) {}

  ngOnInit(): void {
    this.loadVivier();
  }

  loadVivier(): void {
    this.isLoading = true;
    this.cvService.getVivierGeneral().subscribe({
      next: (data) => {
        this.cvs = data;
        this.isLoading = false;
      },
      error: () => {
        this.errorMessage = 'Impossible de charger le vivier.';
        this.isLoading = false;
      }
    });
  }

  onFileSelected(event: Event): void {
    const input = event.target as HTMLInputElement;
    if (input.files && input.files.length > 0) {
      this.selectedFile = input.files[0];
    }
  }

  onDragOver(event: DragEvent): void {
    event.preventDefault();
    this.isDragOver = true;
  }

  onDragLeave(): void {
    this.isDragOver = false;
  }

  onDrop(event: DragEvent): void {
    event.preventDefault();
    this.isDragOver = false;
    if (event.dataTransfer?.files && event.dataTransfer.files.length > 0) {
      this.selectedFile = event.dataTransfer.files[0];
    }
  }

  uploadCv(): void {
    if (!this.selectedFile) {
      this.errorMessage = 'Veuillez sélectionner un fichier.';
      return;
    }

    this.isUploading = true;
    this.errorMessage = '';
    this.successMessage = '';

    this.cvService.upload(this.selectedFile, this.selectedSource, undefined, this.commentaire || undefined).subscribe({
      next: (cv) => {
        this.isUploading = false;
        this.selectedFile = null;
        this.commentaire = '';
        if (cv.doublonPotentiel) {
          this.successMessage = `CV ajouté, mais un doublon potentiel a été détecté pour ${cv.emailCandidat}.`;
        } else {
          this.successMessage = 'CV ajouté au vivier avec succès.';
        }
        this.loadVivier();
      },
      error: (err) => {
        this.isUploading = false;
        this.errorMessage = err.error || 'Erreur lors de l\'upload du CV.';
      }
    });
  }

  removeSelectedFile(): void {
    this.selectedFile = null;
  }
}