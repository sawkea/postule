package io.github.sawkea.postule;

import io.github.sawkea.contact.Contact;
import io.github.sawkea.interview.Interview;
import io.github.sawkea.user.User;
import jakarta.persistence.*;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "job_applications")
@Getter
@Setter
public class JobApplication {

    // Les 9 statuts du Excel. Stockés en texte (EnumType.STRING), jamais en numéro d'ordre.
    public enum Status {
        TO_APPLY,            // À postuler
        SENT,                // Envoyée
        TO_FOLLOW_UP,        // À relancer
        FOLLOWED_UP,         // Relancée
        INTERVIEW_SCHEDULED, // Entretien planifié
        INTERVIEW_DONE,      // Entretien passé
        REJECTED,            // Refusée
        ACCEPTED,            // Acceptée
        NO_RESPONSE          // Sans réponse
    }

    public enum Type {
        OFFER_RESPONSE,        // Réponse à offre
        SOLICITATION_RESPONSE, // Réponse à sollicitation
        SPONTANEOUS            // Candidature spontanée
    }

    public enum SalaryPeriod { YEARLY, MONTHLY }

    public enum SalaryBasis { GROSS, NET }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // LAZY : l'utilisateur n'est chargé que si on y accède vraiment.
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String company;

    @Column(nullable = false, length = 500)
    private String position;

    @Column(name = "sent_date")
    private LocalDate sentDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Type type;

    private String source;

    private String location;

    @Column(name = "remote_policy")
    private String remotePolicy;

    @Column(name = "salary_min")
    private Integer salaryMin;

    @Column(name = "salary_max")
    private Integer salaryMax;

    @Enumerated(EnumType.STRING)
    @Column(name = "salary_period")
    private SalaryPeriod salaryPeriod;

    @Enumerated(EnumType.STRING)
    @Column(name = "salary_basis")
    private SalaryBasis salaryBasis;

    // Catégorie dans la fonction publique (A, B, C), vide pour le privé.
    @Column(name = "public_category")
    private String publicCategory;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status = Status.TO_APPLY;

    // Ta colonne "Pourquoi ?" (motif de refus, etc.)
    @Column(name = "status_reason", columnDefinition = "text")
    private String statusReason;

    @Column(name = "follow_up_date")
    private LocalDate followUpDate;

    @Column(name = "offer_url", columnDefinition = "text")
    private String offerUrl;

    @Column(columnDefinition = "text")
    private String notes;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    // mappedBy : le côté propriétaire de la relation est le champ "jobApplication" des enfants.
    // orphanRemoval : retirer un entretien de la liste le supprime en base.
    @OneToMany(mappedBy = "jobApplication", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Interview> interviews = new ArrayList<>();

    @OneToMany(mappedBy = "jobApplication", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Contact> contacts = new ArrayList<>();

    // Méthodes utilitaires pour garder les deux côtés de la relation cohérents.
    public void addInterview(Interview interview) {
        interviews.add(interview);
        interview.setJobApplication(this);
    }

    public void addContact(Contact contact) {
        contacts.add(contact);
        contact.setJobApplication(this);
    }
}

