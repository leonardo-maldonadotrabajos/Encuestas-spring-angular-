-- Migración: todas las encuestas en 'borrador' pasan a 'finalizada'
-- El trigger trg_encuestas_estado solo permite: borrador → activa → finalizada
-- Por eso se hace en dos pasos usando una CTE

WITH cambiados AS (
    UPDATE encuestas
    SET    estado = 'activa'
    WHERE  estado = 'borrador'
    RETURNING id
)
UPDATE encuestas e
SET    estado = 'finalizada'
FROM   cambiados c
WHERE  e.id = c.id;
