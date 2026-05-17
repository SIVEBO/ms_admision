package com.sivebo.ms_admision.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdmisionRequest {

    @NotNull(message = "El clienteId es obligatorio")
    private Long clienteId;

    @NotBlank(message = "La descripción del paquete es obligatoria")
    private String descripcionPaquete;

    @Positive(message = "El peso debe ser positivo")
    private Double peso;

    private String dimensiones;

    @NotBlank(message = "La dirección destino es obligatoria")
    private String direccionDestino;
}
