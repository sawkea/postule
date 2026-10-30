package io.github.sawkea.postule;
 
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
 
@RestController
@RequestMapping("/api/applications")
public class JobApplicationController {
 
    private final JobApplicationService service;
 
    public JobApplicationController(JobApplicationService service) {
        this.service = service;
    }
 
    // GET /api/applications            -> toutes les candidatures
    // GET /api/applications?status=SENT -> filtrées par statut
    @GetMapping
    public List<JobApplicationResponse> list(@RequestParam(required = false) JobApplication.Status status) {
        return service.findAll(status);
    }
 
    @GetMapping("/{id}")
    public JobApplicationResponse get(@PathVariable Long id) {
        return service.findById(id);
    }
 
    // @Valid déclenche la validation du corps de la requête (400 si invalide).
    // La réponse est un 201 Created avec l'adresse de la ressource dans l'en-tête Location.
    @PostMapping
    public ResponseEntity<JobApplicationResponse> create(@Valid @RequestBody JobApplicationRequest request) {
        JobApplicationResponse created = service.create(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.id())
                .toUri();
        return ResponseEntity.created(location).body(created);
    }
 
    @PutMapping("/{id}")
    public JobApplicationResponse update(@PathVariable Long id, @Valid @RequestBody JobApplicationRequest request) {
        return service.update(id, request);
    }
 
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}