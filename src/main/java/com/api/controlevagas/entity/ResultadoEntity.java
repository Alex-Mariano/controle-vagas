package com.api.controlevagas.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "RESULTADO")
public class ResultadoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID_RESULTADO", nullable = false, unique = true)
    private Integer idResultado;

    @Column(name = "NUMEROAPARTAMENTO")
    private Integer numeroApartamento;

    @Column(name = "QUANTIDADEVAGAS")
    private Integer quantidadeVagas;

    @Column(name = "ID_VAGA_DEPOSITO")
    private Integer idVagaDeposito;

    @Column(name = "NROVAGA1", length = 5)
    private String nroVaga1;

    @Column(name = "ANDARVAGA1", length = 5)
    private String andarVaga1;

    @Column(name = "NROVAGA2", length = 5)
    private String nroVaga2;

    @Column(name = "ANDARVAGA2", length = 5)
    private String andarVaga2;

    @Column(name = "ID_VAGA_PNE")
    private Integer idVagaPNE;

}
