export type PipelineStage = 'RH' | 'TECHNICAL' | 'MANAGERIAL' | 'HIRED' | 'VIVIER';
export type Decision = 'PENDING' | 'GO' | 'NO_GO' | 'KEEP_IN_POOL';

export interface CreateApplicationRequest {
  cvId: number;
  demandeId: number;
}

export interface DecisionRequest {
  decision: Decision;
  comment?: string;
}

export interface ApplicationResponse {
  id: number;
  cvId: number;
  nomCandidat: string;
  emailCandidat: string;
  demandeId: number;
  demandeTitre: string;
  matchingScore: number;
  stage: PipelineStage;
  decision: Decision;
  rhComment: string;
  technicalComment: string;
  managerialComment: string;
  createdAt: string;
  updatedAt: string;
}