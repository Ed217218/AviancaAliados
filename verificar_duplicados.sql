-- Script para verificar datos duplicados en TEMP_BLOCKS_ONEBLOCK
-- Ejecutar en Oracle SQL Developer o similar

-- 1. Ver todos los registros de prueba
SELECT 
    SOLICITANTE,
    NEGOCIO,
    AEROLINEA,
    NUMERO_VUELO,
    ORIGEN,
    DESTINO,
    FECHA_INICIAL,
    FECHA_FINAL,
    ASIENTOS,
    CREATED_DATE
FROM ISL2K.TEMP_BLOCKS_ONEBLOCK
WHERE SOLICITANTE LIKE 'Test%'
ORDER BY CREATED_DATE DESC;

-- 2. Verificar duplicados específicos
SELECT 
    SOLICITANTE,
    NEGOCIO,
    COUNT(*) AS CANTIDAD_DUPLICADOS
FROM ISL2K.TEMP_BLOCKS_ONEBLOCK
WHERE SOLICITANTE LIKE 'Test%'
GROUP BY SOLICITANTE, NEGOCIO
HAVING COUNT(*) > 1;

-- 3. Ver el constraint que está fallando
SELECT 
    constraint_name,
    constraint_type,
    search_condition
FROM user_constraints
WHERE table_name = 'TEMP_BLOCKS_ONEBLOCK';

-- 4. Ver las columnas del unique constraint
SELECT 
    column_name,
    position
FROM user_cons_columns
WHERE constraint_name = 'PA_HOLDS_00007'  -- constraint que está fallando
ORDER BY position;

-- 5. Limpiar datos de prueba (CUIDADO - solo en QA)
-- DELETE FROM ISL2K.TEMP_BLOCKS_ONEBLOCK
-- WHERE SOLICITANTE LIKE 'Test%';
-- COMMIT;
