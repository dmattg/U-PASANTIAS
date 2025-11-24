package com.sigepu.sigepu.controller;

import com.sigepu.sigepu.dto.EmpresaRequest;
import com.sigepu.sigepu.entity.Empresa;
import com.sigepu.sigepu.service.EmpresaService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/empresas")
public class EmpresaController {

    @Autowired
    private EmpresaService empresaService;

    @PostMapping
    public ResponseEntity<?> crear(@RequestBody EmpresaRequest request) {
        try {
            Empresa nueva = empresaService.crearEmpresa(request);
            return ResponseEntity.ok(nueva);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<?> obtenerPorUsuario(@PathVariable Long idUsuario) {
        return empresaService.obtenerPorUsuario(idUsuario)
                .map(empresa -> ResponseEntity.ok(empresa))
                .orElse(ResponseEntity.noContent().build());
    }
}