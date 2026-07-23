import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { ApplicationService } from '../../../core/services/application.service';
import { ApplicationResponse, Decision, PipelineStage } from '../../../core/models/application.model';
import { KanbanCardComponent } from '../kanban-card/kanban-card.component';

@Component({
  selector: 'app-kanban-board',
  standalone: true,
  imports: [FormsModule, KanbanCardComponent],
  templateUrl: './kanban-board.component.html',
  styleUrl: './kanban-board.component.css'
})
export class KanbanBoardComponent implements OnInit {
  applications: ApplicationResponse[] = [];
  demandeId: number | null = null;
  isLoading = true;
  errorMessage = '';

  selectedApp: ApplicationResponse | null = null;
  selectedDecision: Decision = 'GO';
  comment = '';
  showModal = false;

  columns: { stage: PipelineStage; label: string; color: string }[] = [
    { stage: 'RH', label: 'Entretien RH', color: 'col-rh' },
    { stage: 'TECHNICAL', label: 'Technique', color: 'col-tech' },
    { stage: 'MANAGERIAL', label: 'Managérial', color: 'col-mgr' },
    { stage: 'HIRED', label: 'Recrutés', color: 'col-hired' },
    { stage: 'VIVIER', label: 'Vivier', color: 'col-vivier' }
  ];

  constructor(
    private applicationService: ApplicationService,
    private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('demandeId');
    if (id) {
      this.demandeId = +id;
      this.load();
    }
  }

  load(): void {
    if (!this.demandeId) return;
    this.isLoading = true;
    this.applicationService.getByDemande(this.demandeId).subscribe({
      next: (data) => {
        this.applications = data;
        this.isLoading = false;
      },
      error: () => {
        this.errorMessage = 'Impossible de charger le pipeline.';
        this.isLoading = false;
      }
    });
  }

  getByStage(stage: PipelineStage): ApplicationResponse[] {
    return this.applications.filter(a => a.stage === stage);
  }

  openDecisionModal(app: ApplicationResponse): void {
    this.selectedApp = app;
    this.selectedDecision = 'GO';
    this.comment = '';
    this.showModal = true;
  }

  closeModal(): void {
    this.showModal = false;
    this.selectedApp = null;
  }

  submitDecision(): void {
    if (!this.selectedApp) return;
    this.applicationService.updateDecision(this.selectedApp.id, {
      decision: this.selectedDecision,
      comment: this.comment
    }).subscribe({
      next: () => {
        this.closeModal();
        this.load();
      },
      error: () => alert('Erreur lors de la mise à jour.')
    });
  }

  decisionsForStage(stage: PipelineStage): Decision[] {
    if (stage === 'RH') return ['GO', 'NO_GO'];
    if (stage === 'TECHNICAL') return ['GO', 'NO_GO', 'KEEP_IN_POOL'];
    if (stage === 'MANAGERIAL') return ['GO', 'NO_GO', 'KEEP_IN_POOL'];
    return [];
  }
}