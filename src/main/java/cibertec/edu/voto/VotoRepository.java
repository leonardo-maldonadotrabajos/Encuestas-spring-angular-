	package cibertec.edu.voto;
	
	import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
	import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
	import org.springframework.stereotype.Repository;
	
	import java.util.List;
	import java.util.UUID;
	
	@Repository
	public class VotoRepository {
	
	    private final NamedParameterJdbcTemplate jdbc;
	
	    public VotoRepository(NamedParameterJdbcTemplate jdbc) {
	        this.jdbc = jdbc;
	    }
	
	    public UUID insertar(UUID encuestaId, UUID usuarioId, UUID opcionId) {
	        String sql = """
	                INSERT INTO votos (encuesta_id, usuario_id, opcion_id)
	                VALUES (:encuestaId, :usuarioId, :opcionId)
	                RETURNING id
	                """;
	        return jdbc.queryForObject(sql,
	                new MapSqlParameterSource()
	                        .addValue("encuestaId", encuestaId)
	                        .addValue("usuarioId",  usuarioId)
	                        .addValue("opcionId",   opcionId),
	                (rs, rn) -> UUID.fromString(rs.getString("id")));
	    }
	
    // ESPECIFICACIÓN: usa la vista v_resultados_encuesta
    public List<ResultadoResponse> findResultados(UUID encuestaId) {
        String sql = """
                SELECT encuesta_id, encuesta_titulo, estado,
                       opcion_id, opcion_texto, orden,
                       total_votos, COALESCE(porcentaje, 0) AS porcentaje
                FROM   v_resultados_encuesta
                WHERE  encuesta_id = :encuestaId
                ORDER  BY orden
                """;
        return jdbc.query(sql,
                new MapSqlParameterSource("encuestaId", encuestaId),
                (rs, rn) -> new ResultadoResponse(
                        UUID.fromString(rs.getString("encuesta_id")),
                        rs.getString("encuesta_titulo"),
                        rs.getString("estado"),
                        UUID.fromString(rs.getString("opcion_id")),
                        rs.getString("opcion_texto"),
                        rs.getInt("orden"),
                        rs.getLong("total_votos"),
                        rs.getDouble("porcentaje")
                ));
	    }
	
	    public boolean yaVoto(UUID usuarioId, UUID encuestaId) {
	        String sql = """
	                SELECT EXISTS (
	                    SELECT 1 FROM votos
	                    WHERE usuario_id = :usuarioId AND encuesta_id = :encuestaId
	                )
	                """;
	        Boolean result = jdbc.queryForObject(sql,
	                new MapSqlParameterSource()
	                        .addValue("usuarioId",  usuarioId)
	                        .addValue("encuestaId", encuestaId),
	                Boolean.class);
	        return Boolean.TRUE.equals(result);
	    }
	}