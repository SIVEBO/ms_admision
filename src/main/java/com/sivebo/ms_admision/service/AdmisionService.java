package com.sivebo.ms_admision.service;

import com.sivebo.ms_admision.dto.AdmisionRequest;
import com.sivebo.ms_admision.dto.AdmisionResponse;
import com.sivebo.ms_admision.model.Admision;
import com.sivebo.ms_admision.model.EstadoTracking;
import com.sivebo.ms_admision.model.Paquete;
import com.sivebo.ms_admision.repository.AdmisionRepository;
import com.sivebo.ms_admision.repository.PaqueteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdmisionService {

    private final AdmisionRepository admisionRepository;
    private final PaqueteRepository paqueteRepository;
    private final WebClient webClient;

    @Value("${ms.clientes.url}")
    private String msClientesUrl;

    @Transactional
    public AdmisionResponse registrar(AdmisionRequest request) {
        log.info("[ms_admision] Iniciando registro de admisión para clienteId={}", request.getClienteId());

        validarClienteExiste(request.getClienteId());

        Paquete paquete = paqueteRepository.save(Paquete.builder()
                .descripcion(request.getDescripcionPaquete())
                .peso(request.getPeso())
                .dimensiones(request.getDimensiones())
                .estado(EstadoTracking.INGRESADO)
                .clienteId(request.getClienteId())
                .build());
        log.info("[ms_admision] Paquete creado con id={}", paquete.getId());

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

        log.info("[ms_admision] Admisión registrada id={}, tracking={}", admision.getId(), codigoTracking);
        return toResponse(admision);
    }

    @Transactional(readOnly = true)
    public List<AdmisionResponse> listar() {
        log.info("[ms_admision] Listando todas las admisiones");
        List<AdmisionResponse> lista = admisionRepository.findAll().stream()
                .map(this::toResponse).collect(Collectors.toList());
        log.info("[ms_admision] Total admisiones encontradas: {}", lista.size());
        return lista;
    }

    @Transactional(readOnly = true)
    public AdmisionResponse buscarPorId(Long id) {
        log.info("[ms_admision] Buscando admisión con id={}", id);
        Admision admision = admisionRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("[ms_admision] Admisión no encontrada id={}", id);
                    return new RuntimeException("Admisión no encontrada con ID: " + id);
                });
        return toResponse(admision);
    }

    private void validarClienteExiste(Long clienteId) {
        log.info("[ms_admision] Consultando ms_clientes para validar clienteId={}", clienteId);
        try {
            webClient.get()
                    .uri(msClientesUrl + "/clientes/" + clienteId)
                    .retrieve()
                    .bodyToMono(Object.class)
                    .block();
            log.info("[ms_admision] Cliente id={} validado correctamente en ms_clientes", clienteId);
        } catch (WebClientResponseException.NotFound e) {
            log.error("[ms_admision] Cliente id={} no existe en ms_clientes", clienteId);
            throw new RuntimeException("El cliente con ID " + clienteId + " no existe en el sistema");
        } catch (Exception e) {
            log.error("[ms_admision] Error al comunicarse con ms_clientes: {}", e.getMessage());
            throw new RuntimeException("No se pudo validar el cliente. ms_clientes no disponible");
        }
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
