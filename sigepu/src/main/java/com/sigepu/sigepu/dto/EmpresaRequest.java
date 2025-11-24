package com.sigepu.sigepu.dto;

import lombok.Data;

@Data
public class EmpresaRequest {
    private String nombre;
    private Long idUsuarioGestor; // El ID del usuario que crea la empresa
}