package com.sivebo.ms_admision.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.sivebo.ms_admision.dto.request.AdmisionRequestDTO;
import com.sivebo.ms_admision.dto.request.CrearGuiaRequestDTO;
import com.sivebo.ms_admision.dto.response.AdmisionResponseDTO;
import com.sivebo.ms_admision.exception.EntityNotFoundException;
import com.sivebo.ms_admision.model.Admision;
import com.sivebo.ms_admision.model.TipoCarga;
import com.sivebo.ms_admision.repository.AdmisionRepository;
import com.sivebo.ms_admision.repository.TipoCargaRepository;
import com.sivebo.ms_admision.utils.MapToDTO;
import com.sivebo.ms_admision.utils.WebClientUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdmisionService extends MapToDTO {

        private final AdmisionRepository admisionRepository;
        private final TipoCargaRepository tipoCargaRepository;
        private final WebClientUtil webClientUtil;
        private final WebClient clientesWebClient;
        private final WebClient sucursalesWebClient;
        private final WebClient authWebClient;
        private final WebClient trackingWebClient;

        // RF-14: registrar admision con remitente y destinatario
        public AdmisionResponseDTO registrar(AdmisionRequestDTO dto) {

                // SCRUM-62: validar remitente y destinatario contra ms_clientes
                webClientUtil.validateExists(dto.getIdClienteRem(), "Cliente remitente", "/api/v1/clientes/{id}",
                                clientesWebClient);
                webClientUtil.validateExists(dto.getIdClienteDest(), "Cliente destinatario", "/api/v1/clientes/{id}",
                                clientesWebClient);

                // SCRUM-63: validar sucursal origen y destino contra ms_sucursales
                webClientUtil.validateExistsByQueryParam(dto.getIdSucursalOrigen(), "Sucursal de origen",
                                "/api/v1/sucursales/buscar", "id", sucursalesWebClient);
                webClientUtil.validateExistsByQueryParam(dto.getIdSucursalDest(), "Sucursal de destino",
                                "/api/v1/sucursales/buscar", "id", sucursalesWebClient);

                // SCRUM-64: validar usuario que registra la admision contra ms_auth
                webClientUtil.validateExists(dto.getIdUsuarioReg(), "Usuario registrador", "/api/v1/usuarios/{id}",
                                authWebClient);

                TipoCarga tipoCarga = tipoCargaRepository.findByNombreTipo(dto.getNombreTipoCarga())
                                .orElseThrow(() -> new EntityNotFoundException(
                                                "Tipo de carga no encontrado: " + dto.getNombreTipoCarga()));

                Admision admision = new Admision(
                                null,
                                dto.getIdClienteRem(),
                                dto.getIdClienteDest(),
                                dto.getIdSucursalOrigen(),
                                dto.getIdSucursalDest(),
                                tipoCarga,
                                dto.getPesoKg(),
                                LocalDateTime.now(),
                                dto.getIdUsuarioReg());

                Admision guardada = admisionRepository.save(admision);
                log.info(">>> Admisión registrada id={}, remitente={}, destinatario={}",
                                guardada.getId(), dto.getIdClienteRem(), dto.getIdClienteDest());

                // SCRUM-65 (RF-16): disparar creacion de guia en ms_tracking
                // codigoTracking: exactamente 12 chars alfanumerico en mayusculas (requerido por ms_tracking)
                String codigoTracking = java.util.UUID.randomUUID()
                                .toString().replace("-", "").substring(0, 12).toUpperCase();

                webClientUtil.postCrear("/api/v1/guias",
                                new CrearGuiaRequestDTO(codigoTracking, guardada.getId()),
                                "Guía de despacho", trackingWebClient);

                log.info(">>> Guía disparada en ms_tracking, codigoTracking={}", codigoTracking);

                return mapAdmisionToDTO(guardada);
        }

        public AdmisionResponseDTO getById(Long id) {
                return admisionRepository.findById(id)
                                .map(this::mapAdmisionToDTO)
                                .orElseThrow(() -> new EntityNotFoundException("Admisión no encontrada con id: " + id));
        }

        // RF-17: consultar admisiones por sucursal, fecha y tipo de carga
        public List<AdmisionResponseDTO> buscarConFiltros(Long idSucursal, String nombreTipo,
                        LocalDateTime desde, LocalDateTime hasta) {
                log.info(">>> Consultando admisiones sucursal={}, tipo={}, desde={}, hasta={}",
                                idSucursal, nombreTipo, desde, hasta);
                return admisionRepository.buscarConFiltros(idSucursal, nombreTipo, desde, hasta)
                                .stream()
                                .map(this::mapAdmisionToDTO)
                                .collect(Collectors.toList());
        }
}
