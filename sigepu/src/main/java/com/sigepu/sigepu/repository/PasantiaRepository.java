package com.sigepu.sigepu.repository;

import com.sigepu.sigepu.entity.Pasantia;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PasantiaRepository extends JpaRepository<Pasantia, Long> {
    // Para la empresa (ve todo)
    List<Pasantia> findByEmpresa_Id(Long idEmpresa);
    
    // Para el estudiante (SOLO VE LAS ACTIVAS)
    List<Pasantia> findByActivaTrue(); 
}