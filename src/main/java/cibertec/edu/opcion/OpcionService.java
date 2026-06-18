package cibertec.edu.opcion;

import cibertec.edu.exception.RecursoNoEncontradoException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class OpcionService {

    private final OpcionRepository opcionRepository;

    public OpcionService(OpcionRepository opcionRepository) {
        this.opcionRepository = opcionRepository;
    }

    public List<OpcionResponse> listar(UUID encuestaId) {
        return opcionRepository.findByEncuesta(encuestaId);
    }

    public OpcionResponse obtener(UUID id) {
        return opcionRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Opción no encontrada: " + id));
    }

    public OpcionResponse crear(UUID encuestaId, OpcionRequest request) {
        UUID nuevoId = opcionRepository.insert(encuestaId, request.getTexto(), request.getOrden());
        return obtener(nuevoId);
    }

    public OpcionResponse editar(UUID id, OpcionRequest request) {
        obtener(id);
        int filas = opcionRepository.update(id, request.getTexto(), request.getOrden());
        if (filas == 0) throw new RecursoNoEncontradoException("Opción no encontrada: " + id);
        return obtener(id);
    }

    public void eliminar(UUID id) {
        obtener(id);
        opcionRepository.delete(id);
    }
}