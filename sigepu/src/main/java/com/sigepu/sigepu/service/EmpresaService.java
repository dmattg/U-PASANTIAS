package com.sigepu.sigepu.service;

import com.sigepu.sigepu.dto.EmpresaRequest;
import com.sigepu.sigepu.entity.Empresa;
import com.sigepu.sigepu.entity.Usuario;
import com.sigepu.sigepu.repository.EmpresaRepository;
import com.sigepu.sigepu.repository.UsuarioRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmpresaService {

    @Autowired
    private EmpresaRepository empresaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    // Crear una nueva empresa
    public Empresa crearEmpresa(EmpresaRequest request) {
        // 1. Buscar al usuario gestor para asegurarnos que existe
        Usuario gestor = usuarioRepository.findById(request.getIdUsuarioGestor())
                .orElseThrow(() -> new RuntimeException("Usuario gestor no encontrado"));

        // 2. Crear la entidad Empresa
        Empresa empresa = new Empresa();
        empresa.setNombre(request.getNombre());
        empresa.setUsuarioGestor(gestor);

        // 3. Guardar
        return empresaRepository.save(empresa);
    }

    // Listar todas las empresas (Para ver que funciona)
    public List<Empresa> listarTodas() {
        return empresaRepository.findAll();
    }
    
    public Optional<Empresa> obtenerPorUsuario(Long idUsuario) {
        return empresaRepository.findByUsuarioGestor_Id(idUsuario);
    }
}