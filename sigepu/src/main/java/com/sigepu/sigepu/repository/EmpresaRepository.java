package com.sigepu.sigepu.repository;

import com.sigepu.sigepu.entity.Empresa;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmpresaRepository extends JpaRepository<Empresa, Long> {
    // Buscar empresa por el ID del usuario gestor (Login)
    Optional<Empresa> findByUsuarioGestor_Id(Long idUsuario);
}