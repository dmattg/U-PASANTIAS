package com.sigepu.sigepu.service;

import com.sigepu.sigepu.dto.EstudianteRequest;
import com.sigepu.sigepu.entity.Carrera;
import com.sigepu.sigepu.entity.Estudiante;
import com.sigepu.sigepu.entity.Usuario;
import com.sigepu.sigepu.repository.CarreraRepository;
import com.sigepu.sigepu.repository.EstudianteRepository;
import com.sigepu.sigepu.repository.UsuarioRepository;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EstudianteService {

    @Autowired
    private EstudianteRepository estudianteRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private CarreraRepository carreraRepository;

    // Buscar perfil por ID de Usuario (Login)
    public Optional<Estudiante> obtenerPorIdUsuario(Long idUsuario) {
        return estudianteRepository.findByUsuario_Id(idUsuario);
    }

    // CREAR PERFIL AUTOMÁTICAMENTE VINCULADO
    public Estudiante crearPerfil(Long idUsuario, EstudianteRequest request) {
        // 1. Buscamos el usuario que ya existe (el que se acaba de loguear)
        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // 2. Buscamos la carrera
        Carrera carrera = carreraRepository.findById(request.getIdCarrera())
                .orElseThrow(() -> new RuntimeException("Carrera no encontrada"));

        // 3. Creamos el estudiante y LE ASIGNAMOS EL USUARIO AUTOMÁTICAMENTE
        Estudiante estudiante = new Estudiante();
        estudiante.setNombre(request.getNombre());
        estudiante.setApellido(request.getApellido());
        estudiante.setUsuario(usuario); // <--- ¡AQUÍ OCURRE LA MAGIA DE LA VINCULACIÓN!
        estudiante.setCarrera(carrera);

        return estudianteRepository.save(estudiante);
    }
}