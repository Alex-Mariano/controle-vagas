package com.api.controlevagas.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "APARTAMENTOS")
public class ApartamentosEntity {

    @Id
    @Column(name = "NUMEROAPARTAMENTO", nullable = false, unique = true)
    private Integer numeroApartamento;

    @Column(name = "QUANTIDADEVAGAS", nullable = false, length = 2)
    private Integer quantidadeVagas;

    @Column(name = "ID_VAGA_DEPOSITO")
    private Integer idVagaDeposito;

    @Column(name = "ID_VAGA_PNE")
    private Integer idVagaPNE;

}
