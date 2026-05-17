package com.sivebo.ms_admision.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "paquetes")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Paquete {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String descripcion;

    private Double peso;
    private String dimensiones;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EstadoTracking estado;

    @Column(nullable = false)
    private Long clienteId; // referencia al cliente en ms_clientes
}
