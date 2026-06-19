package cibertec.edu.encuesta;

import cibertec.edu.auth.UsuarioPrincipal;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@CrossOrigin(origins = "https://sistema-encuestas-frontend.onrender.com")
@RestController
@RequestMapping("/api/encuestas")
public class EncuestaController {

    private final EncuestaService encuestaService;

    public EncuestaController(EncuestaService encuestaService) {
        this.encuestaService = encuestaService;
    }

    @GetMapping
    public ResponseEntity<List<EncuestaResponse>> listar(
            @RequestParam(required = false) String estado) {
        return ResponseEntity.ok(encuestaService.listar(estado));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EncuestaResponse> obtener(@PathVariable UUID id) {
        return ResponseEntity.ok(encuestaService.obtener(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EncuestaResponse> crear(
            @Valid @RequestBody EncuestaRequest request,
            @AuthenticationPrincipal UsuarioPrincipal principal) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(encuestaService.crear(request, principal.id()));
    }

    @PutMapping("/{id}/estado")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EncuestaResponse> cambiarEstado(
            @PathVariable UUID id,
            @RequestBody java.util.Map<String, String> body,
            @AuthenticationPrincipal UsuarioPrincipal principal) {
        return ResponseEntity.ok(encuestaService.cambiarEstado(
                id, body.get("estado"), principal.id()));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> eliminar(@PathVariable UUID id) {
        encuestaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
