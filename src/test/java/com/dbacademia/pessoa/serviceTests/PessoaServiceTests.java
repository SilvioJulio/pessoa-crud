package com.dbacademia.pessoa.serviceTests;


import com.dbacademia.pessoa.dtos.PessoaDTO;
import com.dbacademia.pessoa.entity.Pessoa;
import com.dbacademia.pessoa.exception.BusinessRuleException;
import com.dbacademia.pessoa.repository.PessoaRepository;
import com.dbacademia.pessoa.service.PessoaService;
import com.dbacademia.pessoa.util.PessoaCreator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.time.Period;
import java.util.ArrayList;
import java.util.List;

import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.Optional;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PessoaServiceTests {

    @Mock
    private PessoaRepository pessoaRepository;

    @InjectMocks
    private PessoaService pessoaService;

    private Pessoa pessoaMock;

    @BeforeEach
    void setUp() {
        // 1. Crie o DTO primeiro
        PessoaDTO dto = PessoaCreator.createPesssoaDTO(
                1L,
                "Luiz Felipe",
                "01852416033",
                LocalDate.of(2003, 5, 11),
                null,
                new ArrayList<>()
        );

        pessoaMock = PessoaCreator.createPessoaEntity(dto);
    }



    @Test
    void deveAtualizarUmaPessoaComSucessoTest() {
        // Arrange
        Long id = 1L;

        // Criando pessoa que "está no banco"
        PessoaDTO dtoBanco = PessoaCreator.createPesssoaDTO(id, "Nome Original", "01852416033", LocalDate.of(2003, 5, 11), null, new ArrayList<>());
        Pessoa pessoaBD = PessoaCreator.createPessoaEntity(dtoBanco);

        // Criando "novos dados" que vêm da requisição (mesmo CPF)
        PessoaDTO dtoNovo = PessoaCreator.createPesssoaDTO(id, "Nome Atualizado", "01852416033", LocalDate.of(2003, 5, 11), null, new ArrayList<>());
        Pessoa registrosNovos = PessoaCreator.createPessoaEntity(dtoNovo);

        when(pessoaRepository.findById(id)).thenReturn(Optional.of(pessoaBD));
        when(pessoaRepository.saveAndFlush(any(Pessoa.class))).thenAnswer(i -> i.getArgument(0));

        // Act
        PessoaDTO resultado = pessoaService.atualizar(id, registrosNovos);

        // Assert
        assertNotNull(resultado);
        assertEquals("Nome Atualizado", resultado.nome());
        assertEquals("01852416033", resultado.cpf());
        verify(pessoaRepository).saveAndFlush(any(Pessoa.class));
    }

    @Test
    void deveLancarExcecaoAoTentarAlterarCpf() {
        // Arrange
        Long id = 1L;
        PessoaDTO dtoBanco = PessoaCreator.createPesssoaDTO(id, "User", "11111111111", LocalDate.of(2000, 1, 1), null,new ArrayList<>());
        Pessoa pessoaNoBanco = PessoaCreator.createPessoaEntity(dtoBanco);

        PessoaDTO dtoErro = PessoaCreator.createPesssoaDTO(id, "User", "22222222222", LocalDate.of(2000, 1, 1), null,  new ArrayList<>());
        Pessoa dadosComCpfDiferente = PessoaCreator.createPessoaEntity( dtoErro);

        when(pessoaRepository.findById(id)).thenReturn(Optional.of(pessoaNoBanco));

        // Act & Assert
        assertThrows(BusinessRuleException.class, () -> {
            pessoaService.atualizar(id, dadosComCpfDiferente);
        });
    }

    @Test
    void deveVerificarIdadeVaiSerCalculada() {
        Integer idadeCalculada = pessoaMock.getIdade();
        assertNotNull(idadeCalculada);
        // Em 2026, quem nasceu em 2003 tem 22 ou 23 anos
        assertTrue(idadeCalculada == 22 || idadeCalculada == 23);

    }

    @Test
    void deveListarTodasPessoasEndereco() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Pessoa> paginaPessoas = new PageImpl<>(List.of(pessoaMock));
        when(pessoaRepository.findAll(pageable)).thenReturn(paginaPessoas);

        Page<PessoaDTO> resultado = pessoaService.listarTodos(pageable);

        assertNotNull(resultado);
        assertEquals(1, resultado.getTotalElements());
    }

    @Test
    void deveBuscarUmaPessaoPorIdComSucesso() {
        Long id = 1L;
        when(pessoaRepository.findById(id)).thenReturn(Optional.of(pessoaMock));

        PessoaDTO resultado = pessoaService.buscarPorId(id);

        assertEquals(id, resultado.id());
        assertEquals(pessoaMock.getNome(), resultado.nome());
    }
    @Test
    void deveVerificarIdadeCalculadaNoDTO() {
        PessoaDTO dto = new PessoaDTO(1L, "Teste", "01852416033", LocalDate.of(2000, 1, 1), null, new ArrayList<>());

        Integer idadeEsperada = Period.between(LocalDate.of(2000, 1, 1), LocalDate.now()).getYears();

        assertEquals(idadeEsperada, dto.idade());
    }
    @Test
    void deveVerificarSeIdadeFoiCalculadaNoRetorno() {
        // 1. Arrange: Criamos um DTO com idade null
        PessoaDTO dtoEntrada = PessoaCreator.createPesssoaDTO(null, "Luiz", "01852416033", LocalDate.of(2000, 1, 1), null, new ArrayList<>());
        Pessoa entidade = PessoaCreator.createPessoaEntity(dtoEntrada);

        when(pessoaRepository.save(any())).thenReturn(entidade);

        // 2. Act: O service chama o Mapper que calcula a idade
        PessoaDTO resultado = pessoaService.criar(entidade);


        assertNotNull(resultado.idade());
        assertEquals(26, resultado.idade());
    }


}
