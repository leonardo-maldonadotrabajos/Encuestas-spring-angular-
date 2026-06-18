package cibertec.edu.opcion;

import java.util.UUID;

public record OpcionResponse(
        UUID   id,
        UUID   encuestaId,
        String texto,
        int    orden
) {}