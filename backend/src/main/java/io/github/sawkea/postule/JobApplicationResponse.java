package io.github.sawkea.postule;

import java.time.Instant;
import java.time.LocalDate;
 
/**
 * Ce que l'API renvoie au client. On ne renvoie jamais l'entité directement :
 * elle contient l'utilisateur (donc le mot de passe haché) et des listes liées,
 * ce qui provoquerait des fuites de données et des boucles de sérialisation.
 */
public record JobApplicationResponse(
        Long id,
        String company,
        String position,
        LocalDate sentDate,
        JobApplication.Type type,
        String source,
        String location,
        String remotePolicy,
        Integer salaryMin,
        Integer salaryMax,
        JobApplication.SalaryPeriod salaryPeriod,
        JobApplication.SalaryBasis salaryBasis,
        String publicCategory,
        JobApplication.Status status,
        String statusReason,
        LocalDate followUpDate,
        String offerUrl,
        String notes,
        Instant createdAt,
        Instant updatedAt) {
 
    public static JobApplicationResponse from(JobApplication a) {
        return new JobApplicationResponse(
                a.getId(),
                a.getCompany(),
                a.getPosition(),
                a.getSentDate(),
                a.getType(),
                a.getSource(),
                a.getLocation(),
                a.getRemotePolicy(),
                a.getSalaryMin(),
                a.getSalaryMax(),
                a.getSalaryPeriod(),
                a.getSalaryBasis(),
                a.getPublicCategory(),
                a.getStatus(),
                a.getStatusReason(),
                a.getFollowUpDate(),
                a.getOfferUrl(),
                a.getNotes(),
                a.getCreatedAt(),
                a.getUpdatedAt());
    }
}