package com.sigepu.sigepu.controller;

import com.sigepu.sigepu.dto.PasantiaRequest;
import com.sigepu.sigepu.entity.Pasantia;
import com.sigepu.sigepu.service.PasantiaService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/pasantias")
public class PasantiaController {

    @Autowired private PasantiaService pasantiaService;

    @PostMapping
    public ResponseEntity<?> crear(@RequestBody PasantiaRequest request) {
        try { return ResponseEntity.ok(pasantiaService.crearPasantia(request)); } 
        catch (RuntimeException e) { return ResponseEntity.badRequest().body(e.getMessage()); }
    }

    // EDITAR
    @PutMapping("/{id}")
    public ResponseEntity<?> editar(@PathVariable Long id, @RequestBody PasantiaRequest request) {
        try { return ResponseEntity.ok(pasantiaService.editarPasantia(id, request)); } 
        catch (RuntimeException e) { return ResponseEntity.badRequest().body(e.getMessage()); }
    }

    // TOGGLE ESTADO
    @PutMapping("/{id}/toggle")
    public ResponseEntity<?> toggle(@PathVariable Long id) {
        try { return ResponseEntity.ok(pasantiaService.alternarEstado(id)); } 
        catch (RuntimeException e) { return ResponseEntity.badRequest().body(e.getMessage()); }
    }

    // Estudiantes ven solo activas
    @GetMapping
    public List<Pasantia> listar() { return pasantiaService.listarActivas(); }

    // Empresas ven todas
    @GetMapping("/empresa/{idEmpresa}")
    public List<Pasantia> listarPorEmpresa(@PathVariable Long idEmpresa) {
        return pasantiaService.listarPorEmpresa(idEmpresa);
    }
}