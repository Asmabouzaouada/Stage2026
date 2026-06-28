export type NiveauExperience = 'JUNIOR' | 'CONFIRME' | 'SENIOR';
export type StatutDemande = 'OUVERTE' | 'EN_COURS' | 'SUSPENDUE' | 'ANNULEE' | 'CLOTUREE';

export interface DemandeRequest {
  titre: string;
  description: string;
  competencesRequises: string[];
  competencesSouhaitees: string[];
  niveauExperience: NiveauExperience;
  dateBesoin: string;
  urgence: boolean;
  demandeurId?: number;
}

export interface DemandeResponse {
  id: number;
  titre: string;
  description: string;
  competencesRequises: string[];
  competencesSouhaitees: string[];
  niveauExperience: NiveauExperience;
  dateBesoin: string;
  urgence: boolean;
  statut: StatutDemande;
  demandeurNom: string;
  createurNom: string;
  dateCreation: string;
}