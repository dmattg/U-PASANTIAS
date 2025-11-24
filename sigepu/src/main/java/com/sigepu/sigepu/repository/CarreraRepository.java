package com.sigepu.sigepu.repository;

import com.sigepu.sigepu.entity.Carrera;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
// Hereda de JpaRepository indicando que maneja entidades 'Carrera' con ID tipo 'Long'
public interface CarreraRepository extends JpaRepository<Carrera, Long> {
    // No necesitamos métodos extra por ahora. Con los básicos basta.
}