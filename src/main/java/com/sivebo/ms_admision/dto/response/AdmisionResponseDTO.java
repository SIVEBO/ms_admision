package com.sivebo.ms_admision.dto.response;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdmisionResponseDTO {

        Long id;
        Long idClienteRem;
        Long idClienteDest;
        Long idSucursalOrigen;
        Long idSucursalDest;
        String nombreTipoCarga;
        Double pesoKg;
        LocalDateTime fechaCreacion;
        Long idUsuarioReg;
}
