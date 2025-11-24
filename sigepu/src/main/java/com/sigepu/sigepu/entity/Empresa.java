package com.sigepu.sigepu.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "empresas")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Empresa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    // --- Relacion con Usuario ---

    // @ManyToOne: Indica la relacion. "Muchas" empresas podrian estar asociadas
    // a "Un" usuario gestor (aunque en nuestro caso de negocio sera 1 a 1,
    // ManyToOne es la forma estandar de mapear una FK simple).
    @ManyToOne
    // @JoinColumn: Especifica el nombre exacto de la columna llave foranea en la tabla de BD.
    // nullable = false asegura que toda empresa deba tener un gestor asignado.
    @JoinColumn(name = "id_usuario_gestor", nullable = false)
    private Usuario usuarioGestor;
}