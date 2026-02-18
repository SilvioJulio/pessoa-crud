package com.dbacademia.pessoa.testeIntegrado;


import com.dbacademia.pessoa.dtos.PessoaDTO;
import com.dbacademia.pessoa.entity.Pessoa;
import com.dbacademia.pessoa.util.PessoaCreator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles("test")
public class pessoaTesteIntegrado {

    @Autowired
    private TestRestTemplate restTemplate;


    private Pessoa criarPessoaMock(String nome, String cpf) {
        PessoaDTO dto = PessoaCreator.createPesssoaDTO(
                null,
                nome,
                cpf,
                LocalDate.of(2003, 5, 11),
                null,
                new ArrayList<>()
        );
        return PessoaCreator.createPessoaEntity(dto);
    }

    @Test
    void deveCriarPessoaComSucesso() {
        Pessoa novaPessoa = criarPessoaMock("Luiz Felipe", "01852416033");

        ResponseEntity<PessoaDTO> response = restTemplate.postForEntity("/pessoas", novaPessoa, PessoaDTO.class);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody().id());
        assertEquals(novaPessoa.getNome(), response.getBody().nome());
    }

    @Test
    void deveBuscarPessoaPorIdComSucesso() {
        Pessoa novaPessoa = criarPessoaMock("Busca Por Id", "12345678901");

        ResponseEntity<PessoaDTO> postResponse = restTemplate.postForEntity("/pessoas", novaPessoa, PessoaDTO.class);
        Long pessoaId = postResponse.getBody().id();

        ResponseEntity<PessoaDTO> getResponse = restTemplate.getForEntity("/pessoas/" + pessoaId, PessoaDTO.class);

        assertEquals(HttpStatus.OK, getResponse.getStatusCode());
        assertEquals(pessoaId, getResponse.getBody().id());
    }

    @Test
    void deveListarPessoasComSucesso() {
        Pessoa novaPessoa = criarPessoaMock("Listagem", "98765432100");
        restTemplate.postForEntity("/pessoas", novaPessoa, PessoaDTO.class);

        ResponseEntity<String> response = restTemplate.getForEntity("/pessoas", String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void deveAtualizarPessoaComSucesso() {
        // 1. Criar e Postar (Criação)
        Pessoa novaPessoa = criarPessoaMock("Nome Original", "01852416033");
        ResponseEntity<PessoaDTO> postResponse = restTemplate.postForEntity("/pessoas", novaPessoa, PessoaDTO.class);

        // Validar se o POST funcionou e pegou o ID
        assertEquals(HttpStatus.CREATED, postResponse.getStatusCode());
        Long pessoaId = postResponse.getBody().id();
        assertNotNull(pessoaId, "O ID não pode ser nulo após o POST");


        PessoaDTO dadosAtualizacao = new PessoaDTO(
                pessoaId,
                "Nome Atualizado",
                "01852416033", // Mesmo CPF
                LocalDate.of(2003, 5, 11),
                null,
                new ArrayList<>()
        );


        HttpEntity<PessoaDTO> requestEntity = new HttpEntity<>(dadosAtualizacao);
        ResponseEntity<PessoaDTO> putResponse = restTemplate.exchange(
                "/pessoas/" + pessoaId,
                HttpMethod.PUT,
                requestEntity,
                PessoaDTO.class
        );


        assertEquals(HttpStatus.OK, putResponse.getStatusCode());
        assertNotNull(putResponse.getBody());
        assertEquals("Nome Atualizado", putResponse.getBody().nome());
    }



    @Test
    void deveDeletarPessoaComSucesso() {
        Pessoa novaPessoa = criarPessoaMock("Deletar", "11122233344");
        ResponseEntity<PessoaDTO> postResponse = restTemplate.postForEntity("/pessoas", novaPessoa, PessoaDTO.class);
        Long pessoaId = postResponse.getBody().id();

        restTemplate.delete("/pessoas/" + pessoaId);

        ResponseEntity<PessoaDTO> getResponse = restTemplate.getForEntity("/pessoas/" + pessoaId, PessoaDTO.class);

        // Verifica se realmente não encontra mais
        assertEquals(HttpStatus.NOT_FOUND, getResponse.getStatusCode());
    }


    @Test
    void deveCalcularIdadeCorretamente() {
        LocalDate dataNascimento = LocalDate.of(1990, 5, 7);
        PessoaDTO novaPessoa = new PessoaDTO(
                null,
                "Calculo Idade",
                "55566677788",
                dataNascimento,
                null,
                new ArrayList<>()
        );

        ResponseEntity<PessoaDTO> postResponse = restTemplate.postForEntity("/pessoas", novaPessoa, PessoaDTO.class);

        assertEquals(HttpStatus.CREATED, postResponse.getStatusCode());
        assertNotNull(postResponse.getBody());
        assertEquals(35, postResponse.getBody().idade());
    }

    @Test
    void deveRetornar404AoBuscarPessoaInexistente() {
        ResponseEntity<PessoaDTO> response = restTemplate.getForEntity("/pessoas/99999", PessoaDTO.class);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void deveRetornar404AoAtualizarPessoaInexistente() {
        PessoaDTO dadosAtualizacao = new PessoaDTO(
                99999L,
                "Nome Inexistente",
                "00000000000",
                LocalDate.of(2000, 1, 1),
                null,
                new ArrayList<>()
        );

        HttpEntity<PessoaDTO> requestEntity = new HttpEntity<>(dadosAtualizacao);
        ResponseEntity<PessoaDTO> response = restTemplate.exchange(
                "/pessoas/99999",
                HttpMethod.PUT,
                requestEntity,
                PessoaDTO.class
        );

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}
