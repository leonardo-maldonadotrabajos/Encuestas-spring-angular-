package cibertec.edu.encuesta;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * PROYECCIÓN: Solo expone los campos necesarios al cliente.
 * No expone campos internos como triggers o metadatos de BD.
 */
public record EncuestaResponse(
        UUID    id,
        String  titulo,
        String  descripcion,
        String  estado,
        UUID    creadoPor,
        LocalDateTime creadoEn,
        LocalDateTime actualizadoEn
) {}