package com.sivebo.ms_admision.utils;

import com.sivebo.ms_admision.dto.response.AdmisionResponseDTO;
import com.sivebo.ms_admision.model.Admision;

public class MapToDTO {

        protected AdmisionResponseDTO mapAdmisionToDTO(Admision admision) {
                return new AdmisionResponseDTO(
                                admision.getId(),
                                admision.getIdClienteRem(),
                                admision.getIdClienteDest(),
                                admision.getIdSucursalOrigen(),
                                admision.getIdSucursalDest(),
                                admision.getTipoCarga().getNombreTipo(),
                                admision.getPesoKg(),
                                admision.getFechaCreacion(),
                                admision.getIdUsuarioReg());
        }
}
