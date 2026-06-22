package com.sivebo.ms_admision.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CrearGuiaRequestDTO {

        String codigoTracking;
        Long idAdmision;
}
