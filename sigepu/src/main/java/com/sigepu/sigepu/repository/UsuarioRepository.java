package com.sigepu.sigepu.repository;

import com.sigepu.sigepu.entity.Usuario;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// @Repository: Indica a Spring que esta interfaz es un componente de acceso a datos.
@Repository
// Extendemos de JpaRepository<Tipo de Entidad, Tipo de ID>
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    // --- Métodos Mágicos (Query Methods) ---

    // Spring Data JPA es tan listo que si creamos un método siguiendo
    // la convención de nombres "findBy[NombreAtributo]", él genera el SQL automáticamente.

    // Este método servirá para buscar un usuario por su email exacto.
    // Usamos Optional<> porque puede que el usuario con ese email exista o no.
    // Será fundamental para el proceso de Login.
    Optional<Usuario> findByEmail(String email);

}