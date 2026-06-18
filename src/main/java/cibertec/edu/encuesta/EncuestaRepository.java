package cibertec.edu.encuesta;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class EncuestaRepository {

    private final NamedParameterJdbcTemplate jdbc;

    public EncuestaRepository(NamedParameterJdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    private EncuestaResponse mapRow(java.sql.ResultSet rs, int rowNum) throws java.sql.SQLException {
        return new EncuestaResponse(
                UUID.fromString(rs.getString("id")),
                rs.getString("titulo"),
                rs.getString("descripcion"),
                rs.getString("estado"),
                UUID.fromString(rs.getString("creado_por")),
                rs.getTimestamp("creado_en").toLocalDateTime(),
                rs.getTimestamp("actualizado_en").toLocalDateTime()
        );
    }

    public List<EncuestaResponse> findAll(String estado) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        String sql;
        if (estado != null) {
            sql = """
                    SELECT id, titulo, descripcion, estado,
                           creado_por, creado_en, actualizado_en
                    FROM   encuestas
                    WHERE  estado = :estado::survey_status
                    ORDER  BY creado_en DESC
                    """;
            params.addValue("estado", estado);
        } else {
            sql = """
                    SELECT id, titulo, descripcion, estado,
                           creado_por, creado_en, actualizado_en
                    FROM   encuestas
                    ORDER  BY creado_en DESC
                    """;
        }
        return jdbc.query(sql, params, this::mapRow);
    }

    public Optional<EncuestaResponse> findById(UUID id) {
        String sql = """
                SELECT id, titulo, descripcion, estado,
                       creado_por, creado_en, actualizado_en
                FROM   encuestas
                WHERE  id = :id
                """;
        return jdbc.query(sql, new MapSqlParameterSource("id", id), this::mapRow)
                .stream().findFirst();
    }

    public UUID insert(String titulo, String descripcion, UUID creadoPor) {
        String sql = """
                INSERT INTO encuestas (titulo, descripcion, creado_por, estado)
                VALUES (:titulo, :descripcion, :creadoPor, 'borrador')
                RETURNING id
                """;
        var params = new MapSqlParameterSource()
                .addValue("titulo",      titulo)
                .addValue("descripcion", descripcion)
                .addValue("creadoPor",   creadoPor);
        return jdbc.queryForObject(sql, params,
                (rs, rn) -> UUID.fromString(rs.getString("id")));
    }

    public int updateEstado(UUID id, String estado, UUID adminId) {
        String sql = """
                UPDATE encuestas
                SET    estado = :estado::survey_status
                WHERE  id = :id AND creado_por = :adminId
                """;
        return jdbc.update(sql, new MapSqlParameterSource()
                .addValue("estado",  estado)
                .addValue("id",      id)
                .addValue("adminId", adminId));
    }

    public int delete(UUID id) {
        String sql = "DELETE FROM encuestas WHERE id = :id";
        return jdbc.update(sql, new MapSqlParameterSource("id", id));
    }
}
