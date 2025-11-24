package com.sigepu.sigepu.dto;

import lombok.Data;

@Data
public class PasantiaRequest {
    private String titulo;
    private String descripcion;
    private Long idEmpresa;          // Que empresa ofrece la pasantia
    private Long idCarreraRequerida; // Para que carrera es la oferta
}