package com.sivebo.ms_admision.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "admisiones")
public class Admision {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        Long id;

        @Column(name = "id_cliente_rem", nullable = false)
        Long idClienteRem;

        @Column(name = "id_cliente_dest", nullable = false)
        Long idClienteDest;

        @Column(name = "id_sucursal_origen", nullable = false)
        Long idSucursalOrigen;

        @Column(name = "id_sucursal_dest", nullable = false)
        Long idSucursalDest;

        @ManyToOne
        @JoinColumn(name = "id_tipo", nullable = false)
        TipoCarga tipoCarga;

        @Column(name = "peso_kg", nullable = false)
        Double pesoKg;

        @Column(name = "fecha_creacion", nullable = false)
        LocalDateTime fechaCreacion;

        @Column(name = "id_usuario_reg", nullable = false)
        Long idUsuarioReg;
}
