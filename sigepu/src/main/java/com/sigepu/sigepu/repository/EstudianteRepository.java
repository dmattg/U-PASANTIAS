package com.sigepu.sigepu.repository;

import com.sigepu.sigepu.entity.Estudiante;
import java.util.Optional; // Importar Optional
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EstudianteRepository extends JpaRepository<Estudiante, Long> {
    // Método mágico: Buscar estudiante donde el campo 'usuario.id' sea igual al parametro
    Optional<Estudiante> findByUsuario_Id(Long idUsuario);
}