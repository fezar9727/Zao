-- Estaciones de prueba para la base zao_pruebas (solo perfil "servicios").
-- Permiten crear turnos sin tocar la base real zao_db.
INSERT INTO estacion (restaurante_id, nombre, fecha_creacion, fecha_actualizacion)
SELECT id, 'Parrilla', NOW(), NOW() FROM restaurante WHERE nit = '900000000-1';

INSERT INTO estacion (restaurante_id, nombre, fecha_creacion, fecha_actualizacion)
SELECT id, 'Salteados', NOW(), NOW() FROM restaurante WHERE nit = '900000000-1';
