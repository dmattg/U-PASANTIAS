package com.sigepu.sigepu.dto;

import lombok.Data;

@Data
public class EstudianteRequest {
    private String nombre;
    private String apellido;
    private Long idCarrera; // El usuario seleccionará su carrera de una lista
}