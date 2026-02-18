package com.dbacademia.pessoa.pessoaControllerTeste;

import com.dbacademia.pessoa.controller.PessoaController;
import com.dbacademia.pessoa.dtos.PessoaDTO;
import com.dbacademia.pessoa.entity.Pessoa;
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

    private PessoaDTO pessoaDTO;
    private LocalDate dataNasc;

    @BeforeEach
    void setUp() {
        dataNasc = LocalDate.of(1990, 5, 7);
        // Em 2026, quem nasceu em 07/05/1990 tem 35 anos
        pessoaDTO = new PessoaDTO(1L, "Julio Santos", "12345678901", dataNasc, null, new ArrayList<>());
    }

    @Test
    @DisplayName("Deve criar uma pessoa com sucesso e retornar 201")
    void deveCriarPessoaComSucesso() throws Exception {
        when(service.criar(any(Pessoa.class))).thenReturn(pessoaDTO);

        mockMvc.perform(post("/pessoas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(pessoaDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nome").value("Julio Santos"))
                .andExpect(jsonPath("$.idade").value(35));
    }

    @Test
    @DisplayName("Deve listar pessoas com paginação e retornar 200")
    void deveListarPessoasPaginadas() throws Exception {
        Page<PessoaDTO> pagina = new PageImpl<>(List.of(pessoaDTO));
        when(service.listarTodos(any(Pageable.class))).thenReturn(pagina);

        mockMvc.perform(get("/pessoas")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].nome").value("Julio Santos"));
    }

    @Test
    @DisplayName("Deve buscar pessoa por ID e retornar 200")
    void deveBuscarPorId() throws Exception {
        when(service.buscarPorId(1L)).thenReturn(pessoaDTO);

        mockMvc.perform(get("/pessoas/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nome").value("Julio Santos"));
    }

    @Test
    @DisplayName("Deve atualizar pessoa e retornar 200")
    void deveAtualizarPessoa() throws Exception {
        when(service.atualizar(eq(1L), any(Pessoa.class))).thenReturn(pessoaDTO);

        mockMvc.perform(put("/pessoas/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(pessoaDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Julio Santos"));
    }

    @Test
    @DisplayName("Deve deletar pessoa e retornar 204")
    void deveDeletarPessoa() throws Exception {
        mockMvc.perform(delete("/pessoas/1"))
                .andExpect(status().isNoContent());
    }
}
