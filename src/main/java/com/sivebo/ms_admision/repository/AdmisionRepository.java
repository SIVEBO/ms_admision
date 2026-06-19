package com.sivebo.ms_admision.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.sivebo.ms_admision.model.Admision;

public interface AdmisionRepository extends JpaRepository<Admision, Long> {

        // RF-17: consultar admisiones por sucursal de origen, con filtros opcionales de fecha y tipo de carga
        @Query("""
                        SELECT a FROM Admision a
                        WHERE a.idSucursalOrigen = :idSucursal
                          AND (:nombreTipo IS NULL OR a.tipoCarga.nombreTipo = :nombreTipo)
                          AND (:desde IS NULL OR a.fechaCreacion >= :desde)
                          AND (:hasta IS NULL OR a.fechaCreacion <= :hasta)
                        ORDER BY a.fechaCreacion DESC
                        """)
        List<Admision> buscarConFiltros(
                        @Param("idSucursal") Long idSucursal,
                        @Param("nombreTipo") String nombreTipo,
                        @Param("desde") LocalDateTime desde,
                        @Param("hasta") LocalDateTime hasta);
}
