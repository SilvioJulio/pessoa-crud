package com.dbacademia.pessoa.testeintegrado;


import com.dbacademia.pessoa.dtos.endereco.EnderecoRequestDTO;
import com.dbacademia.pessoa.dtos.pessoa.PessoaRequestDTO;
import com.dbacademia.pessoa.dtos.pessoa.PessoaResponseDTO;
import com.dbacademia.pessoa.entity.Pessoa;
import com.dbacademia.pessoa.repository.PessoaRepository;
import com.dbacademia.pessoa.util.PessoaCreator;
import org.junit.jupiter.api.BeforeEach;
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
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class pessoaTesteIntegrado {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private PessoaRepository pessoaRepository;

    @BeforeEach
    void setup() {
        pessoaRepository.deleteAll();
    }

    // Criar um Endereço padrão para satisfazer a validação @NotEmpty
    private List<EnderecoRequestDTO> criarEnderecoRequest() {
        return List.of(new EnderecoRequestDTO(
                "Rua Teste", 123, "Bairro", "Cidade", "SP", "01001000", true
        ));
    }

    private PessoaRequestDTO criarPessoaRequest(String nome, String cpf) {
        return new PessoaRequestDTO(
                nome,
                cpf,
                LocalDate.of(2003, 5, 11),
                criarEnderecoRequest() // Agora envia com endereço
        );
    }

    @Test
    void deveCriarPessoaComSucesso() {
        PessoaRequestDTO request = criarPessoaRequest("Luiz Felipe", "01852416033");
        ResponseEntity<PessoaResponseDTO> response = restTemplate.postForEntity("/pessoas", request, PessoaResponseDTO.class);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody().id());
        assertEquals(request.nome(), response.getBody().nome());
    }

    @Test
    void deveBuscarPessoaPorIdComSucesso() {
        PessoaRequestDTO request = criarPessoaRequest("Busca Por Id", "12345678901");
        ResponseEntity<PessoaResponseDTO> postResponse = restTemplate.postForEntity("/pessoas", request, PessoaResponseDTO.class);
        Long pessoaId = postResponse.getBody().id();

        ResponseEntity<PessoaResponseDTO> getResponse = restTemplate.getForEntity("/pessoas/" + pessoaId, PessoaResponseDTO.class);

        assertEquals(HttpStatus.OK, getResponse.getStatusCode());
        assertEquals(pessoaId, getResponse.getBody().id());
    }

    @Test
    void deveListarPessoasComSucesso() {
        PessoaRequestDTO request = criarPessoaRequest("Listagem", "98765432100");
        restTemplate.postForEntity("/pessoas", request, PessoaResponseDTO.class);

        ResponseEntity<String> response = restTemplate.getForEntity("/pessoas", String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().contains("Listagem"));
    }

    @Test
    void deveAtualizarPessoaComSucesso() {
        String cpfOriginal = "01852416033";
        PessoaRequestDTO requestInicial = criarPessoaRequest("Nome Original", cpfOriginal);
        ResponseEntity<PessoaResponseDTO> postResponse = restTemplate.postForEntity("/pessoas", requestInicial, PessoaResponseDTO.class);
        Long pessoaId = postResponse.getBody().id();

        PessoaRequestDTO dadosAtualizacao = new PessoaRequestDTO(
                "Nome Atualizado",
                cpfOriginal, // CPF imutável
                LocalDate.of(2003, 5, 11),
                criarEnderecoRequest() // Reutiliza endereço para validar
        );

        HttpEntity<PessoaRequestDTO> requestEntity = new HttpEntity<>(dadosAtualizacao);
        ResponseEntity<PessoaResponseDTO> putResponse = restTemplate.exchange(
                "/pessoas/" + pessoaId, HttpMethod.PUT, requestEntity, PessoaResponseDTO.class
        );

        assertEquals(HttpStatus.OK, putResponse.getStatusCode());
        assertEquals("Nome Atualizado", putResponse.getBody().nome());
    }

    @Test
    void deveDeletarPessoaComSucesso() {
        PessoaRequestDTO request = criarPessoaRequest("Deletar", "11122233344");
        ResponseEntity<PessoaResponseDTO> postResponse = restTemplate.postForEntity("/pessoas", request, PessoaResponseDTO.class);
        Long pessoaId = postResponse.getBody().id();

        restTemplate.delete("/pessoas/" + pessoaId);

        ResponseEntity<PessoaResponseDTO> getResponse = restTemplate.getForEntity("/pessoas/" + pessoaId, PessoaResponseDTO.class);
        assertEquals(HttpStatus.NOT_FOUND, getResponse.getStatusCode());
    }

    @Test
    void deveCalcularIdadeCorretamente() {
        LocalDate dataNascimento = LocalDate.of(1990, 5, 7);
        int idadeEsperada = Period.between(dataNascimento, LocalDate.now()).getYears();

        PessoaRequestDTO request = new PessoaRequestDTO(
                "Calculo Idade", "55566677788", dataNascimento, criarEnderecoRequest()
        );

        ResponseEntity<PessoaResponseDTO> response = restTemplate.postForEntity("/pessoas", request, PessoaResponseDTO.class);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(idadeEsperada, response.getBody().idade());
    }

    @Test
    void deveRetornar404AoBuscarPessoaInexistente() {
        ResponseEntity<PessoaResponseDTO> response = restTemplate.getForEntity("/pessoas/99999", PessoaResponseDTO.class);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void deveRetornar404AoAtualizarPessoaInexistente() {
        PessoaRequestDTO request = new PessoaRequestDTO(
                "Nome Inexistente", "00000000000", LocalDate.of(2000, 1, 1), criarEnderecoRequest()
        );

        HttpEntity<PessoaRequestDTO> requestEntity = new HttpEntity<>(request);
        ResponseEntity<PessoaResponseDTO> response = restTemplate.exchange(
                "/pessoas/99999", HttpMethod.PUT, requestEntity, PessoaResponseDTO.class
        );

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void deveRetornarErroFormatadoAoDeletarIdInexistente() {
        ResponseEntity<Map> response = restTemplate.exchange(
                "/pessoas/11", HttpMethod.DELETE, null, Map.class
        );

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        Map<String, Object> body = response.getBody();

        assertNotNull(body);
        assertEquals("BusinessRuleViolation", body.get("error"));
        assertEquals("ID não encontrado", body.get("message"));
    }
}
