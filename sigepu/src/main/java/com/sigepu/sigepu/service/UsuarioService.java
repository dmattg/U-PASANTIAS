package com.sigepu.sigepu.service;

import com.sigepu.sigepu.entity.Usuario;
import com.sigepu.sigepu.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Registra un nuevo usuario en la base de datos
    @Transactional
    public Usuario registrarUsuario(Usuario usuario) {
        // Verifica si el email ya existe
        if (usuarioRepository.findByEmail(usuario.getEmail()).isPresent()) {
            throw new RuntimeException("El email ya esta registrado");
        }

        // Encripta la contraseña antes de guardar
        String passEncriptada = passwordEncoder.encode(usuario.getPassword());
        usuario.setPassword(passEncriptada);

        // Guarda el usuario
        return usuarioRepository.save(usuario);
    }
    
    // Metodo para validar credenciales (Login)
    public Usuario login(String email, String password) {
        // 1. Buscar al usuario por email
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // 2. Verificar si la contrasena coincide con la encriptada
        // El metodo .matches() compara el texto plano con el hash de la BD
        if (!passwordEncoder.matches(password, usuario.getPassword())) {
            throw new RuntimeException("Contrasena incorrecta");
        }

        // 3. Si todo esta bien, devolvemos el usuario
        return usuario;
    }
}