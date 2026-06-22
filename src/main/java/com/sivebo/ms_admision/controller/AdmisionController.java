package com.sivebo.ms_admision.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sivebo.ms_admision.dto.request.AdmisionRequestDTO;
import com.sivebo.ms_admision.dto.response.AdmisionResponseDTO;
import com.sivebo.ms_admision.service.AdmisionService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("api/v1/admisiones")
@RequiredArgsConstructor
@Tag(name = "Admisiones", description = "Registro de ingreso de paquetes")
public class AdmisionController {

        private final AdmisionService admisionService;

        @Operation(summary = "Registrar una admision", description = "registra remitente, destinatario, tipo de carga y peso")
        @PostMapping
        public ResponseEntity<AdmisionResponseDTO> registrar(@Valid @RequestBody AdmisionRequestDTO dto) {
                log.info(">>> POST /api/v1/admisiones - remitente={}, destinatario={}",
                                dto.getIdClienteRem(), dto.getIdClienteDest());
                return ResponseEntity.status(HttpStatus.CREATED).body(admisionService.registrar(dto));
        }

        @Operation(summary = "Obtener admision por id")
        @GetMapping("/{id}")
        public ResponseEntity<AdmisionResponseDTO> getById(@PathVariable Long id) {
                return ResponseEntity.ok(admisionService.getById(id));
        }

        @Operation(summary = "Consultar admisiones por sucursal con filtros", description = "filtra por sucursal de origen, rango de fecha y tipo de carga. Ej: ?idSucursal=1&nombreTipo=Encomienda&desde=2026-01-01T00:00:00")
        @GetMapping
        public List<AdmisionResponseDTO> buscar(
                        @RequestParam Long idSucursal,
                        @RequestParam(required = false) String nombreTipo,
                        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime desde,
                        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime hasta) {
                return admisionService.buscarConFiltros(idSucursal, nombreTipo, desde, hasta);
        }
}
