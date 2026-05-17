package com.sivebo.ms_admision.repository;

import com.sivebo.ms_admision.model.Admision;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface AdmisionRepository extends JpaRepository<Admision, Long> {
    List<Admision> findByClienteId(Long clienteId);
    Optional<Admision> findByCodigoTracking(String codigoTracking);
}
