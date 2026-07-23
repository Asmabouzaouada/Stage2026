import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { CvResponse, SourceCandidat } from '../models/cv.model';
import { environment } from '../../../environments/environment';

@Injectable({ providedIn: 'root' })
export class CvService {
  private apiUrl = `${environment.apiUrl}/cvs`;

  constructor(private http: HttpClient) {}

  upload(file: File, source: SourceCandidat, demandeId?: number, commentaire?: string): Observable<CvResponse> {
    const formData = new FormData();
    formData.append('file', file);
    formData.append('source', source);
    if (demandeId) formData.append('demandeId', demandeId.toString());
    if (commentaire) formData.append('commentaire', commentaire);

    return this.http.post<CvResponse>(`${this.apiUrl}/upload`, formData);
  }

  getVivierGeneral(): Observable<CvResponse[]> {
    return this.http.get<CvResponse[]>(`${this.apiUrl}/vivier`);
  }

  getById(id: number): Observable<CvResponse> {
    return this.http.get<CvResponse>(`${this.apiUrl}/${id}`);
  }

  getByDemande(demandeId: number): Observable<CvResponse[]> {
    return this.http.get<CvResponse[]>(`${this.apiUrl}/demande/${demandeId}`);
  }

  rattacherADemande(cvId: number, demandeId: number): Observable<CvResponse> {
    return this.http.patch<CvResponse>(`${this.apiUrl}/${cvId}/rattacher/${demandeId}`, {});
  }

  ajouterAuVivier(cvId: number): Observable<CvResponse> {
    return this.http.patch<CvResponse>(`${this.apiUrl}/${cvId}/ajouter-vivier`, {});
  }

  download(cvId: number): Observable<Blob> {
    return this.http.get(`${this.apiUrl}/${cvId}/download`, { responseType: 'blob' });
  }

  delete(cvId: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${cvId}`);
  }
}