package com.sigepu.sigepu.entity;

import com.sigepu.sigepu.enums.EstadoAplicacion;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "aplicaciones")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Aplicacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // La fecha y hora exacta en que el estudiante hizo clic en "Postular".
    // Se guardara automaticamente gracias al metodo @PrePersist de abajo.
    @Column(name = "fecha_aplicacion", nullable = false, updatable = false)
    private LocalDateTime fechaAplicacion;

    // El estado actual de la postulacion.
    @Enumerated(EnumType.STRING)
    @Column(name = "estado_universidad", nullable = false)
    private EstadoAplicacion estadoUniversidad;

    // --- Relaciones ---

    // Quien es el estudiante que aplica?
    @ManyToOne
    @JoinColumn(name = "id_estudiante", nullable = false)
    private Estudiante estudiante;

    // A que pasantia esta aplicando?
    @ManyToOne
    @JoinColumn(name = "id_pasantia", nullable = false)
    private Pasantia pasantia;

    // --- Metodo Automatico ---

    // @PrePersist: Este metodo se ejecuta automaticamente justo ANTES de que
    // JPA guarde un objeto nuevo de tipo Aplicacion en la base de datos.
    @PrePersist
    protected void onCreate() {
        // Asignamos la fecha y hora actual del sistema.
        this.fechaAplicacion = LocalDateTime.now();
        // Por defecto, toda nueva aplicacion nace en estado PENDIENTE.
        if (this.estadoUniversidad == null) {
            this.estadoUniversidad = EstadoAplicacion.PENDIENTE;
        }
    }
}