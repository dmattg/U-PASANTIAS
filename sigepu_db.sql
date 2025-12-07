-- ================================================================
-- SCRIPT MAESTRO DE REINICIO Y POBLADO MASIVO - SIGEPU v2.0
-- ================================================================

SET FOREIGN_KEY_CHECKS = 0;

-- 1. CREACIÓN DE BASE DE DATOS
CREATE DATABASE IF NOT EXISTS sigepu_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE sigepu_db;

-- 2. LIMPIEZA TOTAL
DROP TABLE IF EXISTS aplicaciones;
DROP TABLE IF EXISTS pasantias;
DROP TABLE IF EXISTS empresas;
DROP TABLE IF EXISTS estudiantes;
DROP TABLE IF EXISTS usuarios;
DROP TABLE IF EXISTS carreras;

-- 3. ESTRUCTURA DE TABLAS (ACTUALIZADA CON 'FOTO' Y 'FINALIZADO')

CREATE TABLE carreras (
  id BIGINT NOT NULL AUTO_INCREMENT,
  nombre VARCHAR(255) NOT NULL,
  PRIMARY KEY (id)
);

CREATE TABLE usuarios (
  id BIGINT NOT NULL AUTO_INCREMENT,
  email VARCHAR(255) NOT NULL UNIQUE,
  password VARCHAR(255) NOT NULL,
  rol ENUM('ESTUDIANTE', 'GESTOR_EMPRESARIAL', 'GESTOR_UNIVERSITARIO') NOT NULL,
  foto VARCHAR(255) DEFAULT NULL, -- Agregado para funcionalidad de perfil
  PRIMARY KEY (id)
);

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

CREATE TABLE empresas (
  id BIGINT NOT NULL AUTO_INCREMENT,
  nombre VARCHAR(255) NOT NULL,
  id_usuario_gestor BIGINT NOT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (id_usuario_gestor) REFERENCES usuarios (id)
);

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

CREATE TABLE aplicaciones (
  id BIGINT NOT NULL AUTO_INCREMENT,
  fecha_aplicacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  -- Incluye FINALIZADO para el cierre de ciclo
  estado_universidad ENUM('PENDIENTE', 'APROBADO', 'RECHAZADO', 'SOLICITUD_CANCELACION', 'CANCELADO', 'CONTRATADO', 'NO_SELECCIONADO', 'FINALIZADO') NOT NULL DEFAULT 'PENDIENTE',
  id_estudiante BIGINT NOT NULL,
  id_pasantia BIGINT NOT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (id_estudiante) REFERENCES estudiantes (id),
  FOREIGN KEY (id_pasantia) REFERENCES pasantias (id)
);

SET FOREIGN_KEY_CHECKS = 1;

-- ================================================================
-- 4. POBLADO MASIVO DE DATOS
-- ================================================================

-- A. CARRERAS (10 Opciones)
INSERT INTO carreras (nombre) VALUES 
('Ingeniería en Sistemas y Computación'),       -- ID 1
('Ingeniería Industrial'),                      -- ID 2
('Licenciatura en Administración de Empresas'), -- ID 3
('Licenciatura en Mercadeo'),                   -- ID 4
('Licenciatura en Diseño Gráfico'),             -- ID 5
('Licenciatura en Idioma Inglés'),              -- ID 6
('Licenciatura en Ciencias Jurídicas'),         -- ID 7
('Licenciatura en Contaduría Pública'),         -- ID 8
('Licenciatura en Psicología'),                 -- ID 9
('Arquitectura');                               -- ID 10

-- B. USUARIO ADMINISTRADOR (UNIVERSIDAD)
INSERT INTO usuarios (email, password, rol) VALUES 
('admin@utec.edu.sv', '$2a$10$77L6Feh8QyY0ZzzfUZOFm.KvtfHSwnUFTTEHNk0wEmtf6tEZIzrLm', 'GESTOR_UNIVERSITARIO'); -- ID 1

-- ------------------------------------------------------------------------------------------------
-- C. GENERACIÓN POR RUBRO (Empresas + Estudiantes + Pasantías)
-- Contraseña para todos: "1234"
-- ------------------------------------------------------------------------------------------------

-- === RUBRO 1: SISTEMAS (ID Carrera 1) ===
-- Usuarios Empresa
INSERT INTO usuarios (email, password, rol) VALUES 
('rrhh@tech.com', '$2a$10$77L6Feh8QyY0ZzzfUZOFm.KvtfHSwnUFTTEHNk0wEmtf6tEZIzrLm', 'GESTOR_EMPRESARIAL'), -- ID 2
('rrhh@soft.com', '$2a$10$77L6Feh8QyY0ZzzfUZOFm.KvtfHSwnUFTTEHNk0wEmtf6tEZIzrLm', 'GESTOR_EMPRESARIAL'); -- ID 3
-- Perfiles Empresa
INSERT INTO empresas (nombre, id_usuario_gestor) VALUES ('Tech Solutions', 2), ('SoftDev Corp', 3);
-- Usuarios Estudiantes
INSERT INTO usuarios (email, password, rol) VALUES 
('juan.sistemas@mail.com', '$2a$10$77L6Feh8QyY0ZzzfUZOFm.KvtfHSwnUFTTEHNk0wEmtf6tEZIzrLm', 'ESTUDIANTE'), -- ID 4
('ana.sistemas@mail.com', '$2a$10$77L6Feh8QyY0ZzzfUZOFm.KvtfHSwnUFTTEHNk0wEmtf6tEZIzrLm', 'ESTUDIANTE');  -- ID 5
-- Perfiles Estudiantes
INSERT INTO estudiantes (nombre, apellido, id_usuario, id_carrera) VALUES ('Juan', 'Perez', 4, 1), ('Ana', 'Gomez', 5, 1);
-- Pasantías (5)
INSERT INTO pasantias (titulo, descripcion, id_empresa, id_carrera_requerida) VALUES 
('Desarrollador Java Jr', 'Backend Spring Boot.', 1, 1),
('Soporte Técnico', 'Mantenimiento preventivo.', 1, 1),
('QA Tester', 'Pruebas automatizadas.', 2, 1),
('Desarrollador Frontend', 'React y Angular.', 2, 1),
('Admin de Base de Datos', 'MySQL y Oracle.', 2, 1);

-- === RUBRO 2: INDUSTRIAL (ID Carrera 2) ===
INSERT INTO usuarios (email, password, rol) VALUES 
('rrhh@factory.com', '$2a$10$77L6Feh8QyY0ZzzfUZOFm.KvtfHSwnUFTTEHNk0wEmtf6tEZIzrLm', 'GESTOR_EMPRESARIAL'),
('rrhh@logistica.com', '$2a$10$77L6Feh8QyY0ZzzfUZOFm.KvtfHSwnUFTTEHNk0wEmtf6tEZIzrLm', 'GESTOR_EMPRESARIAL');
INSERT INTO empresas (nombre, id_usuario_gestor) VALUES ('Manufacturas El Sol', 6), ('Logística Global', 7);
INSERT INTO usuarios (email, password, rol) VALUES 
('pedro.ind@mail.com', '$2a$10$77L6Feh8QyY0ZzzfUZOFm.KvtfHSwnUFTTEHNk0wEmtf6tEZIzrLm', 'ESTUDIANTE'),
('lucia.ind@mail.com', '$2a$10$77L6Feh8QyY0ZzzfUZOFm.KvtfHSwnUFTTEHNk0wEmtf6tEZIzrLm', 'ESTUDIANTE');
INSERT INTO estudiantes (nombre, apellido, id_usuario, id_carrera) VALUES ('Pedro', 'Rivas', 8, 2), ('Lucia', 'Mendez', 9, 2);
INSERT INTO pasantias (titulo, descripcion, id_empresa, id_carrera_requerida) VALUES 
('Analista de Procesos', 'Optimización de líneas.', 3, 2),
('Supervisor de Calidad', 'Normas ISO.', 3, 2),
('Seguridad Industrial', 'Prevención de riesgos.', 3, 2),
('Asistente de Bodega', 'Control de inventarios.', 4, 2),
('Jefe de Flota Jr', 'Logística de transporte.', 4, 2);

-- === RUBRO 3: ADMINISTRACIÓN (ID Carrera 3) ===
INSERT INTO usuarios (email, password, rol) VALUES ('rrhh@grupo.com', '$2a$10$77L6Feh8QyY0ZzzfUZOFm.KvtfHSwnUFTTEHNk0wEmtf6tEZIzrLm', 'GESTOR_EMPRESARIAL'), ('rrhh@corp.com', '$2a$10$77L6Feh8QyY0ZzzfUZOFm.KvtfHSwnUFTTEHNk0wEmtf6tEZIzrLm', 'GESTOR_EMPRESARIAL');
INSERT INTO empresas (nombre, id_usuario_gestor) VALUES ('Grupo Roble', 10), ('Corporación Multi', 11);
INSERT INTO usuarios (email, password, rol) VALUES ('carlos.admin@mail.com', '$2a$10$77L6Feh8QyY0ZzzfUZOFm.KvtfHSwnUFTTEHNk0wEmtf6tEZIzrLm', 'ESTUDIANTE'), ('sofia.admin@mail.com', '$2a$10$77L6Feh8QyY0ZzzfUZOFm.KvtfHSwnUFTTEHNk0wEmtf6tEZIzrLm', 'ESTUDIANTE');
INSERT INTO estudiantes (nombre, apellido, id_usuario, id_carrera) VALUES ('Carlos', 'Diaz', 12, 3), ('Sofia', 'Luna', 13, 3);
INSERT INTO pasantias (titulo, descripcion, id_empresa, id_carrera_requerida) VALUES 
('Asistente Administrativo', 'Gestión de archivos.', 5, 3),
('Auxiliar de Gerencia', 'Apoyo a dirección.', 5, 3),
('Analista de Compras', 'Gestión de proveedores.', 6, 3),
('Recepción y Atención', 'Cliente interno.', 6, 3),
('Gestor de Proyectos Jr', 'Seguimiento de KPIs.', 6, 3);

-- === RUBRO 4: MERCADEO (ID Carrera 4) ===
INSERT INTO usuarios (email, password, rol) VALUES ('rrhh@agency.com', '$2a$10$77L6Feh8QyY0ZzzfUZOFm.KvtfHSwnUFTTEHNk0wEmtf6tEZIzrLm', 'GESTOR_EMPRESARIAL'), ('rrhh@brand.com', '$2a$10$77L6Feh8QyY0ZzzfUZOFm.KvtfHSwnUFTTEHNk0wEmtf6tEZIzrLm', 'GESTOR_EMPRESARIAL');
INSERT INTO empresas (nombre, id_usuario_gestor) VALUES ('Agencia Publicidad Top', 14), ('Brand Managers', 15);
INSERT INTO usuarios (email, password, rol) VALUES ('luis.mkt@mail.com', '$2a$10$77L6Feh8QyY0ZzzfUZOFm.KvtfHSwnUFTTEHNk0wEmtf6tEZIzrLm', 'ESTUDIANTE'), ('elena.mkt@mail.com', '$2a$10$77L6Feh8QyY0ZzzfUZOFm.KvtfHSwnUFTTEHNk0wEmtf6tEZIzrLm', 'ESTUDIANTE');
INSERT INTO estudiantes (nombre, apellido, id_usuario, id_carrera) VALUES ('Luis', 'Vega', 16, 4), ('Elena', 'Cruz', 17, 4);
INSERT INTO pasantias (titulo, descripcion, id_empresa, id_carrera_requerida) VALUES 
('Community Manager', 'Redes Sociales.', 7, 4),
('Analista de Mercado', 'Encuestas y focus group.', 7, 4),
('Asistente de Marca', 'Branding corporativo.', 8, 4),
('Ventas Jr', 'Prospección de clientes.', 8, 4),
('Organizador de Eventos', 'Logística de lanzamientos.', 8, 4);

-- === RUBRO 5: DISEÑO GRÁFICO (ID Carrera 5) ===
INSERT INTO usuarios (email, password, rol) VALUES ('rrhh@pixel.com', '$2a$10$77L6Feh8QyY0ZzzfUZOFm.KvtfHSwnUFTTEHNk0wEmtf6tEZIzrLm', 'GESTOR_EMPRESARIAL'), ('rrhh@studio.com', '$2a$10$77L6Feh8QyY0ZzzfUZOFm.KvtfHSwnUFTTEHNk0wEmtf6tEZIzrLm', 'GESTOR_EMPRESARIAL');
INSERT INTO empresas (nombre, id_usuario_gestor) VALUES ('Pixel Art Studio', 18), ('Studio Creativo', 19);
INSERT INTO usuarios (email, password, rol) VALUES ('diego.dis@mail.com', '$2a$10$77L6Feh8QyY0ZzzfUZOFm.KvtfHSwnUFTTEHNk0wEmtf6tEZIzrLm', 'ESTUDIANTE'), ('valeria.dis@mail.com', '$2a$10$77L6Feh8QyY0ZzzfUZOFm.KvtfHSwnUFTTEHNk0wEmtf6tEZIzrLm', 'ESTUDIANTE');
INSERT INTO estudiantes (nombre, apellido, id_usuario, id_carrera) VALUES ('Diego', 'Soto', 20, 5), ('Valeria', 'Mejia', 21, 5);
INSERT INTO pasantias (titulo, descripcion, id_empresa, id_carrera_requerida) VALUES 
('Diseñador UI/UX', 'Figma y Adobe XD.', 9, 5),
('Ilustrador Digital', 'Creación de personajes.', 9, 5),
('Editor de Video', 'Premiere y After Effects.', 10, 5),
('Diseñador Editorial', 'Maquetación de revistas.', 10, 5),
('Fotógrafo Jr', 'Edición en Lightroom.', 10, 5);

-- === RUBRO 6: INGLÉS (ID Carrera 6) ===
INSERT INTO usuarios (email, password, rol) VALUES ('rrhh@academy.com', '$2a$10$77L6Feh8QyY0ZzzfUZOFm.KvtfHSwnUFTTEHNk0wEmtf6tEZIzrLm', 'GESTOR_EMPRESARIAL'), ('rrhh@callcenter.com', '$2a$10$77L6Feh8QyY0ZzzfUZOFm.KvtfHSwnUFTTEHNk0wEmtf6tEZIzrLm', 'GESTOR_EMPRESARIAL');
INSERT INTO empresas (nombre, id_usuario_gestor) VALUES ('English Academy', 22), ('Global Call Center', 23);
INSERT INTO usuarios (email, password, rol) VALUES ('tom.eng@mail.com', '$2a$10$77L6Feh8QyY0ZzzfUZOFm.KvtfHSwnUFTTEHNk0wEmtf6tEZIzrLm', 'ESTUDIANTE'), ('kate.eng@mail.com', '$2a$10$77L6Feh8QyY0ZzzfUZOFm.KvtfHSwnUFTTEHNk0wEmtf6tEZIzrLm', 'ESTUDIANTE');
INSERT INTO estudiantes (nombre, apellido, id_usuario, id_carrera) VALUES ('Tom', 'Hardy', 24, 6), ('Kate', 'Winslet', 25, 6);
INSERT INTO pasantias (titulo, descripcion, id_empresa, id_carrera_requerida) VALUES 
('Teacher Assistant', 'Apoyo en clases.', 11, 6),
('Traductor de Documentos', 'Traducción técnica.', 11, 6),
('Customer Service Agent', 'Atención bilingüe.', 12, 6),
('Quality Assurance EN', 'Monitoreo de llamadas.', 12, 6),
('Content Writer EN', 'Redacción de blogs.', 12, 6);

-- === RUBRO 7: JURÍDICAS (ID Carrera 7) ===
INSERT INTO usuarios (email, password, rol) VALUES ('rrhh@lex.com', '$2a$10$77L6Feh8QyY0ZzzfUZOFm.KvtfHSwnUFTTEHNk0wEmtf6tEZIzrLm', 'GESTOR_EMPRESARIAL'), ('rrhh@notaria.com', '$2a$10$77L6Feh8QyY0ZzzfUZOFm.KvtfHSwnUFTTEHNk0wEmtf6tEZIzrLm', 'GESTOR_EMPRESARIAL');
INSERT INTO empresas (nombre, id_usuario_gestor) VALUES ('Bufete Lex', 26), ('Notaría Central', 27);
INSERT INTO usuarios (email, password, rol) VALUES ('mario.law@mail.com', '$2a$10$77L6Feh8QyY0ZzzfUZOFm.KvtfHSwnUFTTEHNk0wEmtf6tEZIzrLm', 'ESTUDIANTE'), ('laura.law@mail.com', '$2a$10$77L6Feh8QyY0ZzzfUZOFm.KvtfHSwnUFTTEHNk0wEmtf6tEZIzrLm', 'ESTUDIANTE');
INSERT INTO estudiantes (nombre, apellido, id_usuario, id_carrera) VALUES ('Mario', 'Bros', 28, 7), ('Laura', 'Croft', 29, 7);
INSERT INTO pasantias (titulo, descripcion, id_empresa, id_carrera_requerida) VALUES 
('Asistente Legal Civil', 'Revisión de demandas.', 13, 7),
('Procurador', 'Visitas a juzgados.', 13, 7),
('Asistente Notarial', 'Elaboración de escrituras.', 14, 7),
('Investigador Jurídico', 'Análisis de jurisprudencia.', 14, 7),
('Auxiliar Corporativo', 'Contratos mercantiles.', 14, 7);

-- === RUBRO 8: CONTADURÍA (ID Carrera 8) ===
INSERT INTO usuarios (email, password, rol) VALUES ('rrhh@audit.com', '$2a$10$77L6Feh8QyY0ZzzfUZOFm.KvtfHSwnUFTTEHNk0wEmtf6tEZIzrLm', 'GESTOR_EMPRESARIAL'), ('rrhh@bank.com', '$2a$10$77L6Feh8QyY0ZzzfUZOFm.KvtfHSwnUFTTEHNk0wEmtf6tEZIzrLm', 'GESTOR_EMPRESARIAL');
INSERT INTO empresas (nombre, id_usuario_gestor) VALUES ('Auditores Asociados', 30), ('Banco Nacional', 31);
INSERT INTO usuarios (email, password, rol) VALUES ('beto.conta@mail.com', '$2a$10$77L6Feh8QyY0ZzzfUZOFm.KvtfHSwnUFTTEHNk0wEmtf6tEZIzrLm', 'ESTUDIANTE'), ('gaby.conta@mail.com', '$2a$10$77L6Feh8QyY0ZzzfUZOFm.KvtfHSwnUFTTEHNk0wEmtf6tEZIzrLm', 'ESTUDIANTE');
INSERT INTO estudiantes (nombre, apellido, id_usuario, id_carrera) VALUES ('Beto', 'Cuevas', 32, 8), ('Gaby', 'Espino', 33, 8);
INSERT INTO pasantias (titulo, descripcion, id_empresa, id_carrera_requerida) VALUES 
('Auxiliar Contable', 'Partidas de diario.', 15, 8),
('Auditor Jr', 'Arqueo de caja.', 15, 8),
('Cajero Bancario', 'Manejo de efectivo.', 16, 8),
('Analista de Créditos', 'Evaluación de riesgo.', 16, 8),
('Asistente de Impuestos', 'Declaraciones IVA.', 16, 8);

-- === RUBRO 9: PSICOLOGÍA (ID Carrera 9) ===
INSERT INTO usuarios (email, password, rol) VALUES ('rrhh@talent.com', '$2a$10$77L6Feh8QyY0ZzzfUZOFm.KvtfHSwnUFTTEHNk0wEmtf6tEZIzrLm', 'GESTOR_EMPRESARIAL'), ('rrhh@clinic.com', '$2a$10$77L6Feh8QyY0ZzzfUZOFm.KvtfHSwnUFTTEHNk0wEmtf6tEZIzrLm', 'GESTOR_EMPRESARIAL');
INSERT INTO empresas (nombre, id_usuario_gestor) VALUES ('Talento Humano S.A.', 34), ('Clínica Mente Sana', 35);
INSERT INTO usuarios (email, password, rol) VALUES ('sigmund.psi@mail.com', '$2a$10$77L6Feh8QyY0ZzzfUZOFm.KvtfHSwnUFTTEHNk0wEmtf6tEZIzrLm', 'ESTUDIANTE'), ('anna.psi@mail.com', '$2a$10$77L6Feh8QyY0ZzzfUZOFm.KvtfHSwnUFTTEHNk0wEmtf6tEZIzrLm', 'ESTUDIANTE');
INSERT INTO estudiantes (nombre, apellido, id_usuario, id_carrera) VALUES ('Sigmund', 'Freud', 36, 9), ('Anna', 'O', 37, 9);
INSERT INTO pasantias (titulo, descripcion, id_empresa, id_carrera_requerida) VALUES 
('Reclutador Jr', 'Entrevistas de trabajo.', 17, 9),
('Capacitador', 'Talleres de clima laboral.', 17, 9),
('Asistente Clínico', 'Recepción de pacientes.', 18, 9),
('Psicometrista', 'Aplicación de pruebas.', 18, 9),
('Asistente Educativo', 'Consejería estudiantil.', 18, 9);

-- === RUBRO 10: ARQUITECTURA (ID Carrera 10) ===
INSERT INTO usuarios (email, password, rol) VALUES ('rrhh@constru.com', '$2a$10$77L6Feh8QyY0ZzzfUZOFm.KvtfHSwnUFTTEHNk0wEmtf6tEZIzrLm', 'GESTOR_EMPRESARIAL'), ('rrhh@urbano.com', '$2a$10$77L6Feh8QyY0ZzzfUZOFm.KvtfHSwnUFTTEHNk0wEmtf6tEZIzrLm', 'GESTOR_EMPRESARIAL');
INSERT INTO empresas (nombre, id_usuario_gestor) VALUES ('Constructora El Salvador', 38), ('Diseño Urbano', 39);
INSERT INTO usuarios (email, password, rol) VALUES ('frank.arq@mail.com', '$2a$10$77L6Feh8QyY0ZzzfUZOFm.KvtfHSwnUFTTEHNk0wEmtf6tEZIzrLm', 'ESTUDIANTE'), ('zaha.arq@mail.com', '$2a$10$77L6Feh8QyY0ZzzfUZOFm.KvtfHSwnUFTTEHNk0wEmtf6tEZIzrLm', 'ESTUDIANTE');
INSERT INTO estudiantes (nombre, apellido, id_usuario, id_carrera) VALUES ('Frank', 'Lloyd', 40, 10), ('Zaha', 'Hadid', 41, 10);
INSERT INTO pasantias (titulo, descripcion, id_empresa, id_carrera_requerida) VALUES 
('Dibujante AutoCAD', 'Planos estructurales.', 19, 10),
('Supervisor de Obra', 'Bitácora de campo.', 19, 10),
('Diseñador de Interiores', 'Renderizado 3D.', 20, 10),
('Modelador BIM', 'Revit y Archicad.', 20, 10),
('Analista de Presupuestos', 'Costos de materiales.', 20, 10);