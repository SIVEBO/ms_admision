package com.sivebo.ms_admision.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO de salida hacia ms_tracking al confirmar una admision (RF-16).
 * Contrato real: POST /api/v1/guias
 * codigoTracking debe tener exactamente 12 caracteres (validacion en ms_tracking).
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CrearGuiaRequestDTO {

        String codigoTracking;
        Long idAdmision;
}
