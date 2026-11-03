import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { JobApplication } from '../models/job-application.model';

// providedIn: 'root' : une seule instance partagée dans toute l'application
// (équivalent d'un service Symfony, injecté là où on en a besoin).
@Injectable({ providedIn: 'root' })
export class JobApplicationService {
  private readonly http = inject(HttpClient);

  // Adresse relative : c'est le proxy (en développement) qui la redirige vers localhost:8080.
  private readonly baseUrl = '/api/applications';

  // Récupère toutes les candidatures de l'utilisateur connecté.
  findAll(): Observable<JobApplication[]> {
    return this.http.get<JobApplication[]>(this.baseUrl);
  }
}