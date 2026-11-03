package io.github.sawkea.interview;

import io.github.sawkea.postule.JobApplication;
import jakarta.persistence.*;
import java.time.Instant;
import lombok.Getter;
import lombok.Setter;

// // entités INTERVIEWS
@Entity
@Table(name = "interviews")
@Getter
@Setter
public class Interview {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "job_application_id", nullable = false)
    private JobApplication jobApplication;

    @Column(name = "scheduled_at", nullable = false)
    private Instant scheduledAt;

    private String interviewer;

    // Ex. : "entretien téléphonique", "dans les bureaux", "visio"
    private String format;

    // Compte rendu
    @Column(columnDefinition = "text")
    private String summary;
}
