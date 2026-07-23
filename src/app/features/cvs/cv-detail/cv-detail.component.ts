import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { CvService } from '../../../core/services/cv.service';
import { CvResponse } from '../../../core/models/cv.model';
import { ApplicationService } from '../../../core/services/application.service';

@Component({
  selector: 'app-cv-detail',
  standalone: true,
  imports: [RouterLink],
  templateUrl: './cv-detail.component.html',
  styleUrl: './cv-detail.component.css'
})
export class CvDetailComponent implements OnInit {
  cv: CvResponse | null = null;
  isLoading = true;
  errorMessage = '';
  isDownloading = false;

  constructor(
    private cvService: CvService,
    private route: ActivatedRoute,
    private router: Router,
    private applicationService: ApplicationService
  ) {}

  ngOnInit(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    if (id) {
      this.loadCv(id);
    }
  }

  loadCv(id: number): void {
    this.isLoading = true;
    this.cvService.getById(id).subscribe({
      next: (data) => {
        this.cv = data;
        this.isLoading = false;
      },
      error: () => {
        this.errorMessage = 'Impossible de charger ce CV.';
        this.isLoading = false;
      }
    });
  }

  downloadCv(): void {
    if (!this.cv) return;

    this.isDownloading = true;
    this.cvService.download(this.cv.id).subscribe({
      next: (blob) => {
        const url = window.URL.createObjectURL(blob);
        const a = document.createElement('a');
        a.href = url;
        a.download = this.cv!.nomFichierOriginal;
        a.click();
        window.URL.revokeObjectURL(url);
        this.isDownloading = false;
      },
      error: () => {
        this.isDownloading = false;
        alert('Erreur lors du téléchargement.');
      }
    });
  }
  associerADemande(demandeId: number): void {
  if (!this.cv) return;
  this.applicationService.create({ cvId: this.cv.id, demandeId }).subscribe({
    next: () => this.router.navigate(['/pipeline', demandeId]),
    error: () => alert('Erreur lors de l\'association.')
  });
}
}