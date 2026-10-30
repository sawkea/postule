package io.github.sawkea.common;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
 
// Lancée quand une ressource n'existe pas (ou n'appartient pas à l'utilisateur).
// Spring la transforme automatiquement en réponse HTTP 404.
@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {
 
    public ResourceNotFoundException(String message) {
        super(message);
    }
}