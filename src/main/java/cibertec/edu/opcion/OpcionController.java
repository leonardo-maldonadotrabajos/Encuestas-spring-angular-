package cibertec.edu.opcion;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/encuestas/{encuestaId}/opciones")
public class OpcionController {

    private final OpcionService opcionService;

    public OpcionController(OpcionService opcionService) {
        this.opcionService = opcionService;
    }

    @GetMapping
    public ResponseEntity<List<OpcionResponse>> listar(@PathVariable UUID encuestaId) {
        return ResponseEntity.ok(opcionService.listar(encuestaId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<OpcionResponse> obtener(@PathVariable UUID encuestaId, @PathVariable UUID id) {
        return ResponseEntity.ok(opcionService.obtener(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<OpcionResponse> crear(@PathVariable UUID encuestaId,
            @Valid @RequestBody OpcionRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(opcionService.crear(encuestaId, request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<OpcionResponse> editar(@PathVariable UUID encuestaId,
            @PathVariable UUID id, @Valid @RequestBody OpcionRequest request) {
        return ResponseEntity.ok(opcionService.editar(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> eliminar(@PathVariable UUID encuestaId, @PathVariable UUID id) {
        opcionService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}