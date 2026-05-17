package com.sivebo.ms_admision.dto;

import com.sivebo.ms_admision.model.EstadoTracking;
import lombok.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdmisionResponse {
    private Long id;
    private Long clienteId;
    private Long paqueteId;
    private String descripcionPaquete;
    private Double peso;
    private String dimensiones;
    private String direccionDestino;
    private String codigoTracking;
    private EstadoTracking estadoActual;
    private LocalDateTime fechaIngreso;
}
