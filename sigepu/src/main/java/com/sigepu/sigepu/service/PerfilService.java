package com.sigepu.sigepu.service;

import com.sigepu.sigepu.dto.PerfilRequest;
import com.sigepu.sigepu.entity.*;
import com.sigepu.sigepu.enums.Rol;
import com.sigepu.sigepu.repository.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class PerfilService {

    @Autowired private UsuarioRepository usuarioRepository;
    @Autowired private EstudianteRepository estudianteRepository;
    @Autowired private EmpresaRepository empresaRepository;
    @Autowired private PasswordEncoder passwordEncoder;

    // Ruta física donde se guardarán las fotos
    private final Path rootLocation = Paths.get("uploads");

    // 1. SUBIR FOTO
    public String subirFoto(Long idUsuario, MultipartFile file) throws IOException {
        // Crear directorio si no existe
        if (!Files.exists(rootLocation)) Files.createDirectories(rootLocation);

        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Generar nombre único para evitar duplicados (ej: asd87-foto.jpg)
        String filename = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
        
        // Guardar archivo en disco
        Files.copy(file.getInputStream(), rootLocation.resolve(filename), StandardCopyOption.REPLACE_EXISTING);

        // Guardar nombre en BD
        usuario.setFoto(filename);
        usuarioRepository.save(usuario);

        return filename;
    }

    // 2. ACTUALIZAR DATOS
    public void actualizarDatos(Long idUsuario, PerfilRequest request) {
        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Cambio de contraseña (si viene en el request)
        if (request.getPassword() != null && !request.getPassword().isEmpty()) {
            usuario.setPassword(passwordEncoder.encode(request.getPassword()));
            usuarioRepository.save(usuario);
        }

        // Actualizar datos específicos según el rol
        if (usuario.getRol() == Rol.ESTUDIANTE) {
            Estudiante est = estudianteRepository.findByUsuario_Id(idUsuario).orElse(null);
            if (est != null) {
                if(request.getNombre() != null) est.setNombre(request.getNombre());
                if(request.getApellido() != null) est.setApellido(request.getApellido());
                estudianteRepository.save(est);
            }
        } else if (usuario.getRol() == Rol.GESTOR_EMPRESARIAL) {
            Empresa emp = empresaRepository.findByUsuarioGestor_Id(idUsuario).orElse(null);
            if (emp != null) {
                if(request.getNombre() != null) emp.setNombre(request.getNombre());
                // Aquí podrías agregar descripción a la entidad Empresa si quisieras
                empresaRepository.save(emp);
            }
        }
    }
    
    // Obtener la foto actual
    public String obtenerFoto(Long idUsuario) {
        return usuarioRepository.findById(idUsuario).map(Usuario::getFoto).orElse(null);
    }
}