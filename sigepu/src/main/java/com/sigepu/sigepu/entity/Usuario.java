package com.sigepu.sigepu.entity;

// Importamos nuestro Enum Rol
import com.sigepu.sigepu.enums.Rol;

// Importaciones de JPA (para la base de datos)
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

// Importaciones de Lombok (para generar codigo automatico)
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// @Entity: Indica que esta clase es una tabla en la BD
@Entity
// @Table: El nombre exacto de la tabla en MySQL
@Table(name = "usuarios")
// Lombok genera getters, setters, constructores, etc.
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    // Aqui guardaremos la contrasena. En el futuro la encriptaremos.
    @Column(nullable = false)
    private String password;

    // Guardamos el rol como texto (STRING) en la base de datos
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Rol rol;
    
    private String foto;
}