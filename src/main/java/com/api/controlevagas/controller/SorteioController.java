package com.api.controlevagas.controller;

import com.api.controlevagas.service.SorteioService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(value = "/sorteio", produces = MediaType.APPLICATION_JSON_VALUE)
@ResponseBody
public class SorteioController {

    @Autowired
    SorteioService sorteioVagas;

    @ApiOperation(value = "Sorteio")
    @DeleteMapping(value = "/zera")
    public ResponseEntity<Object> zeraResultado() {

        sorteioVagas.zeraResultado();

        return ResponseEntity.status(HttpStatus.OK).body("Resultado Zerado com sucesso");
    }

    @ApiOperation(value = "Sorteio")
    @PostMapping(value = "/apartamentos")
    public ResponseEntity<Object> sorteioApartamento() {

        sorteioVagas.sorteioApartamentos();

        return ResponseEntity.status(HttpStatus.OK).body("Sorteio de Apartamentos Realizado");
    }
    @ApiOperation(value = "Sorteio")
    @PostMapping(value = "/vagas")
    public ResponseEntity<Object> sorteioVaga() {

        sorteioVagas.sorteioVagas();

        return ResponseEntity.status(HttpStatus.OK).body("Sorteio de Vagas Realizado");
    }

}
