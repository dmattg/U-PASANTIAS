package com.sigepu.sigepu.controller;

import com.sigepu.sigepu.dto.PerfilRequest;
import com.sigepu.sigepu.service.PerfilService;
import java.io.IOException;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/perfil")
public class PerfilController {

    @Autowired private PerfilService perfilService;

    @PostMapping("/{idUsuario}/foto")
    public ResponseEntity<?> subirFoto(@PathVariable Long idUsuario, @RequestParam("file") MultipartFile file) {
        try {
            String nombreFoto = perfilService.subirFoto(idUsuario, file);
            // Devolvemos la URL pública de la imagen
            return ResponseEntity.ok(Map.of("url", "/imagenes/" + nombreFoto));
        } catch (IOException e) {
            return ResponseEntity.internalServerError().body("Error al subir imagen");
        }
    }
    
    @GetMapping("/{idUsuario}/foto")
    public ResponseEntity<?> obtenerFoto(@PathVariable Long idUsuario) {
        String foto = perfilService.obtenerFoto(idUsuario);
        if(foto == null) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(Map.of("url", "/imagenes/" + foto));
    }

    @PutMapping("/{idUsuario}")
    public ResponseEntity<?> actualizarDatos(@PathVariable Long idUsuario, @RequestBody PerfilRequest request) {
        perfilService.actualizarDatos(idUsuario, request);
        return ResponseEntity.ok("Datos actualizados");
    }
}