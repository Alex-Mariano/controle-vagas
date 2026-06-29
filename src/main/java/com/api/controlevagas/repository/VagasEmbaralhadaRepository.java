package com.api.controlevagas.repository;

import com.api.controlevagas.entity.VagaEmbaralhada;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VagasEmbaralhadaRepository extends JpaRepository<VagaEmbaralhada, Integer> {

}
