package io.github.sawkea.postule;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
 
/**
 * Ce que le client envoie pour créer ou modifier une candidature.
 * Un "record" Java est une classe immuable : les getters, equals et toString sont générés.
 * Les annotations déclenchent la validation grâce à @Valid dans le contrôleur.
 * Remarque : ni l'id, ni l'utilisateur, ni les dates de création n'y figurent,
 * le client n'a pas à les fournir (ni à les imposer).
 */
public record JobApplicationRequest(
        @NotBlank @Size(max = 255) String company,
        @NotBlank @Size(max = 500) String position,
        LocalDate sentDate,
        @NotNull JobApplication.Type type,
        @Size(max = 255) String source,
        @Size(max = 255) String location,
        @Size(max = 255) String remotePolicy,
        @PositiveOrZero Integer salaryMin,
        @PositiveOrZero Integer salaryMax,
        JobApplication.SalaryPeriod salaryPeriod,
        JobApplication.SalaryBasis salaryBasis,
        @Size(max = 50) String publicCategory,
        JobApplication.Status status,
        String statusReason,
        LocalDate followUpDate,
        String offerUrl,
        String notes) {
}
