package com.sivebo.ms_admision.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.sivebo.ms_admision.model.TipoCarga;
import com.sivebo.ms_admision.repository.TipoCargaRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

        private final TipoCargaRepository tipoCargaRepository;

        @Override
        public void run(String... args) {
                if (tipoCargaRepository.count() > 0) {
                        log.info(">>> Tipos de carga ya cargados. Se omite inicialización.");
                        return;
                }

                log.info(">>> Cargando tipos de carga iniciales...");

                tipoCargaRepository.save(new TipoCarga(null, "Documento", "Sobres y documentos sin embalaje especial"));
                tipoCargaRepository.save(new TipoCarga(null, "Encomienda", "Paquetes y cajas de carga general"));

                log.info(">>> Tipos de carga iniciales cargados exitosamente.");
        }
}
