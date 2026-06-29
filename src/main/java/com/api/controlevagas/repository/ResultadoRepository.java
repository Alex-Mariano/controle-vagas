package com.api.controlevagas.repository;

import com.api.controlevagas.entity.ResultadoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResultadoRepository extends JpaRepository<ResultadoEntity, Integer> {

    @Query(value = "SELECT * FROM RESULTADO order by id_resultado ", nativeQuery = true)
    List<ResultadoEntity> findResultadoOrdered();
}
