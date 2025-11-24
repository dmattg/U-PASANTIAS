package com.sigepu.sigepu.repository;

import com.sigepu.sigepu.entity.Aplicacion;
import com.sigepu.sigepu.entity.Empresa; // Nuevo import
import com.sigepu.sigepu.entity.Estudiante;
import com.sigepu.sigepu.entity.Pasantia;
import com.sigepu.sigepu.enums.EstadoAplicacion;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AplicacionRepository extends JpaRepository<Aplicacion, Long> {

    List<Aplicacion> findByEstudiante_Id(Long idEstudiante);
    List<Aplicacion> findByEstadoUniversidad(EstadoAplicacion estado);
    List<Aplicacion> findByPasantia_Id(Long idPasantia);
    
    // Buscar aplicaciones aprobadas para una empresa específica
    List<Aplicacion> findByPasantia_Empresa_IdAndEstadoUniversidad(Long idEmpresa, EstadoAplicacion estado);

    // VALIDACIONES
    
    // 1. Verificar si ya existe postulación a una pasantía específica
    boolean existsByEstudianteAndPasantia(Estudiante estudiante, Pasantia pasantia);

    // 2. NUEVO: Verificar si ya existe postulación a CUALQUIER pasantía de una EMPRESA específica
    // La sintaxis "Pasantia_Empresa" le dice a Spring que navegue la relación.
    boolean existsByEstudianteAndPasantia_Empresa(Estudiante estudiante, Empresa empresa);
}