package io.github.sawkea.common;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
 
// 404 : Réponse HTTP 404 Not Found pour les ressources introuvables 
@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {
 
    public ResourceNotFoundException(String message) {
        super(message);
    }
}