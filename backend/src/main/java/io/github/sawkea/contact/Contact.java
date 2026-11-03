package io.github.sawkea.contact;

import io.github.sawkea.postule.JobApplication;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

// entités CONTACTS
@Entity
@Table(name = "contacts")
@Getter
@Setter
public class Contact {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "job_application_id", nullable = false)
    private JobApplication jobApplication;

    @Column(nullable = false)
    private String name;

    private String role;

    private String email;

    private String phone;
}