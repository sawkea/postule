import { DatePipe } from '@angular/common';
import { Component, OnInit, inject, signal } from '@angular/core';
import { JobApplication, STATUS_LABELS } from '../models/job-application.model';
import { JobApplicationService } from '../services/job-application.service';

@Component({
  selector: 'app-application-list',
  imports: [DatePipe],
  templateUrl: './application-list.html',
  styleUrl: './application-list.css',
})
export class ApplicationList implements OnInit {
  private readonly service = inject(JobApplicationService);

  // Les "signals" contiennent l'état de l'écran : quand leur valeur change,
  // Angular met à jour uniquement les parties du gabarit concernées.
  protected readonly applications = signal<JobApplication[]>([]);
  protected readonly loading = signal(true);
  protected readonly error = signal<string | null>(null);

  protected readonly statusLabels = STATUS_LABELS;

  ngOnInit(): void {
    this.service.findAll().subscribe({
      next: (data) => {
        this.applications.set(data);
        this.loading.set(false);
      },
      error: () => {
        this.error.set('Impossible de charger les candidatures. Le serveur est-il démarré ?');
        this.loading.set(false);
      },
    });
  }
}