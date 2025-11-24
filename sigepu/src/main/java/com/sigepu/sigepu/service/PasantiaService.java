package com.sigepu.sigepu.service;

import com.sigepu.sigepu.dto.PasantiaRequest;
import com.sigepu.sigepu.entity.Carrera;
import com.sigepu.sigepu.entity.Empresa;
import com.sigepu.sigepu.entity.Pasantia;
import com.sigepu.sigepu.repository.CarreraRepository;
import com.sigepu.sigepu.repository.EmpresaRepository;
import com.sigepu.sigepu.repository.PasantiaRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PasantiaService {

    @Autowired private PasantiaRepository pasantiaRepository;
    @Autowired private EmpresaRepository empresaRepository;
    @Autowired private CarreraRepository carreraRepository;

    public Pasantia crearPasantia(PasantiaRequest request) {
        Empresa empresa = empresaRepository.findById(request.getIdEmpresa())
                .orElseThrow(() -> new RuntimeException("Empresa no encontrada"));
        Carrera carrera = carreraRepository.findById(request.getIdCarreraRequerida())
                .orElseThrow(() -> new RuntimeException("Carrera no encontrada"));

        Pasantia pasantia = new Pasantia();
        pasantia.setTitulo(request.getTitulo());
        pasantia.setDescripcion(request.getDescripcion());
        pasantia.setEmpresa(empresa);
        pasantia.setCarreraRequerida(carrera);
        pasantia.setActiva(true); // Nace activa

        return pasantiaRepository.save(pasantia);
    }

    // EDITAR PASANTIA
    public Pasantia editarPasantia(Long id, PasantiaRequest request) {
        Pasantia pasantia = pasantiaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pasantía no encontrada"));
        
        pasantia.setTitulo(request.getTitulo());
        pasantia.setDescripcion(request.getDescripcion());
        // Opcional: permitir cambiar carrera si es necesario
        
        return pasantiaRepository.save(pasantia);
    }

    // ACTIVAR / DESACTIVAR
    public Pasantia alternarEstado(Long id) {
        Pasantia pasantia = pasantiaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pasantía no encontrada"));
        pasantia.setActiva(!pasantia.isActiva()); // Invierte el valor
        return pasantiaRepository.save(pasantia);
    }

    // Para Estudiantes: SOLO ACTIVAS
    public List<Pasantia> listarActivas() {
        return pasantiaRepository.findByActivaTrue();
    }

    // Para Empresa: TODAS (Activas e Inactivas)
    public List<Pasantia> listarPorEmpresa(Long idEmpresa) {
        return pasantiaRepository.findByEmpresa_Id(idEmpresa);
    }
}