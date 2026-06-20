package com.sivebo.ms_admision.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;

import com.sivebo.ms_admision.dto.request.AdmisionRequestDTO;
import com.sivebo.ms_admision.dto.response.AdmisionResponseDTO;
import com.sivebo.ms_admision.exception.EntityNotFoundException;
import com.sivebo.ms_admision.model.Admision;
import com.sivebo.ms_admision.model.TipoCarga;
import com.sivebo.ms_admision.repository.AdmisionRepository;
import com.sivebo.ms_admision.repository.TipoCargaRepository;
import com.sivebo.ms_admision.utils.WebClientUtil;

@ExtendWith(MockitoExtension.class)
class AdmisionServiceTest {

    @Mock AdmisionRepository admisionRepository;
    @Mock TipoCargaRepository tipoCargaRepository;
    @Mock WebClientUtil webClientUtil;
    @Mock WebClient clientesWebClient;
    @Mock WebClient sucursalesWebClient;
    @Mock WebClient authWebClient;
    @Mock WebClient trackingWebClient;

    @InjectMocks AdmisionService service;

    private static final TipoCarga TIPO = new TipoCarga(1L, "Encomienda", "Paquetes pequeños");

    private static final Admision ADMISION = new Admision(
            1L, 10L, 20L, 1L, 2L, TIPO, 5.0,
            LocalDateTime.of(2026, 1, 1, 10, 0), 100L);

    private AdmisionRequestDTO buildDTO() {
        return new AdmisionRequestDTO(10L, 20L, 1L, 2L, "Encomienda", 5.0, 100L);
    }

    @Test
    void getById_found_returnsDTO() {
        when(admisionRepository.findById(1L)).thenReturn(Optional.of(ADMISION));

        AdmisionResponseDTO result = service.getById(1L);

        assertEquals(1L, result.getId());
        assertEquals("Encomienda", result.getNombreTipoCarga());
        assertEquals(10L, result.getIdClienteRem());
    }

    @Test
    void getById_notFound_throwsEntityNotFoundException() {
        when(admisionRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> service.getById(99L));
    }

    @Test
    void buscarConFiltros_sinFiltros_retornaTodosDeUcursal() {
        when(admisionRepository.buscarConFiltros(1L, null, null, null)).thenReturn(List.of(ADMISION));

        List<AdmisionResponseDTO> result = service.buscarConFiltros(1L, null, null, null);

        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getId());
    }

    @Test
    void buscarConFiltros_conTipo_filtraCorrectamente() {
        when(admisionRepository.buscarConFiltros(1L, "Encomienda", null, null))
                .thenReturn(List.of(ADMISION));

        List<AdmisionResponseDTO> result = service.buscarConFiltros(1L, "Encomienda", null, null);

        assertEquals(1, result.size());
        assertEquals("Encomienda", result.get(0).getNombreTipoCarga());
    }

    @Test
    void buscarConFiltros_sinResultados_retornaListaVacia() {
        when(admisionRepository.buscarConFiltros(99L, null, null, null)).thenReturn(List.of());

        assertTrue(service.buscarConFiltros(99L, null, null, null).isEmpty());
    }

    @Test
    void registrar_requestValido_guardaYRetornaDTO() {
        AdmisionRequestDTO dto = buildDTO();
        doNothing().when(webClientUtil).validateExists(anyLong(), anyString(), anyString(), any());
        doNothing().when(webClientUtil).validateExistsByQueryParam(anyLong(), anyString(), anyString(), anyString(), any());
        when(tipoCargaRepository.findByNombreTipo("Encomienda")).thenReturn(Optional.of(TIPO));
        when(admisionRepository.save(any(Admision.class))).thenReturn(ADMISION);
        when(webClientUtil.postCrear(anyString(), any(), anyString(), any())).thenReturn(null);

        AdmisionResponseDTO result = service.registrar(dto);

        assertEquals(1L, result.getId());
        verify(admisionRepository).save(any(Admision.class));
        verify(webClientUtil).postCrear(eq("/api/v1/guias"), any(), eq("Guía de despacho"), any());
    }

    @Test
    void registrar_tipoCargaNoExiste_lanzaEntityNotFoundException() {
        AdmisionRequestDTO dto = buildDTO();
        doNothing().when(webClientUtil).validateExists(anyLong(), anyString(), anyString(), any());
        doNothing().when(webClientUtil).validateExistsByQueryParam(anyLong(), anyString(), anyString(), anyString(), any());
        when(tipoCargaRepository.findByNombreTipo("Encomienda")).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> service.registrar(dto));
        verify(admisionRepository, never()).save(any());
    }
}
