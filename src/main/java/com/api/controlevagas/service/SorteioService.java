package com.api.controlevagas.service;

import com.api.controlevagas.entity.ApartamentosEntity;
import com.api.controlevagas.entity.ResultadoEntity;
import com.api.controlevagas.entity.VagasEntity;
import com.api.controlevagas.repository.ApartamentosRepository;
import com.api.controlevagas.repository.ResultadoRepository;
import com.api.controlevagas.repository.VagasEmbaralhadaRepository;
import com.api.controlevagas.repository.VagasRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class SorteioService {

    public static final String SIMPLES = "S";
    public static final String DUPLA = "D";
    public static final boolean FALSO = false;

    @Autowired
    ApartamentosRepository apartamentosRepository;

    @Autowired
    ResultadoRepository resultadoRepository;

    @Autowired
    VagasRepository vagasRepository;

    @Autowired
    VagasEmbaralhadaRepository vagasEmbaralhadaRepository;

    @Autowired
    EmbaralharService embaralharService;

    public List<ApartamentosEntity> listaApartamentos() {
        return apartamentosRepository.findAll();
    }

    /**
     * Exclui todos os registros da tabela RESULTADO & VAGAEMBARALHADA
     */
    @Transactional
    public void zeraResultado() {

        resultadoRepository.deleteAll();

        vagasEmbaralhadaRepository.deleteAll();

        System.out.println("Zerou resultados....");

    }

    /**
     * Grava os apartamentos embaralhados na tabela RESULTADO com vagas em frente ao deposito
     */
    @Transactional
    public void sorteioApartamentos() {

        // Le tabela APARTAMENTOS
        // Embaralha a lista de apartamentos
        // Grava os apartamentos embaralhados na tabela RESULTADO
        carregaPreResultado();

        // Le tabela VAGAS
        List<VagasEntity> vagas = vagasRepository.findAll();

        // Le tabela RESULTADO
        List<ResultadoEntity> resultadoEntities = resultadoRepository.findAll();

        // Atualiza na tabela RESULTADO os apartamentos com vagas em frente ao Deposito
        atualizaVagasEnfrenteAoDeposito(vagas, resultadoEntities);

        resultadoRepository.saveAll(resultadoEntities);

        System.out.println("Apartamentos sorteados....");
    }

    /**
     * Embaralha as Vagas Simples e Duplas e Atualiza tabela RESULTADO
     */
    @Transactional
    public void sorteioVagas() {

        // Embaralha as Vagas Simples
        List<VagasEntity> vagasSimples = embaralhaVagasSimples();

        // Embaralha as Vagas Duplas
        List<VagasEntity> vagasDuplas = embaralhaVagasDuplas();

        // Le tabela RESULTADO
        List<ResultadoEntity> resultadoEntities = resultadoRepository.findResultadoOrdered();

        // Atualiza as vagas dos Apartamentos
        atualizaVagasSimplesEDuplas(vagasSimples, vagasDuplas, resultadoEntities);

        resultadoRepository.saveAll(resultadoEntities);

        System.out.println("Vagas sorteadas....");
    }

    private static void atualizaVagasSimplesEDuplas(List<VagasEntity> vagasSimples, List<VagasEntity> vagasDuplas, List<ResultadoEntity> resultadoEntities) {

        // Trata Registros da tabela RESULTADO
        resultadoEntities.forEach(resultado -> {

            var quantidadeVagas = resultado.getQuantidadeVagas();
            var nroVaga1 = resultado.getNroVaga1();
            var nroVaga2 = resultado.getNroVaga2();

            // Trata Apartamento com direito a 1 Vaga
            if (quantidadeVagas == 1) {

                // Caso ainda não tenha uma vaga atribuida, seta a vaga ao apartamento
                if (nroVaga1 == null) {
                    resultado.setNroVaga1(vagasSimples.get(0).getNroVaga());
                    resultado.setAndarVaga1(vagasSimples.get(0).getAndarVaga());

                    // Remove Vaga da lista
                    vagasSimples.remove(0);
                }
            }

            // Trata Apartamento com direito a 2 Vagas
            if (quantidadeVagas == 2) {

                // Nenhuma vaga foi associada ao apartamento
                if (nroVaga1 == null) {

                    // Já sorteiou todas vagas DUPLAS
                    if (vagasDuplas.isEmpty()) {

                        // Associa uma vaga SIMPLES ao Apartamento - 1 Vaga
                        resultado.setNroVaga1(vagasSimples.get(0).getNroVaga());
                        resultado.setAndarVaga1(vagasSimples.get(0).getAndarVaga());

                        // Remove Vaga da lista
                        vagasSimples.remove(0);

                        // Associa uma vaga SIMPLES ao Apartamento - 2 Vaga
                        resultado.setNroVaga2(vagasSimples.get(0).getNroVaga());
                        resultado.setAndarVaga2(vagasSimples.get(0).getAndarVaga());

                        // Remove Vaga da lista
                        vagasSimples.remove(0);

                    } else {
                        // Associa uma vaga DUPLA ao Apartamento
                        resultado.setNroVaga1(vagasDuplas.get(0).getNroVaga());
                        resultado.setAndarVaga1(vagasDuplas.get(0).getAndarVaga());

                        // Remove Vaga da lista
                        vagasDuplas.remove(0);

                    }

                } else {  // Apartamento com 1 vaga já sorteada

                    if (nroVaga2 == null) {
                        // Associa uma vaga SIMPLES ao Apartamento - 2 Vaga
                        resultado.setNroVaga2(vagasSimples.get(0).getNroVaga());
                        resultado.setAndarVaga2(vagasSimples.get(0).getAndarVaga());

                        // Remove Vaga da lista
                        vagasSimples.remove(0);
                    }
                }
            }
        });
    }

    private List<VagasEntity> embaralhaVagasDuplas() {

        List<VagasEntity> vagasDuplas = vagasRepository.findByEnfrenteDepositoAndTipoVaga(FALSO, DUPLA);

        Collections.shuffle(vagasDuplas);

        embaralharService.gravaVagasEmbaralhadas(vagasDuplas);

        return vagasDuplas;
    }

    private List<VagasEntity> embaralhaVagasSimples() {

        List<VagasEntity> vagasSimples = vagasRepository.findByEnfrenteDepositoAndTipoVaga(FALSO, SIMPLES);

        Collections.shuffle(vagasSimples);

        embaralharService.gravaVagasEmbaralhadas(vagasSimples);

        return vagasSimples;
    }

    /**
     * Pega a identificação do Deposito da tabela APARTAMENTOS
     * Varre toda os registros da tabela RESULTADO
     * Atualiza  o numero da vaga 1 / andar 1 na tabela RESULTADO
     */
    private static void atualizaVagasEnfrenteAoDeposito(List<VagasEntity> vagas, List<ResultadoEntity> resultadoEntities) {

        resultadoEntities.forEach(
                resultado -> {

                    Integer idVagaDeposito = resultado.getIdVagaDeposito();
                    Integer idVagaPNE = resultado.getIdVagaPNE();

                    if (idVagaDeposito != null) {

                        Optional<VagasEntity> dadosVagaEmFrenteDeposito = vagas.stream()
                                .filter(f -> Objects.equals(f.getIdVaga(), idVagaDeposito))
                                .findFirst();

                        String nroVaga = dadosVagaEmFrenteDeposito.get().getNroVaga();
                        String andarVaga = dadosVagaEmFrenteDeposito.get().getAndarVaga();

                        resultado.setNroVaga1(nroVaga);
                        resultado.setAndarVaga1(andarVaga);

                        if (idVagaPNE != null) {

                            Optional<VagasEntity> dadosVagaPNE = vagas.stream()
                                    .filter(f -> Objects.equals(f.getIdVaga(), idVagaPNE))
                                    .findFirst();

                            nroVaga = dadosVagaPNE.get().getNroVaga();
                            andarVaga = dadosVagaPNE.get().getAndarVaga();

                            resultado.setNroVaga2(nroVaga);
                            resultado.setAndarVaga2(andarVaga);

                        }

                    } else {

                        if (idVagaPNE != null) {

                            Optional<VagasEntity> dadosVagaPNE = vagas.stream()
                                    .filter(f -> Objects.equals(f.getIdVaga(), idVagaPNE))
                                    .findFirst();

                            String nroVaga = dadosVagaPNE.get().getNroVaga();
                            String andarVaga = dadosVagaPNE.get().getAndarVaga();

                            resultado.setNroVaga1(nroVaga);
                            resultado.setAndarVaga1(andarVaga);

                        }
                    }
                }
        );
    }

    /**
     * Le tabela APARTAMENTOS
     * Embaralha a lista de apartamentos
     * Grava os apartamentos embaralhados na tabela RESULTADO
     */
    private void carregaPreResultado() {

        List<ApartamentosEntity> apartamentosEntityList = apartamentosRepository.findAll();

        var resultado2Entity = new ResultadoEntity();

        Collections.shuffle(apartamentosEntityList);

        apartamentosEntityList.forEach(det -> {
                    resultado2Entity.setIdResultado(1);
                    resultado2Entity.setNumeroApartamento(det.getNumeroApartamento());
                    resultado2Entity.setQuantidadeVagas(det.getQuantidadeVagas());
                    resultado2Entity.setIdVagaDeposito(det.getIdVagaDeposito());
                    resultado2Entity.setIdVagaPNE(det.getIdVagaPNE());

                    resultadoRepository.save(resultado2Entity);
                }
        );

        resultadoRepository.flush();
    }

}
