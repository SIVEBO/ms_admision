package com.sivebo.ms_admision.controller;

import com.sivebo.ms_admision.dto.AdmisionRequest;
import com.sivebo.ms_admision.dto.AdmisionResponse;
import com.sivebo.ms_admision.service.AdmisionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/admisiones")
@RequiredArgsConstructor
public class AdmisionController {

    private final AdmisionService admisionService;

    @PostMapping
    public ResponseEntity<AdmisionResponse> crear(@Valid @RequestBody AdmisionRequest request) {
        log.info("[ms_admision] POST /admisiones - clienteId={}", request.getClienteId());
        return ResponseEntity.status(HttpStatus.CREATED).body(admisionService.registrar(request));
    }

    @GetMapping
    public ResponseEntity<List<AdmisionResponse>> listar() {
        log.info("[ms_admision] GET /admisiones");
        return ResponseEntity.ok(admisionService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AdmisionResponse> buscarPorId(@PathVariable Long id) {
        log.info("[ms_admision] GET /admisiones/{}", id);
        return ResponseEntity.ok(admisionService.buscarPorId(id));
    }
}
