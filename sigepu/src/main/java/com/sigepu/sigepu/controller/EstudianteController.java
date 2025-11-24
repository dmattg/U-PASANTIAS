package com.sigepu.sigepu.controller;

import com.sigepu.sigepu.dto.EstudianteRequest;
import com.sigepu.sigepu.entity.Estudiante;
import com.sigepu.sigepu.service.EstudianteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/estudiantes")
public class EstudianteController {

    @Autowired
    private EstudianteService estudianteService;

    // Obtener perfil
    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<?> obtenerPorUsuario(@PathVariable Long idUsuario) {
        return estudianteService.obtenerPorIdUsuario(idUsuario)
                .map(estudiante -> ResponseEntity.ok(estudiante))
                .orElse(ResponseEntity.noContent().build()); // 204 No Content si no existe
    }

    // Crear perfil vinculado al usuario
    @PostMapping("/usuario/{idUsuario}")
    public ResponseEntity<?> crearPerfil(@PathVariable Long idUsuario, @RequestBody EstudianteRequest request) {
        try {
            Estudiante nuevo = estudianteService.crearPerfil(idUsuario, request);
            return ResponseEntity.ok(nuevo);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}