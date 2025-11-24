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
@Table(name = "pasantias")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Pasantia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String titulo;

    // Aunque en la BD es tipo TEXT para soportar textos largos,
    // en Java lo manejamos simplemente como un String.
    @Column(nullable = false)
    private String descripcion;

    // --- Relaciones ---

    // Relacion: Indica a que Empresa pertenece esta oferta.
    @ManyToOne
    // El nombre en @JoinColumn debe coincidir exactamente con la FK en la tabla 'pasantias'
    @JoinColumn(name = "id_empresa", nullable = false)
    private Empresa empresa;

    // Relacion: Indica que Carrera se requiere para esta pasantia.
    @ManyToOne
    @JoinColumn(name = "id_carrera_requerida", nullable = false)
    private Carrera carreraRequerida;
    
    @Column(nullable = false)
    private boolean activa = true;
}