package com.api.controlevagas.repository;

import com.api.controlevagas.entity.VagasEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VagasRepository extends JpaRepository<VagasEntity, Integer> {

    List<VagasEntity> findByEnfrenteDepositoAndTipoVaga(boolean enfrenteDeposito, String tipoVaga);

    List<VagasEntity> findByNroVagaAndTipoVaga(String nroVaga, String tipoVaga);

}
