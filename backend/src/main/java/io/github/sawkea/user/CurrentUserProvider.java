package io.github.sawkea.user;


import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
 
/**
 * TEMPORAIRE : renvoie toujours le même utilisateur de développement,
 * créé à la première utilisation. Quand l'authentification sera en place,
 * seule cette classe changera : elle ira lire l'utilisateur connecté.
 * Le reste du code (services, contrôleurs) n'aura pas à bouger.
 */
@Component
public class CurrentUserProvider {
 
    private static final String DEV_EMAIL = "dev@postule.local";
 
    private final UserRepository userRepository;
 
    public CurrentUserProvider(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
 
    @Transactional
    public User get() {
        return userRepository.findByEmail(DEV_EMAIL).orElseGet(() -> {
            User user = new User();
            user.setEmail(DEV_EMAIL);
            user.setPasswordHash("{noop}dev-only-no-login");
            return userRepository.save(user);
        });
    }
}
 