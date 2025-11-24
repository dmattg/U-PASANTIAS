package com.sigepu.sigepu.service;

import com.sigepu.sigepu.dto.AplicacionRequest;
import com.sigepu.sigepu.entity.Aplicacion;
import com.sigepu.sigepu.entity.Empresa; // Importante para la nueva validacion
import com.sigepu.sigepu.entity.Estudiante;
import com.sigepu.sigepu.entity.Pasantia;
import com.sigepu.sigepu.enums.EstadoAplicacion;
import com.sigepu.sigepu.repository.AplicacionRepository;
import com.sigepu.sigepu.repository.EstudianteRepository;
import com.sigepu.sigepu.repository.PasantiaRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AplicacionService {

    @Autowired
    private AplicacionRepository aplicacionRepository;
    @Autowired
    private EstudianteRepository estudianteRepository;
    @Autowired
    private PasantiaRepository pasantiaRepository;

    // 1. Estudiante aplica a una pasantia
    public Aplicacion crearAplicacion(AplicacionRequest request) {
        Estudiante estudiante = estudianteRepository.findById(request.getIdEstudiante())
                .orElseThrow(() -> new RuntimeException("Estudiante no encontrado"));
        
        Pasantia pasantia = pasantiaRepository.findById(request.getIdPasantia())
                .orElseThrow(() -> new RuntimeException("Pasantia no encontrada"));

        // --- NUEVA VALIDACIÓN DE EMPRESA ---
        // Obtenemos la empresa dueña de la pasantía
        Empresa empresaOwner = pasantia.getEmpresa();

        // Verificamos si el estudiante ya tiene ALGUNA aplicación con esa empresa
        // Nota: Asegurate que tu AplicacionRepository tenga el metodo 'existsByEstudianteAndPasantia_Empresa'
        if (aplicacionRepository.existsByEstudianteAndPasantia_Empresa(estudiante, empresaOwner)) {
            throw new RuntimeException("Política de Empresa: Ya tienes una solicitud activa con " + empresaOwner.getNombre() + ". No puedes aplicar a más de una oferta de la misma compañía.");
        }
        // -----------------------------------

        // Validacion de carrera
        if (!estudiante.getCarrera().getId().equals(pasantia.getCarreraRequerida().getId())) {
            throw new RuntimeException("No puedes aplicar: Tu carrera no coincide con la requerida.");
        }

        Aplicacion aplicacion = new Aplicacion();
        aplicacion.setEstudiante(estudiante);
        aplicacion.setPasantia(pasantia);
        // El estado inicial 'PENDIENTE' se asigna en la Entidad (@PrePersist)
        
        return aplicacionRepository.save(aplicacion);
    }

    // 2. Solicitar Cancelación (Estudiante)
    public Aplicacion solicitarCancelacion(Long idAplicacion) {
        Aplicacion app = aplicacionRepository.findById(idAplicacion)
                .orElseThrow(() -> new RuntimeException("Aplicacion no encontrada"));
        
        // Solo se puede cancelar si no ha finalizado ya
        if (app.getEstadoUniversidad() == EstadoAplicacion.CANCELADO || 
            app.getEstadoUniversidad() == EstadoAplicacion.RECHAZADO ||
            app.getEstadoUniversidad() == EstadoAplicacion.CONTRATADO || 
            app.getEstadoUniversidad() == EstadoAplicacion.NO_SELECCIONADO) {
            throw new RuntimeException("No se puede cancelar una solicitud ya finalizada");
        }

        app.setEstadoUniversidad(EstadoAplicacion.SOLICITUD_CANCELACION);
        return aplicacionRepository.save(app);
    }

    // 3. Listar Pendientes (Gestor)
    public List<Aplicacion> listarPendientesGestion() {
        return aplicacionRepository.findAll();
    }
    
    // 4. Listar Mis Aplicaciones (Estudiante)
    public List<Aplicacion> listarPorEstudiante(Long idEstudiante) {
        return aplicacionRepository.findByEstudiante_Id(idEstudiante);
    }

    // 5. Gestionar Estados (Gestor Universitario y Empresarial)
    public Aplicacion gestionarAplicacion(Long idAplicacion, String accion) {
        Aplicacion app = aplicacionRepository.findById(idAplicacion)
                .orElseThrow(() -> new RuntimeException("Aplicacion no encontrada"));

        switch (accion.toUpperCase()) {
            case "APROBAR":
                app.setEstadoUniversidad(EstadoAplicacion.APROBADO);
                break;
            case "RECHAZAR":
                app.setEstadoUniversidad(EstadoAplicacion.RECHAZADO);
                break;
            case "CONFIRMAR_CANCELACION":
                app.setEstadoUniversidad(EstadoAplicacion.CANCELADO);
                break;
            case "CONTRATAR":
                app.setEstadoUniversidad(EstadoAplicacion.CONTRATADO);
                break;
            case "DESCARTAR":
                app.setEstadoUniversidad(EstadoAplicacion.NO_SELECCIONADO);
                break;
            default:
                throw new RuntimeException("Accion no valida");
        }
        return aplicacionRepository.save(app);
    }
    
    // 6. Listar candidatos aprobados para una empresa
    public List<Aplicacion> listarAprobadosPorEmpresa(Long idEmpresa) {
        return aplicacionRepository.findByPasantia_Empresa_IdAndEstadoUniversidad(idEmpresa, EstadoAplicacion.APROBADO);
    }
}