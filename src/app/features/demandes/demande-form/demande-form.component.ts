import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { DemandeService } from '../../../core/services/demande.service';
import { DemandeRequest, DemandeResponse } from '../../../core/models/demande.model';
import { FormsModule } from '@angular/forms';
@Component({
  selector: 'app-demande-form',
  standalone: true,
   imports: [ReactiveFormsModule, FormsModule, RouterLink],
  templateUrl: './demande-form.component.html',
  styleUrl: './demande-form.component.css'
})
export class DemandeFormComponent implements OnInit {
  form: FormGroup;
  isEditMode = false;
  demandeId: number | null = null;
  errorMessage = '';
  isLoading = false;

  competenceRequiseInput = '';
  competenceSouhaiteeInput = '';
  competencesRequises: string[] = [];
  competencesSouhaitees: string[] = [];

  constructor(
    private fb: FormBuilder,
    private demandeService: DemandeService,
    private router: Router,
    private route: ActivatedRoute
  ) {
    this.form = this.fb.group({
      titre: ['', Validators.required],
      description: ['', Validators.required],
      niveauExperience: ['JUNIOR', Validators.required],
      dateBesoin: ['', Validators.required],
      urgence: [false]
    });
  }

  ngOnInit(): void {
    const idParam = this.route.snapshot.paramMap.get('id');
    if (idParam) {
      this.isEditMode = true;
      this.demandeId = +idParam;
      this.loadDemande(this.demandeId);
    }
  }

  loadDemande(id: number): void {
    this.demandeService.getById(id).subscribe({
      next: (demande) => {
        this.form.patchValue({
          titre: demande.titre,
          description: demande.description,
          niveauExperience: demande.niveauExperience,
          dateBesoin: demande.dateBesoin,
          urgence: demande.urgence
        });
        this.competencesRequises = demande.competencesRequises || [];
        this.competencesSouhaitees = demande.competencesSouhaitees || [];
      },
      error: () => this.errorMessage = 'Impossible de charger cette demande.'
    });
  }

  ajouterCompetenceRequise(): void {
    const val = this.competenceRequiseInput.trim();
    if (val && !this.competencesRequises.includes(val)) {
      this.competencesRequises.push(val);
    }
    this.competenceRequiseInput = '';
  }

  retirerCompetenceRequise(comp: string): void {
    this.competencesRequises = this.competencesRequises.filter(c => c !== comp);
  }

  ajouterCompetenceSouhaitee(): void {
    const val = this.competenceSouhaiteeInput.trim();
    if (val && !this.competencesSouhaitees.includes(val)) {
      this.competencesSouhaitees.push(val);
    }
    this.competenceSouhaiteeInput = '';
  }

  retirerCompetenceSouhaitee(comp: string): void {
    this.competencesSouhaitees = this.competencesSouhaitees.filter(c => c !== comp);
  }

  onSubmit(): void {
    if (this.form.invalid || this.competencesRequises.length === 0) {
      this.errorMessage = 'Veuillez remplir tous les champs requis et ajouter au moins une compétence requise.';
      return;
    }

    this.isLoading = true;
    this.errorMessage = '';

    const payload = {
      ...this.form.value,
      competencesRequises: this.competencesRequises,
      competencesSouhaitees: this.competencesSouhaitees
    };

    const request = this.isEditMode && this.demandeId
      ? this.demandeService.update(this.demandeId, payload)
      : this.demandeService.create(payload);

    request.subscribe({
      next: () => this.router.navigate(['/demandes']),
      error: () => {
        this.isLoading = false;
        this.errorMessage = 'Une erreur est survenue lors de l\'enregistrement.';
      }
    });
  }
}