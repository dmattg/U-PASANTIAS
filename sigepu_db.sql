-- Creamos la base de datos si no existe, para evitar errores.
CREATE DATABASE IF NOT EXISTS sigepu_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- Le decimos a MySQL que vamos a trabajar sobre esta base de datos.
USE sigepu_db;

-- -----------------------------------------------------
-- Tabla: carreras
-- Almacena las carreras universitarias disponibles en el sistema.
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS carreras (
  id INT NOT NULL AUTO_INCREMENT,
  nombre VARCHAR(255) NOT NULL,
  PRIMARY KEY (id)
);

-- -----------------------------------------------------
-- Tabla: usuarios
-- Tabla central para la autenticacion. Almacena los datos de login
-- y el rol de cada persona en el sistema.
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS usuarios (
  id INT NOT NULL AUTO_INCREMENT,
  email VARCHAR(255) NOT NULL UNIQUE,
  password VARCHAR(255) NOT NULL,
  rol ENUM('ESTUDIANTE', 'GESTOR_EMPRESARIAL', 'GESTOR_UNIVERSITARIO') NOT NULL,
  PRIMARY KEY (id)
);

-- -----------------------------------------------------
-- Tabla: estudiantes
-- Almacena la informacion especifica del estudiante y se relaciona
-- con un usuario y una carrera.
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS estudiantes (
  id INT NOT NULL AUTO_INCREMENT,
  nombre VARCHAR(100) NOT NULL,
  apellido VARCHAR(100) NOT NULL,
  id_usuario INT NOT NULL,
  id_carrera INT NOT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (id_usuario) REFERENCES usuarios (id),
  FOREIGN KEY (id_carrera) REFERENCES carreras (id)
);

-- -----------------------------------------------------
-- Tabla: empresas
-- Almacena la informacion de las empresas que publican pasantias.
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS empresas (
  id INT NOT NULL AUTO_INCREMENT,
  nombre VARCHAR(255) NOT NULL,
  id_usuario_gestor INT NOT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (id_usuario_gestor) REFERENCES usuarios (id)
);

-- -----------------------------------------------------
-- Tabla: pasantias
-- Contiene toda la informacion sobre las ofertas de pasantias.
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS pasantias (
  id INT NOT NULL AUTO_INCREMENT,
  titulo VARCHAR(255) NOT NULL,
  descripcion TEXT NOT NULL,
  id_empresa INT NOT NULL,
  id_carrera_requerida INT NOT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (id_empresa) REFERENCES empresas (id),
  FOREIGN KEY (id_carrera_requerida) REFERENCES carreras (id)
);

-- -----------------------------------------------------
-- Tabla: aplicaciones
-- La tabla mas importante! Conecta a un estudiante con una pasantia
-- y guarda el estado de la postulacion.
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS aplicaciones (
  id INT NOT NULL AUTO_INCREMENT,
  fecha_aplicacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  estado_universidad ENUM('PENDIENTE', 'APROBADO', 'RECHAZADO') NOT NULL DEFAULT 'PENDIENTE',
  id_estudiante INT NOT NULL,
  id_pasantia INT NOT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (id_estudiante) REFERENCES estudiantes (id),
  FOREIGN KEY (id_pasantia) REFERENCES pasantias (id)
);