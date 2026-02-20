package com.dbacademia.pessoa.pessoacontrollertests;

import com.dbacademia.pessoa.controller.PessoaController;
import com.dbacademia.pessoa.dtos.endereco.EnderecoRequestDTO;
import com.dbacademia.pessoa.dtos.pessoa.PessoaRequestDTO;
import com.dbacademia.pessoa.dtos.pessoa.PessoaResponseDTO;
import com.dbacademia.pessoa.service.PessoaService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean; // Nova anotação para 2026
import org.springframework.test.web.servlet.MockMvc;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PessoaController.class)
public class PessoaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;


    @MockitoBean
    private PessoaService service;

    private PessoaRequestDTO pessoaRequestDTO;
    private LocalDate dataNasc;

    @BeforeEach
    void setup() {
        dataNasc = LocalDate.of(1990, 5, 7);
        pessoaRequestDTO = new PessoaRequestDTO(
                "Julio Santos",
                "12345678901",
                dataNasc,
                new ArrayList<>()
        );
    }

    @Test
    void deveCriarPessoaComSucesso() throws Exception {
        // 1. Crie um endereço válido para passar na validação
        EnderecoRequestDTO endereco = new EnderecoRequestDTO(
                "Rua Teste", 123, "Bairro", "Cidade", "ST", "12345678", true
        );

        // 2. O QUE ENTRA: Agora com a lista contendo o endereço
        PessoaRequestDTO enviado = new PessoaRequestDTO(
                "Julio",
                "12345678901",
                LocalDate.of(1990, 5, 7),
                List.of(endereco) // <--- Não pode ser vazia!
        );

        // 3. O QUE SAI: O Response (ajuste a lista aqui também se necessário)
        PessoaResponseDTO retornado = new PessoaResponseDTO(
                1L,
                "Julio",
                "12345678901",
                LocalDate.of(1990, 5, 7),
                35,
                new ArrayList<>() // Aqui no retorno pode ser vazia no Mock se seu DTO permitir
        );

        when(service.criarPessoa(any(PessoaRequestDTO.class))).thenReturn(retornado);

        mockMvc.perform(post("/pessoas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(enviado)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.idade").value(35));
    }


    @Test
    @DisplayName("Deve listar pessoas e retornar 200")
    void deveListarPessoas() throws Exception {


        List<PessoaResponseDTO> pessoas = List.of(
                new PessoaResponseDTO(1L, "Julio Santos", "12345678901", dataNasc, 35, new ArrayList<>()),
                new PessoaResponseDTO(2L, "Maria Silva", "10987654321", LocalDate.of(1985, 3, 15), 38, new ArrayList<>())
        );

        Page<PessoaResponseDTO> page = new PageImpl<>(pessoas);
        when(service.listarPessoas(any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/pessoas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(2))
                .andExpect(jsonPath("$.content[0].nome").value("Julio Santos"))
                .andExpect(jsonPath("$.content[1].nome").value("Maria Silva"));
   }

    @Test
    @DisplayName("Deve buscar pessoa por ID e retornar 200")
    void deveBuscarPorId() throws Exception {
        PessoaResponseDTO pessoaResponseDTO = new PessoaResponseDTO(
                1L,
                "Julio Santos",
                "12345678901",
                dataNasc,
                35,
                new ArrayList<>()
        );

        when(service.buscarPessoaPorId(1L)).thenReturn(pessoaResponseDTO);
        mockMvc.perform(get("/pessoas/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nome").value("Julio Santos"))
                .andExpect(jsonPath("$.idade").value(35));

    }

    @Test
    @DisplayName("Deve atualizar pessoa e retornar 200")
    void deveAtualizarPessoa() throws Exception {
        // 1. Crie um endereço para satisfazer a validação @NotEmpty/@NotNull
        EnderecoRequestDTO endereco = new EnderecoRequestDTO(
                "Rua Teste", 123, "Bairro", "Cidade", "ST", "12345678", true
        );

        // 2. Adicione o endereço na lista do Request
        PessoaRequestDTO atualizado = new PessoaRequestDTO(
                "Julio Updated",
                "12345678901",
                dataNasc,
                List.of(endereco) // <--- Não pode ser vazio
        );

        PessoaResponseDTO pessoaResponseDTO = new PessoaResponseDTO(
                1L,
                "Julio Updated",
                "12345678901",
                dataNasc,
                35,
                new ArrayList<>() // No Response o Mock aceita vazio sem problemas
        );

        when(service.atualizarPessoa(eq(1L), any(PessoaRequestDTO.class))).thenReturn(pessoaResponseDTO);

        mockMvc.perform(put("/pessoas/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(atualizado)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nome").value("Julio Updated"))
                .andExpect(jsonPath("$.idade").value(35));
    }




    @Test
    @DisplayName("Deve deletar pessoa e retornar 204")
    void deveDeletarPessoa() throws Exception {
        mockMvc.perform(delete("/pessoas/1"))
                .andExpect(status().isNoContent());
    }
}
