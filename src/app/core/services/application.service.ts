import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ApplicationResponse, CreateApplicationRequest, DecisionRequest } from '../models/application.model';
import { environment } from '../../../environments/environment';

@Injectable({ providedIn: 'root' })
export class ApplicationService {
  private apiUrl = `${environment.apiUrl}/applications`;

  constructor(private http: HttpClient) {}

  create(request: CreateApplicationRequest): Observable<ApplicationResponse> {
    return this.http.post<ApplicationResponse>(this.apiUrl, request);
  }

  getByDemande(demandeId: number): Observable<ApplicationResponse[]> {
    return this.http.get<ApplicationResponse[]>(`${this.apiUrl}/demande/${demandeId}`);
  }

  getByCv(cvId: number): Observable<ApplicationResponse[]> {
    return this.http.get<ApplicationResponse[]>(`${this.apiUrl}/cv/${cvId}`);
  }

  updateDecision(id: number, request: DecisionRequest): Observable<ApplicationResponse> {
    return this.http.patch<ApplicationResponse>(`${this.apiUrl}/${id}/decision`, request);
  }
}