package io.github.sawkea.postule;

import io.github.sawkea.common.ResourceNotFoundException;
import io.github.sawkea.user.CurrentUserProvider;
import io.github.sawkea.user.User;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true) // par défaut, lecture seule. Les méthodes d'écriture le surchargent
public class JobApplicationService {

    private final JobApplicationRepository repository;
    private final CurrentUserProvider currentUser;

    // Injection par constructeur : Spring fournit lui-même les deux objets.
    public JobApplicationService(JobApplicationRepository repository, CurrentUserProvider currentUser) {
        this.repository = repository;
        this.currentUser = currentUser;
    }

    // LIST 
    public List<JobApplicationResponse> findAll(JobApplication.Status status) {
        Long userId = currentUser.get().getId();
        List<JobApplication> result = (status == null)
                ? repository.findByUserIdOrderBySentDateDesc(userId)
                : repository.findByUserIdAndStatus(userId, status);
        return result.stream().map(JobApplicationResponse::from).toList();
    }
    // FIND 
    public JobApplicationResponse findById(Long id) {
        return JobApplicationResponse.from(getOwned(id));
    }
    // CREATE 
    @Transactional
    public JobApplicationResponse create(JobApplicationRequest request) {
        JobApplication application = new JobApplication();
        application.setUser(currentUser.get());
        apply(application, request);
        return JobApplicationResponse.from(repository.save(application));
    }
    // UPDATE 
    @Transactional
    public JobApplicationResponse update(Long id, JobApplicationRequest request) {
        JobApplication application = getOwned(id);
        apply(application, request);
        // Pas de save() nécessaire : l'entité est "gérée" par Hibernate dans la transaction,
        // les changements sont enregistrés automatiquement à la fin de la méthode.
        return JobApplicationResponse.from(application);
    }
    // DELETE 
    @Transactional
    public void delete(Long id) {
        repository.delete(getOwned(id));
    }

    // Règle : on ne charge JAMAIS une candidature sans filtrer sur l'utilisateur.
    // Une candidature d'un autre utilisateur donne la même réponse qu'une candidature inexistante.
    private JobApplication getOwned(Long id) {
        User user = currentUser.get();
        return repository.findByIdAndUserId(id, user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Candidature introuvable : " + id));
    }
    // 
    private void apply(JobApplication a, JobApplicationRequest r) {
        a.setCompany(r.company());
        a.setPosition(r.position());
        a.setSentDate(r.sentDate());
        a.setType(r.type());
        a.setSource(r.source());
        a.setLocation(r.location());
        a.setRemotePolicy(r.remotePolicy());
        a.setSalaryMin(r.salaryMin());
        a.setSalaryMax(r.salaryMax());
        a.setSalaryPeriod(r.salaryPeriod());
        a.setSalaryBasis(r.salaryBasis());
        a.setPublicCategory(r.publicCategory());
        // Statut absent : à la création, la valeur par défaut de l'entité (TO_APPLY) s'applique ;
        // à la modification, on garde le statut actuel.
        if (r.status() != null) {
            a.setStatus(r.status());
        }
        a.setStatusReason(r.statusReason());
        a.setFollowUpDate(r.followUpDate());
        a.setOfferUrl(r.offerUrl());
        a.setNotes(r.notes());
    }
}