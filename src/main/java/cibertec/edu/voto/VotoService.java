package cibertec.edu.voto;

import cibertec.edu.exception.ConflictoException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class VotoService {

    private final VotoRepository votoRepository;

    public VotoService(VotoRepository votoRepository) {
        this.votoRepository = votoRepository;
    }

    public UUID votar(UUID encuestaId, UUID usuarioId, VotoRequest request) {
        // VALIDACIÓN: verificar si ya votó (doble capa junto al UNIQUE constraint de BD)
        if (votoRepository.yaVoto(usuarioId, encuestaId)) {
            throw new ConflictoException("Ya has votado en esta encuesta");
        }
        return votoRepository.insertar(encuestaId, usuarioId, request.getOpcionId());
    }

    // ESPECIFICACIÓN: resultados usando la vista de BD
    public List<ResultadoResponse> resultados(UUID encuestaId) {
        return votoRepository.findResultados(encuestaId);
    }
}