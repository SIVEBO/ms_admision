package com.sivebo.ms_admision.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sivebo.ms_admision.model.TipoCarga;

public interface TipoCargaRepository extends JpaRepository<TipoCarga, Long> {

        Optional<TipoCarga> findByNombreTipo(String nombreTipo);
}
