package com.sigepu.sigepu.dto;

import lombok.Data;

@Data
public class PerfilRequest {
    private String nombre;    // Para Estudiante/Empresa
    private String apellido;  // Para Estudiante
    private String descripcion; // Para Empresa
    private String password;  // Para Todos (Opcional)
}