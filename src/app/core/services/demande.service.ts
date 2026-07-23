import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { DemandeRequest, DemandeResponse, StatutDemande } from '../models/demande.model';
import { environment } from '../../../environments/environment';

@Injectable({ providedIn: 'root' })
export class DemandeService {
  private apiUrl = `${environment.apiUrl}/demandes`;

  constructor(private http: HttpClient) {}

  getAll(): Observable<DemandeResponse[]> {
    return this.http.get<DemandeResponse[]>(this.apiUrl);
  }

  getById(id: number): Observable<DemandeResponse> {
    return this.http.get<DemandeResponse>(`${this.apiUrl}/${id}`);
  }

  create(request: DemandeRequest): Observable<DemandeResponse> {
    return this.http.post<DemandeResponse>(this.apiUrl, request);
  }

  update(id: number, request: DemandeRequest): Observable<DemandeResponse> {
    return this.http.put<DemandeResponse>(`${this.apiUrl}/${id}`, request);
  }

  updateStatut(id: number, statut: StatutDemande): Observable<DemandeResponse> {
    return this.http.patch<DemandeResponse>(`${this.apiUrl}/${id}/statut`, statut);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}