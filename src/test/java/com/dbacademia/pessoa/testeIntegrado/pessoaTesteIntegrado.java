package com.dbacademia.pessoa.testeIntegrado;


import com.dbacademia.pessoa.dtos.PessoaDTO;
import com.dbacademia.pessoa.entity.Pessoa;
import com.dbacademia.pessoa.util.PessoaCreator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles("test")
public class pessoaTesteIntegrado {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void deveCriarPessoaComSucesso() {
        Pessoa novaPessoa = PessoaCreator.createPessoaEntity();

        ResponseEntity<PessoaDTO> response = restTemplate.postForEntity("/pessoas", novaPessoa, PessoaDTO.class);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody().id());
        assertEquals(novaPessoa .getNome(), response.getBody().nome());
        assertEquals(novaPessoa .getIdade(), response.getBody().idade());
    }

    @Test
    void  deveBuscarPessoaPorIdComSucesso() {
        Pessoa novaPessoa = PessoaCreator.createPessoaEntity();

        ResponseEntity<PessoaDTO> postResponse = restTemplate.postForEntity("/pessoas", novaPessoa, PessoaDTO.class);
        Long pessoaId = postResponse.getBody().id();

        ResponseEntity<PessoaDTO> getResponse = restTemplate.getForEntity("/pessoas/" + pessoaId, PessoaDTO.class);

        assertEquals(HttpStatus.OK, getResponse.getStatusCode());
        assertEquals(pessoaId, getResponse.getBody().id());
        assertEquals(novaPessoa.getNome(), getResponse.getBody().nome());
    }

    @Test
    void deveListarPessoasComSucesso() {
        Pessoa novaPessoa = PessoaCreator.createPessoaEntity();

        restTemplate.postForEntity("/pessoas", novaPessoa, PessoaDTO.class);

        ResponseEntity<String> response = restTemplate.getForEntity("/pessoas", String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());


    }

    @Test
    void  deveAtualizarPessoaComSucesso() {
        Pessoa novaPessoa = PessoaCreator.createPessoaEntity();

        ResponseEntity<PessoaDTO> postResponse = restTemplate.postForEntity("/pessoas", novaPessoa, PessoaDTO.class);
        Long pessoaId = postResponse.getBody().id();

        novaPessoa.setNome("Nome Atualizado");

        restTemplate.put("/pessoas/" + pessoaId, novaPessoa);

        ResponseEntity<PessoaDTO> getResponse = restTemplate.getForEntity("/pessoas/" + pessoaId, PessoaDTO.class);

        assertEquals(HttpStatus.OK, getResponse.getStatusCode());
        assertEquals("Nome Atualizado", getResponse.getBody().nome());
    }

    @Test
    void deveDeletarPessoaComSucesso() {
        Pessoa novaPessoa = PessoaCreator.createPessoaEntity();

        ResponseEntity<PessoaDTO> postResponse = restTemplate.postForEntity("/pessoas", novaPessoa, PessoaDTO.class);
        Long pessoaId = postResponse.getBody().id();

        restTemplate.delete("/pessoas/" + pessoaId);

        ResponseEntity<PessoaDTO> getResponse = restTemplate.getForEntity("/pessoas/" + pessoaId, PessoaDTO.class);

        assertEquals(HttpStatus.NOT_FOUND, getResponse.getStatusCode());
    }
}
