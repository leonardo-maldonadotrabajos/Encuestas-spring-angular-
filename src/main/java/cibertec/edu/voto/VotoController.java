package cibertec.edu.voto;

import cibertec.edu.auth.UsuarioPrincipal;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Controller de Votos.
 *
 * POST /api/encuestas/{id}/votar       → emitir voto (usuario autenticado)
 * GET  /api/encuestas/{id}/resultados  → ver resultados (cualquier autenticado)
 */
@RestController
@RequestMapping("/api/encuestas/{encuestaId}")
public class VotoController {

    private final VotoService votoService;

    public VotoController(VotoService votoService) {
        this.votoService = votoService;
    }

    @PostMapping("/votar")
    @PreAuthorize("hasRole('USUARIO')")
    public ResponseEntity<Map<String, Object>> votar(
            @PathVariable UUID encuestaId,
            @Valid @RequestBody VotoRequest request,
            @AuthenticationPrincipal UsuarioPrincipal principal) {

        UUID votoId = votoService.votar(encuestaId, principal.id(), request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of(
                        "mensaje", "Voto registrado correctamente",
                        "votoId",  votoId.toString()
                ));
    }

    @GetMapping("/resultados")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<ResultadoResponse>> resultados(@PathVariable UUID encuestaId) {
        return ResponseEntity.ok(votoService.resultados(encuestaId));
    }
}