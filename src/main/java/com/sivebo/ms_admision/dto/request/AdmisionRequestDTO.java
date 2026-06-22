package com.sivebo.ms_admision.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdmisionRequestDTO {

        @NotNull(message = "El id del cliente remitente es obligatorio")
        Long idClienteRem;

        @NotNull(message = "El id del cliente destinatario es obligatorio")
        Long idClienteDest;

        @NotNull(message = "La sucursal de origen es obligatoria")
        Long idSucursalOrigen;

        @NotNull(message = "La sucursal de destino es obligatoria")
        Long idSucursalDest;

        @NotNull(message = "El tipo de carga es obligatorio")
        String nombreTipoCarga;

        @NotNull(message = "El peso es obligatorio")
        @Positive(message = "El peso debe ser mayor a 0")
        Double pesoKg;

        @NotNull(message = "El id del usuario que registra la admisión es obligatorio")
        Long idUsuarioReg;
}
