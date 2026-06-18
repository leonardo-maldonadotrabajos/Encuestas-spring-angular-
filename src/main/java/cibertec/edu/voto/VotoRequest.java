package cibertec.edu.voto;

import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public class VotoRequest {

    @NotNull(message = "El ID de la opción es obligatorio")
    private UUID opcionId;

    public UUID getOpcionId()              { return opcionId; }
    public void setOpcionId(UUID opcionId) { this.opcionId = opcionId; }
}