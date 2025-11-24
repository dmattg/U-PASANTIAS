package com.sigepu.sigepu.dto;

import lombok.Data;

// Clase simple para transportar los datos del login
@Data
public class LoginRequest {
    private String email;
    private String password;
}