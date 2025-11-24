package com.sigepu.sigepu.controller;

import com.sigepu.sigepu.dto.AplicacionRequest;
import com.sigepu.sigepu.entity.Aplicacion;
import com.sigepu.sigepu.service.AplicacionService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/aplicaciones")
public class AplicacionController {

    @Autowired
    private AplicacionService aplicacionService;

    @PostMapping("/aplicar")
    public ResponseEntity<?> aplicar(@RequestBody AplicacionRequest request) {
        try {
            Aplicacion nueva = aplicacionService.crearAplicacion(request);
            return ResponseEntity.ok(nueva);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Estudiante solicita cancelar
    @PutMapping("/{id}/solicitar-cancelacion")
    public ResponseEntity<?> solicitarCancelacion(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(aplicacionService.solicitarCancelacion(id));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/pendientes")
    public List<Aplicacion> verTodas() {
        return aplicacionService.listarPendientesGestion();
    }

    @GetMapping("/estudiante/{idEstudiante}")
    public List<Aplicacion> verMisAplicaciones(@PathVariable Long idEstudiante) {
        return aplicacionService.listarPorEstudiante(idEstudiante);
    }

    @PutMapping("/{id}/estado")
    public ResponseEntity<?> cambiarEstado(@PathVariable Long id, @RequestParam String accion) {
        try {
            return ResponseEntity.ok(aplicacionService.gestionarAplicacion(id, accion));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    // Empresa: Ver candidatos aprobados
    @GetMapping("/empresa/{idEmpresa}/aprobados")
    public List<Aplicacion> verAprobadosEmpresa(@PathVariable Long idEmpresa) {
        return aplicacionService.listarAprobadosPorEmpresa(idEmpresa);
    }
}