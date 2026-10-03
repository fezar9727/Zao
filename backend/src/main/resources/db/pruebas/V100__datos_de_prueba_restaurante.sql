-- Restaurante de prueba para la base zao_pruebas (solo perfil "servicios").
-- Permite registrar empleados sin tocar la base real zao_db.
INSERT INTO restaurante (nombre_comercial, nit, direccion, ciudad, fecha_creacion, fecha_actualizacion)
VALUES ('Restaurante de Pruebas Zao', '900000000-1', 'Calle de Pruebas 123', 'Cali', NOW(), NOW());
