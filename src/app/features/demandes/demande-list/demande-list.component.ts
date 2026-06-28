import { Component, OnInit } from '@angular/core';
import { Router, RouterLink } from '@angular/router';
import { DemandeService } from '../../../core/services/demande.service';
import { DemandeResponse, StatutDemande } from '../../../core/models/demande.model';

@Component({
  selector: 'app-demande-list',
  standalone: true,
  imports: [RouterLink],
  templateUrl: './demande-list.component.html',
  styleUrl: './demande-list.component.css'
})
export class DemandeListComponent implements OnInit {
  demandes: DemandeResponse[] = [];
  isLoading = true;
  errorMessage = '';

  constructor(private demandeService: DemandeService, private router: Router) {}

  ngOnInit(): void {
    this.loadDemandes();
  }

  loadDemandes(): void {
    this.isLoading = true;
    this.demandeService.getAll().subscribe({
      next: (data) => {
        this.demandes = data;
        this.isLoading = false;
      },
      error: () => {
        this.errorMessage = 'Impossible de charger les demandes.';
        this.isLoading = false;
      }
    });
  }

  goToEdit(id: number): void {
    this.router.navigate(['/demandes', id, 'modifier']);
  }

  annuler(id: number, event: Event): void {
    event.stopPropagation();
    if (!confirm('Annuler cette demande ?')) return;

    this.demandeService.updateStatut(id, 'ANNULEE').subscribe({
      next: () => this.loadDemandes(),
      error: () => alert('Erreur lors de l\'annulation.')
    });
  }

  statutClass(statut: StatutDemande): string {
    const map: Record<StatutDemande, string> = {
      OUVERTE: 'badge-blue',
      EN_COURS: 'badge-amber',
      SUSPENDUE: 'badge-gray',
      ANNULEE: 'badge-red',
      CLOTUREE: 'badge-green'
    };
    return map[statut];
  }

  statutLabel(statut: StatutDemande): string {
    const map: Record<StatutDemande, string> = {
      OUVERTE: 'Ouverte',
      EN_COURS: 'En cours',
      SUSPENDUE: 'Suspendue',
      ANNULEE: 'Annulée',
      CLOTUREE: 'Clôturée'
    };
    return map[statut];
  }
}