export type SourceCandidat = 'LINKEDIN' | 'CABINET' | 'COOPTATION' | 'CANDIDATURE_SPONTANEE' | 'JOBBOARD' | 'AUTRE';

export interface CvResponse {
  id: number;
  nomFichierOriginal: string;
  nomCandidat: string;
  emailCandidat: string;
  telephoneCandidat: string;
  competencesDetectees: string[];
  experiencesDetectees: string;
  formationDetectee: string;
  source: SourceCandidat;
  demandeId: number | null;
  demandeTitre: string | null;
  dateUpload: string;
  doublonPotentiel: boolean;
}