package cibertec.edu.voto;

import java.util.UUID;

public record ResultadoResponse(
        UUID   encuestaId,
        String encuestaTitulo,
        String estado,
        UUID   opcionId,
        String opcionTexto,
        int    orden,
        long   totalVotos,
        double porcentaje
) {}