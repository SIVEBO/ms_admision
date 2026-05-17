package com.sivebo.ms_admision.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "admisiones")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Admision {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "paquete_id", nullable = false)
    private Paquete paquete;

    @Column(nullable = false)
    private Long clienteId;

    @Column(nullable = false)
    private String direccionDestino;

    @Column(nullable = false)
    private LocalDateTime fechaIngreso;

    @Column(unique = true, nullable = false)
    private String codigoTracking;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EstadoTracking estadoActual;
}
