-- ================================================================
-- SCRIPT MAESTRO DE REINICIO Y POBLADO - SIGEPU
-- ================================================================

-- 1. PREPARACIÓN DEL ENTORNO
-- Desactivamos checks para poder borrar tablas sin problemas de relaciones
SET FOREIGN_KEY_CHECKS = 0;

-- Seleccionamos o creamos la BD
CREATE DATABASE IF NOT EXISTS sigepu_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE sigepu_db;

-- 2. ELIMINACIÓN DE TABLAS ANTIGUAS (Para asegurar estructura nueva)
DROP TABLE IF EXISTS aplicaciones;
DROP TABLE IF EXISTS pasantias;
DROP TABLE IF EXISTS empresas;
DROP TABLE IF EXISTS estudiantes;
DROP TABLE IF EXISTS usuarios;
DROP TABLE IF EXISTS carreras;

-- 3. CREACIÓN DE TABLAS (Estructura Final)

-- Tabla: carreras
CREATE TABLE carreras (
  id BIGINT NOT NULL AUTO_INCREMENT,
  nombre VARCHAR(255) NOT NULL,
  PRIMARY KEY (id)
);

-- Tabla: usuarios
CREATE TABLE usuarios (
  id BIGINT NOT NULL AUTO_INCREMENT,
  email VARCHAR(255) NOT NULL UNIQUE,
  password VARCHAR(255) NOT NULL, -- Hash BCrypt
  rol ENUM('ESTUDIANTE', 'GESTOR_EMPRESARIAL', 'GESTOR_UNIVERSITARIO') NOT NULL,
  PRIMARY KEY (id)
);

-- Tabla: estudiantes
CREATE TABLE estudiantes (
  id BIGINT NOT NULL AUTO_INCREMENT,
  nombre VARCHAR(100) NOT NULL,
  apellido VARCHAR(100) NOT NULL,
  id_usuario BIGINT NOT NULL,
  id_carrera BIGINT NOT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (id_usuario) REFERENCES usuarios (id),
  FOREIGN KEY (id_carrera) REFERENCES carreras (id)
);

-- Tabla: empresas
CREATE TABLE empresas (
  id BIGINT NOT NULL AUTO_INCREMENT,
  nombre VARCHAR(255) NOT NULL,
  id_usuario_gestor BIGINT NOT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (id_usuario_gestor) REFERENCES usuarios (id)
);

-- Tabla: pasantias
-- Incluye el campo 'activa' para que la empresa pueda ocultarlas
CREATE TABLE pasantias (
  id BIGINT NOT NULL AUTO_INCREMENT,
  titulo VARCHAR(255) NOT NULL,
  descripcion TEXT NOT NULL,
  activa BOOLEAN NOT NULL DEFAULT TRUE,
  id_empresa BIGINT NOT NULL,
  id_carrera_requerida BIGINT NOT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (id_empresa) REFERENCES empresas (id),
  FOREIGN KEY (id_carrera_requerida) REFERENCES carreras (id)
);

-- Tabla: aplicaciones
-- Incluye todos los estados del flujo completo
CREATE TABLE aplicaciones (
  id BIGINT NOT NULL AUTO_INCREMENT,
  fecha_aplicacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  estado_universidad ENUM('PENDIENTE', 'APROBADO', 'RECHAZADO', 'SOLICITUD_CANCELACION', 'CANCELADO', 'CONTRATADO', 'NO_SELECCIONADO') NOT NULL DEFAULT 'PENDIENTE',
  id_estudiante BIGINT NOT NULL,
  id_pasantia BIGINT NOT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (id_estudiante) REFERENCES estudiantes (id),
  FOREIGN KEY (id_pasantia) REFERENCES pasantias (id)
);

-- Reactivamos checks de seguridad
SET FOREIGN_KEY_CHECKS = 1;


-- ================================================================
-- 4. POBLADO DE DATOS (SEMILLA)
-- ================================================================

-- A. CARRERAS (Oferta Académica)
INSERT INTO carreras (nombre) VALUES 
('Ingeniería en Sistemas y Computación'),      -- ID 1
('Ingeniería Industrial'),                     -- ID 2
('Licenciatura en Administración de Empresas'),-- ID 3
('Licenciatura en Mercadeo'),                  -- ID 4
('Licenciatura en Diseño Gráfico'),            -- ID 5
('Licenciatura en Idioma Inglés'),             -- ID 6
('Licenciatura en Ciencias Jurídicas'),        -- ID 7
('Licenciatura en Contaduría Pública'),        -- ID 8
('Licenciatura en Psicología'),                -- ID 9
('Arquitectura');                              -- ID 10

-- B. USUARIOS DEL SISTEMA
-- Todos usan la contraseña "1234"
-- Hash: $2a$10$77L6Feh8QyY0ZzzfUZOFm.KvtfHSwnUFTTEHNk0wEmtf6tEZIzrLm

-- 1. Gestor Universitario
INSERT INTO usuarios (email, password, rol) VALUES 
('admin@utec.edu.sv', '$2a$10$77L6Feh8QyY0ZzzfUZOFm.KvtfHSwnUFTTEHNk0wEmtf6tEZIzrLm', 'GESTOR_UNIVERSITARIO');

-- 2. Gestores Empresariales
INSERT INTO usuarios (email, password, rol) VALUES 
('rrhh@techsv.com', '$2a$10$77L6Feh8QyY0ZzzfUZOFm.KvtfHSwnUFTTEHNk0wEmtf6tEZIzrLm', 'GESTOR_EMPRESARIAL'), -- ID 2
('hola@pixel.com', '$2a$10$77L6Feh8QyY0ZzzfUZOFm.KvtfHSwnUFTTEHNk0wEmtf6tEZIzrLm', 'GESTOR_EMPRESARIAL'),  -- ID 3
('legal@bufete.com', '$2a$10$77L6Feh8QyY0ZzzfUZOFm.KvtfHSwnUFTTEHNk0wEmtf6tEZIzrLm', 'GESTOR_EMPRESARIAL'); -- ID 4

-- 3. Estudiantes
INSERT INTO usuarios (email, password, rol) VALUES 
('juan@mail.com', '$2a$10$77L6Feh8QyY0ZzzfUZOFm.KvtfHSwnUFTTEHNk0wEmtf6tEZIzrLm', 'ESTUDIANTE'),   -- ID 5
('maria@mail.com', '$2a$10$77L6Feh8QyY0ZzzfUZOFm.KvtfHSwnUFTTEHNk0wEmtf6tEZIzrLm', 'ESTUDIANTE'),  -- ID 6
('carlos@mail.com', '$2a$10$77L6Feh8QyY0ZzzfUZOFm.KvtfHSwnUFTTEHNk0wEmtf6tEZIzrLm', 'ESTUDIANTE'); -- ID 7

-- C. PERFILES DE EMPRESAS (Vinculadas a usuarios 2, 3, 4)
INSERT INTO empresas (nombre, id_usuario_gestor) VALUES 
('Tech Solutions El Salvador', 2),
('Agencia Creativa Pixel', 3),
('Bufete Méndez & Asociados', 4);

-- D. PERFILES DE ESTUDIANTES (Vinculados a usuarios 5, 6, 7)
INSERT INTO estudiantes (nombre, apellido, id_usuario, id_carrera) VALUES 
('Juan', 'Perez', 5, 1),   -- Sistemas
('Maria', 'Lopez', 6, 5),  -- Diseño
('Carlos', 'Ruiz', 7, 7);  -- Jurídicas

-- E. PASANTÍAS DISPONIBLES
-- Tech Solutions (Sistemas)
INSERT INTO pasantias (titulo, descripcion, id_empresa, id_carrera_requerida, activa) VALUES 
('Desarrollador Backend Java', 'Conocimientos en Spring Boot y MySQL requeridos.', 1, 1, true),
('Soporte Técnico Nivel 1', 'Mantenimiento preventivo y correctivo.', 1, 1, true);

-- Agencia Pixel (Diseño)
INSERT INTO pasantias (titulo, descripcion, id_empresa, id_carrera_requerida, activa) VALUES 
('Diseñador UI/UX Junior', 'Creación de prototipos en Figma.', 2, 5, true),
('Editor de Video', 'Apoyo en creación de contenido para redes.', 2, 5, false); -- Esta nace DESACTIVADA para pruebas

-- Bufete Méndez (Leyes)
INSERT INTO pasantias (titulo, descripcion, id_empresa, id_carrera_requerida, activa) VALUES 
('Asistente Legal', 'Revisión de expedientes civiles.', 3, 7, true);

-- ¡BASE DE DATOS LISTA PARA LA DEMO!