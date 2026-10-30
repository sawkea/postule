package io.github.sawkea.postule;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

// Spring Data génère l'implémentation à partir du nom des méthodes.
// Toutes les méthodes filtrent sur l'utilisateur : c'est la règle du multi-utilisateur.
// Ne jamais utiliser findById() seul dans un service exposé à l'API.
public interface JobApplicationRepository extends JpaRepository<JobApplication, Long> {

    List<JobApplication> findByUserIdOrderBySentDateDesc(Long userId);

    Optional<JobApplication> findByIdAndUserId(Long id, Long userId);

    List<JobApplication> findByUserIdAndStatus(Long userId, JobApplication.Status status);

    // Relances à faire : date de relance atteinte ou dépassée
    List<JobApplication> findByUserIdAndFollowUpDateLessThanEqualOrderByFollowUpDate(
            Long userId, LocalDate date);
}
