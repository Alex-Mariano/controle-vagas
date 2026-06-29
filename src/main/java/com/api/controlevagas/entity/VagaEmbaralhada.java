package com.api.controlevagas.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Builder
@Table(name = "VAGAEMBARALHADA")
@NoArgsConstructor
@AllArgsConstructor
public class VagaEmbaralhada implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID_VAGA_EMB", nullable = false, unique = true)
    private Integer idVagaEmb;

    @Column(name = "NRO_VAGA_EMB", length = 5, nullable = false)
    private String nroVagaEmb;

    @Column(name = "AND_VAGA_EMB", length = 5, nullable = false)
    private String andVagaEmb;

    @Column(name = "TIP_VAGA_EMB", length = 1, nullable = false)
    private String TipVagaEmb;

}
