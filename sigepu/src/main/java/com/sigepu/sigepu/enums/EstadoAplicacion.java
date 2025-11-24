package com.sigepu.sigepu.enums;

public enum EstadoAplicacion {
    PENDIENTE,             // Esperando revision inicial
    APROBADO,              // Aprobado por la U
    RECHAZADO,             // Rechazado por la U
    SOLICITUD_CANCELACION, // Estudiante pide cancelar
    CANCELADO,              // Cancelacion confirmada por la U
    CONTRATADO,
    NO_SELECCIONADO
}