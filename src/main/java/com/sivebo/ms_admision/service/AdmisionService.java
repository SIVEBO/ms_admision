package com.sivebo.ms_admision.service;

import com.sivebo.ms_admision.dto.AdmisionRequest;
import com.sivebo.ms_admision.dto.AdmisionResponse;
import com.sivebo.ms_admision.model.Admision;
import com.sivebo.ms_admision.model.EstadoTracking;
import com.sivebo.ms_admision.model.Paquete;
import com.sivebo.ms_admision.repository.AdmisionRepository;
import com.sivebo.ms_admision.repository.PaqueteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdmisionService {

    private final AdmisionRepository admisionRepository;
    private final PaqueteRepository paqueteRepository;

    @Transactional
    public AdmisionResponse registrar(AdmisionRequest request) {
        Paquete paquete = paqueteRepository.save(Paquete.builder()
                .descripcion(request.getDescripcionPaquete())
                .peso(request.getPeso())
                .dimensiones(request.getDimensiones())
                .estado(EstadoTracking.INGRESADO)
                .clienteId(request.getClienteId())
                .build());

        String codigoTracking = "SIVEBO-" + UUID.randomUUID()
                .toString().replace("-", "").substring(0, 8).toUpperCase();

        Admision admision = admisionRepository.save(Admision.builder()
                .paquete(paquete)
                .clienteId(request.getClienteId())
                .direccionDestino(request.getDireccionDestino())
                .fechaIngreso(LocalDateTime.now())
                .codigoTracking(codigoTracking)
                .estadoActual(EstadoTracking.INGRESADO)
                .build());

        return toResponse(admision);
    }

    @Transactional(readOnly = true)
    public List<AdmisionResponse> listar() {
        return admisionRepository.findAll().stream()
                .map(this::toResponse).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public AdmisionResponse buscarPorId(Long id) {
        return toResponse(admisionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Admisión no encontrada con ID: " + id)));
    }

    private AdmisionResponse toResponse(Admision a) {
        return AdmisionResponse.builder()
                .id(a.getId())
                .clienteId(a.getClienteId())
                .paqueteId(a.getPaquete().getId())
                .descripcionPaquete(a.getPaquete().getDescripcion())
                .peso(a.getPaquete().getPeso())
                .dimensiones(a.getPaquete().getDimensiones())
                .direccionDestino(a.getDireccionDestino())
                .codigoTracking(a.getCodigoTracking())
                .estadoActual(a.getEstadoActual())
                .fechaIngreso(a.getFechaIngreso())
                .build();
    }
}
