package cibertec.edu.opcion;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class OpcionRepository {

    private final NamedParameterJdbcTemplate jdbc;

    public OpcionRepository(NamedParameterJdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    private OpcionResponse mapRow(java.sql.ResultSet rs, int rn) throws java.sql.SQLException {
        return new OpcionResponse(
                UUID.fromString(rs.getString("id")),
                UUID.fromString(rs.getString("encuesta_id")),
                rs.getString("texto"),
                rs.getInt("orden")
        );
    }

    public List<OpcionResponse> findByEncuesta(UUID encuestaId) {
        String sql = """
                SELECT id, encuesta_id, texto, orden
                FROM   opciones
                WHERE  encuesta_id = :encuestaId
                ORDER  BY orden ASC
                """;
        return jdbc.query(sql, new MapSqlParameterSource("encuestaId", encuestaId), this::mapRow);
    }

    public Optional<OpcionResponse> findById(UUID id) {
        String sql = "SELECT id, encuesta_id, texto, orden FROM opciones WHERE id = :id";
        return jdbc.query(sql, new MapSqlParameterSource("id", id), this::mapRow)
                .stream().findFirst();
    }

    public UUID insert(UUID encuestaId, String texto, int orden) {
        String sql = """
                INSERT INTO opciones (encuesta_id, texto, orden)
                VALUES (:encuestaId, :texto, :orden)
                RETURNING id
                """;
        return jdbc.queryForObject(sql,
                new MapSqlParameterSource()
                        .addValue("encuestaId", encuestaId)
                        .addValue("texto",      texto)
                        .addValue("orden",      orden),
                (rs, rn) -> UUID.fromString(rs.getString("id")));
    }

    public int update(UUID id, String texto, int orden) {
        String sql = "UPDATE opciones SET texto = :texto, orden = :orden WHERE id = :id";
        return jdbc.update(sql, new MapSqlParameterSource()
                .addValue("texto", texto)
                .addValue("orden", orden)
                .addValue("id",    id));
    }

    public int delete(UUID id) {
        return jdbc.update("DELETE FROM opciones WHERE id = :id",
                new MapSqlParameterSource("id", id));
    }
}