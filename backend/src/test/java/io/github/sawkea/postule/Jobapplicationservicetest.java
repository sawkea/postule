package io.github.sawkea.postule;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import io.github.sawkea.common.ResourceNotFoundException;
import io.github.sawkea.user.CurrentUserProvider;
import io.github.sawkea.user.User;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Test unitaire du service : aucun Spring, aucune base de données.
 * Les dépendances (repository, utilisateur courant) sont remplacées par des "mocks" :
 * des faux objets dont on contrôle les réponses. Le test s'exécute en quelques millisecondes.
 *
 * Structure de chaque test : Arrange (préparer), Act (agir), Assert (vérifier).
 */
@ExtendWith(MockitoExtension.class)
class JobApplicationServiceTest {

    @Mock
    private JobApplicationRepository repository;

    @Mock
    private CurrentUserProvider currentUser;

    // Mockito crée le vrai service et lui injecte les deux mocks par le constructeur.
    @InjectMocks
    private JobApplicationService service;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setEmail("test@example.com");
        when(currentUser.get()).thenReturn(user);
    }

    @Test
    void create_assignsCurrentUser_andDefaultsStatusToToApply() {
        // Arrange : le repository renvoie l'objet qu'on lui demande de sauvegarder
        when(repository.save(any(JobApplication.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        JobApplicationResponse response = service.create(request("Exemple SA", "Développeuse", null));

        // Assert : on vérifie ce qui a été envoyé au repository, et ce qui est renvoyé
        ArgumentCaptor<JobApplication> captor = ArgumentCaptor.forClass(JobApplication.class);
        verify(repository).save(captor.capture());
        assertThat(captor.getValue().getUser()).isSameAs(user);
        assertThat(response.company()).isEqualTo("Exemple SA");
        assertThat(response.status()).isEqualTo(JobApplication.Status.TO_APPLY);
    }

    @Test
    void findById_throwsNotFound_whenApplicationIsNotOwnedByCurrentUser() {
        // Une candidature d'un autre utilisateur ou inexistante : le repository ne renvoie rien
        when(repository.findByIdAndUserId(42L, 1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.findById(42L))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void update_keepsCurrentStatus_whenRequestHasNoStatus() {
        JobApplication existing = existingApplication(5L, JobApplication.Status.SENT);
        when(repository.findByIdAndUserId(5L, 1L)).thenReturn(Optional.of(existing));

        JobApplicationResponse response = service.update(5L, request("Nouvelle SA", "Nouveau poste", null));

        assertThat(response.company()).isEqualTo("Nouvelle SA");
        assertThat(response.status()).isEqualTo(JobApplication.Status.SENT);
    }

    @Test
    void update_changesStatus_whenRequestHasOne() {
        JobApplication existing = existingApplication(5L, JobApplication.Status.SENT);
        when(repository.findByIdAndUserId(5L, 1L)).thenReturn(Optional.of(existing));

        JobApplicationResponse response = service.update(
                5L, request("Exemple SA", "Développeuse", JobApplication.Status.REJECTED));

        assertThat(response.status()).isEqualTo(JobApplication.Status.REJECTED);
    }

    @Test
    void delete_removesTheOwnedApplication() {
        JobApplication existing = existingApplication(5L, JobApplication.Status.SENT);
        when(repository.findByIdAndUserId(5L, 1L)).thenReturn(Optional.of(existing));

        service.delete(5L);

        verify(repository).delete(existing);
    }

    @Test
    void delete_throwsNotFound_andDeletesNothing_whenNotOwned() {
        when(repository.findByIdAndUserId(99L, 1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.delete(99L))
                .isInstanceOf(ResourceNotFoundException.class);
        verify(repository, never()).delete(any(JobApplication.class));
    }

    @Test
    void findAll_withoutStatus_returnsAllApplicationsOfTheUser() {
        when(repository.findByUserIdOrderBySentDateDesc(1L))
                .thenReturn(List.of(existingApplication(1L, JobApplication.Status.SENT)));

        List<JobApplicationResponse> result = service.findAll(null);

        assertThat(result).hasSize(1);
    }

    @Test
    void findAll_withStatus_filtersOnThatStatus() {
        when(repository.findByUserIdAndStatus(1L, JobApplication.Status.REJECTED))
                .thenReturn(List.of());

        List<JobApplicationResponse> result = service.findAll(JobApplication.Status.REJECTED);

        assertThat(result).isEmpty();
        verify(repository, never()).findByUserIdOrderBySentDateDesc(any());
    }

    // ---- Méthodes utilitaires pour garder les tests lisibles ----

    private JobApplicationRequest request(String company, String position, JobApplication.Status status) {
        // Les champs non utilisés dans ces tests sont à null.
        return new JobApplicationRequest(
                company, position, null, JobApplication.Type.OFFER_RESPONSE,
                null, null, null, null, null, null, null, null,
                status,
                null, null, null, null);
    }

    private JobApplication existingApplication(Long id, JobApplication.Status status) {
        JobApplication application = new JobApplication();
        application.setId(id);
        application.setUser(user);
        application.setCompany("Ancienne SA");
        application.setPosition("Ancien poste");
        application.setType(JobApplication.Type.OFFER_RESPONSE);
        application.setStatus(status);
        return application;
    }
}
