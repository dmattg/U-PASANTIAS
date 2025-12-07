package com.sigepu.sigepu.service;

import com.sigepu.sigepu.dto.AplicacionRequest;
import com.sigepu.sigepu.entity.Aplicacion;
import com.sigepu.sigepu.entity.Empresa;
import com.sigepu.sigepu.entity.Estudiante;
import com.sigepu.sigepu.entity.Pasantia;
import com.sigepu.sigepu.enums.EstadoAplicacion;
import com.sigepu.sigepu.repository.AplicacionRepository;
import com.sigepu.sigepu.repository.EstudianteRepository;
import com.sigepu.sigepu.repository.PasantiaRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // Importante para la limpieza masiva

@Service
public class AplicacionService {

    @Autowired private AplicacionRepository aplicacionRepository;
    @Autowired private EstudianteRepository estudianteRepository;
    @Autowired private PasantiaRepository pasantiaRepository;

    // ... (Métodos crearAplicacion, solicitarCancelacion, listarPendientes... SE MANTIENEN IGUAL)
    // COPIA TUS MÉTODOS ANTERIORES AQUÍ O MANTENLOS SI NO CAMBIARON
    
    // VOY A PONER LOS METODOS CLAVE QUE NO CAMBIAN RESUMIDOS PARA QUE NO SE PIERDA EL CONTEXTO
    public Aplicacion crearAplicacion(AplicacionRequest request) {
        // ... (Tu código actual de crear, con la validación de empresa) ...
        // (Pego el bloque esencial para que compile, pero usa el que ya tenías validado)
        Estudiante estudiante = estudianteRepository.findById(request.getIdEstudiante()).orElseThrow();
        Pasantia pasantia = pasantiaRepository.findById(request.getIdPasantia()).orElseThrow();
        Empresa empresaOwner = pasantia.getEmpresa();
        if (aplicacionRepository.existsByEstudianteAndPasantia_Empresa(estudiante, empresaOwner)) {
            throw new RuntimeException("Política de Empresa: Ya tienes una solicitud activa con " + empresaOwner.getNombre());
        }
        if (!estudiante.getCarrera().getId().equals(pasantia.getCarreraRequerida().getId())) throw new RuntimeException("Carrera no coincide");
        Aplicacion app = new Aplicacion();
        app.setEstudiante(estudiante);
        app.setPasantia(pasantia);
        return aplicacionRepository.save(app);
    }

    public Aplicacion solicitarCancelacion(Long id) {
        Aplicacion app = aplicacionRepository.findById(id).orElseThrow();
        app.setEstadoUniversidad(EstadoAplicacion.SOLICITUD_CANCELACION);
        return aplicacionRepository.save(app);
    }
    
    public List<Aplicacion> listarPendientesGestion() { return aplicacionRepository.findAll(); }
    public List<Aplicacion> listarPorEstudiante(Long id) { return aplicacionRepository.findByEstudiante_Id(id); }
    public List<Aplicacion> listarAprobadosPorEmpresa(Long idEmpresa) {
        return aplicacionRepository.findByPasantia_Empresa_IdAndEstadoUniversidad(idEmpresa, EstadoAplicacion.APROBADO);
    }

    // --- AQUÍ ESTÁ EL CAMBIO IMPORTANTE ---
    
    @Transactional // Asegura que si algo falla, no se borre nada a medias
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
                // 1. Contratamos esta aplicación
                app.setEstadoUniversidad(EstadoAplicacion.CONTRATADO);
                
                // 2. BUSCAMOS Y CANCELAMOS TODAS LAS DEMÁS DE ESTE ESTUDIANTE
                List<Aplicacion> otrasApps = aplicacionRepository.findByEstudiante_Id(app.getEstudiante().getId());
                for (Aplicacion otra : otrasApps) {
                    // Si no es la que acabamos de contratar y no está finalizada/rechazada
                    if (!otra.getId().equals(app.getId()) && 
                        otra.getEstadoUniversidad() != EstadoAplicacion.RECHAZADO &&
                        otra.getEstadoUniversidad() != EstadoAplicacion.CANCELADO &&
                        otra.getEstadoUniversidad() != EstadoAplicacion.NO_SELECCIONADO) {
                        
                        otra.setEstadoUniversidad(EstadoAplicacion.CANCELADO);
                        aplicacionRepository.save(otra);
                    }
                }
                break;
                
            case "DESCARTAR":
                app.setEstadoUniversidad(EstadoAplicacion.NO_SELECCIONADO);
                break;
                
            case "FINALIZAR": // Nuevo caso para la Universidad
                app.setEstadoUniversidad(EstadoAplicacion.FINALIZADO);
                break;
                
            default:
                throw new RuntimeException("Accion no valida");
        }
        return aplicacionRepository.save(app);
    }
}