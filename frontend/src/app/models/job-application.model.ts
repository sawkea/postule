// Reflète exactement ce que renvoie l'API (JobApplicationResponse côté Spring).
// Les enums Java deviennent des types "union de chaînes" en TypeScript.
 
export type ApplicationStatus =
  | 'TO_APPLY'
  | 'SENT'
  | 'TO_FOLLOW_UP'
  | 'FOLLOWED_UP'
  | 'INTERVIEW_SCHEDULED'
  | 'INTERVIEW_DONE'
  | 'REJECTED'
  | 'ACCEPTED'
  | 'NO_RESPONSE';
 
export type ApplicationType = 'OFFER_RESPONSE' | 'SOLICITATION_RESPONSE' | 'SPONTANEOUS';
 
export type SalaryPeriod = 'YEARLY' | 'MONTHLY';
 
export type SalaryBasis = 'GROSS' | 'NET';
 
export interface JobApplication {
  id: number;
  company: string;
  position: string;
  sentDate: string | null; // format ISO "2026-10-30"
  type: ApplicationType;
  source: string | null;
  location: string | null;
  remotePolicy: string | null;
  salaryMin: number | null;
  salaryMax: number | null;
  salaryPeriod: SalaryPeriod | null;
  salaryBasis: SalaryBasis | null;
  publicCategory: string | null;
  status: ApplicationStatus;
  statusReason: string | null;
  followUpDate: string | null;
  offerUrl: string | null;
  notes: string | null;
  createdAt: string;
  updatedAt: string;
}
 
// Libellés affichés à l'écran (l'API, elle, ne manipule que les codes).
export const STATUS_LABELS: Record<ApplicationStatus, string> = {
  TO_APPLY: 'À postuler',
  SENT: 'Envoyée',
  TO_FOLLOW_UP: 'À relancer',
  FOLLOWED_UP: 'Relancée',
  INTERVIEW_SCHEDULED: 'Entretien planifié',
  INTERVIEW_DONE: 'Entretien passé',
  REJECTED: 'Refusée',
  ACCEPTED: 'Acceptée',
  NO_RESPONSE: 'Sans réponse',
};