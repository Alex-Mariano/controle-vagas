package com.api.controlevagas.service;

import com.api.controlevagas.entity.VagaEmbaralhada;
import com.api.controlevagas.entity.VagasEntity;
import com.api.controlevagas.repository.VagasEmbaralhadaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class EmbaralharService {

    @Autowired
    VagasEmbaralhadaRepository vagasEmbaralhadaRepository;

    public void gravaVagasEmbaralhadas(List<VagasEntity> vagasEntity) {

        List<VagaEmbaralhada> temp = new ArrayList<>();

        vagasEntity.forEach(s -> {
            VagaEmbaralhada vaga = new VagaEmbaralhada();
            vaga.setAndVagaEmb(s.getAndarVaga());
            vaga.setNroVagaEmb(s.getNroVaga());
            vaga.setTipVagaEmb(s.getTipoVaga());
            temp.add(vaga);
        });

        vagasEmbaralhadaRepository.saveAll(temp);

    }
}
