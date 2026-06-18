package cibertec.edu.encuesta;

import cibertec.edu.exception.EstadoInvalidoException;
import cibertec.edu.exception.RecursoNoEncontradoException;
import cibertec.edu.opcion.OpcionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class EncuestaService {

    private final EncuestaRepository encuestaRepository;
    private final OpcionRepository opcionRepository;

    public EncuestaService(EncuestaRepository encuestaRepository, OpcionRepository opcionRepository) {
        this.encuestaRepository = encuestaRepository;
        this.opcionRepository = opcionRepository;
    }

    public List<EncuestaResponse> listar(String estado) {
        if (estado != null && !estado.isBlank()) {
            if (!estado.equals("borrador") && !estado.equals("activa") && !estado.equals("finalizada")) {
                throw new EstadoInvalidoException(
                    "Estado inválido: " + estado + ". Use: borrador, activa o finalizada");
            }
        }
        return encuestaRepository.findAll(estado);
    }

    public EncuestaResponse obtener(UUID id) {
        return encuestaRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Encuesta no encontrada: " + id));
    }

    @Transactional
    public EncuestaResponse crear(EncuestaRequest request, UUID adminId) {
        UUID nuevoId = encuestaRepository.insert(
                request.getTitulo(),
                request.getDescripcion(),
                adminId);

        if (request.getOpciones() != null) {
            for (int i = 0; i < request.getOpciones().size(); i++) {
                String texto = request.getOpciones().get(i);
                if (texto != null && !texto.isBlank()) {
                    opcionRepository.insert(nuevoId, texto, i);
                }
            }
        }

        return obtener(nuevoId);
    }

    public EncuestaResponse cambiarEstado(UUID id, String nuevoEstado, UUID adminId) {
        EncuestaResponse encuesta = obtener(id);

        if (!esTransicionValida(encuesta.estado(), nuevoEstado)) {
            throw new EstadoInvalidoException(
                "No se puede cambiar de '" + encuesta.estado() + "' a '" + nuevoEstado + "'");
        }

        int filas = encuestaRepository.updateEstado(id, nuevoEstado, adminId);
        if (filas == 0) {
            throw new RecursoNoEncontradoException("Encuesta no encontrada o no tienes permisos");
        }

        return obtener(id);
    }

    private boolean esTransicionValida(String actual, String nuevo) {
        return switch (actual) {
            case "borrador" -> nuevo.equals("activa");
            case "activa"   -> nuevo.equals("finalizada");
            default         -> false;
        };
    }

    @Transactional
    public void eliminar(UUID id) {
        int filas = encuestaRepository.delete(id);
        if (filas == 0) {
            throw new RecursoNoEncontradoException("Encuesta no encontrada: " + id);
        }
    }
}
