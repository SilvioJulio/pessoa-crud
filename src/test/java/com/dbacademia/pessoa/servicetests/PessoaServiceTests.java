package com.dbacademia.pessoa.servicetests;


import com.dbacademia.pessoa.dtos.pessoa.PessoaRequestDTO;
import com.dbacademia.pessoa.dtos.pessoa.PessoaResponseDTO;
import com.dbacademia.pessoa.entity.Pessoa;
import com.dbacademia.pessoa.exception.BusinessRuleException;
import com.dbacademia.pessoa.mapper.PessoaMapper;
import com.dbacademia.pessoa.repository.PessoaRepository;
import com.dbacademia.pessoa.service.PessoaService;
import com.dbacademia.pessoa.service.PessoaServiceImpl;
import com.dbacademia.pessoa.util.PessoaCreator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.time.Period;
import java.util.ArrayList;
import java.util.List;

import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.Optional;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
public class PessoaServiceTests {

    @Mock
    private PessoaRepository pessoaRepository;

    @Spy
    private PessoaMapper pessoaMapper = org.mapstruct.factory.Mappers.getMapper(PessoaMapper.class);

    @InjectMocks
    private PessoaServiceImpl pessoaService; // Use a implementação aqui para o InjectMocks funcionar

    private PessoaRequestDTO requestMock;
    private Pessoa pessoaMock;

    @BeforeEach
    void setUp() {
        LocalDate dataNasc = LocalDate.of(2003, 5, 11);
        requestMock = new PessoaRequestDTO("Luiz Felipe", "01852416033", dataNasc, new ArrayList<>());

        // Criamos a entidade mock para os retornos do repository
        pessoaMock = pessoaMapper.toEntity(requestMock);
        pessoaMock.setId(1L);
    }

    @Test
    void deveAtualizarUmaPessoaComSucessoTest() {
        Long id = 1L;
        PessoaRequestDTO dtoNovo = new PessoaRequestDTO("Nome Atualizado", "01852416033", LocalDate.of(2003, 5, 11), new ArrayList<>());

        when(pessoaRepository.findById(id)).thenReturn(Optional.of(pessoaMock));
        when(pessoaRepository.save(any(Pessoa.class))).thenAnswer(i -> i.getArgument(0));

        PessoaResponseDTO resultado = pessoaService.atualizarPessoa(id, dtoNovo);

        assertNotNull(resultado);
        assertEquals("Nome Atualizado", resultado.nome());
        verify(pessoaRepository).save(any(Pessoa.class));
    }

    @Test
    void deveLancarExcecaoAoTentarAlterarCpf() {
        Long id = 1L;
        // Request com CPF diferente do que está no pessoaMock ("01852416033")
        PessoaRequestDTO dtoErro = new PessoaRequestDTO("User", "22222222222", LocalDate.of(2000, 1, 1), new ArrayList<>());

        when(pessoaRepository.findById(id)).thenReturn(Optional.of(pessoaMock));

        assertThrows(BusinessRuleException.class, () -> {
            pessoaService.atualizarPessoa(id, dtoErro);
        });
    }

    @Test
    void deveListarTodasPessoasComSucesso() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Pessoa> paginaPessoas = new PageImpl<>(List.of(pessoaMock));
        when(pessoaRepository.findAll(pageable)).thenReturn(paginaPessoas);

        Page<PessoaResponseDTO> resultado = pessoaService.listarPessoas(pageable);

        assertNotNull(resultado);
        assertNotNull(resultado.getContent().get(0).idade());
        assertEquals(1, resultado.getTotalElements());
    }

    @Test
    void deveBuscarUmaPessoaPorIdComSucesso() {
        Long id = 1L;
        when(pessoaRepository.findById(id)).thenReturn(Optional.of(pessoaMock));

        PessoaResponseDTO resultado = pessoaService.buscarPessoaPorId(id);

        assertNotNull(resultado);
        assertEquals(id, resultado.id());
        assertNotNull(resultado.idade());
    }

    @Test
    void deveVerificarIdadeCalculadaNoResponse() {
        // Nascido em 2000, em 2026 deve ter 26 anos (considerando que já passou o aniversário)
        LocalDate dataNascimento = LocalDate.of(2000, 1, 1);
        PessoaRequestDTO dtoEntrada = new PessoaRequestDTO("Luiz", "01852416033", dataNascimento, new ArrayList<>());

        Pessoa entidade = pessoaMapper.toEntity(dtoEntrada);
        entidade.setId(1L);

        when(pessoaRepository.existsByCpf(anyString())).thenReturn(false);
        when(pessoaRepository.save(any())).thenReturn(entidade);

        PessoaResponseDTO resultado = pessoaService.criarPessoa(dtoEntrada);

        Integer idadeEsperada = Period.between(dataNascimento, LocalDate.now()).getYears();
        assertEquals(idadeEsperada, resultado.idade());
    }

    @Test
    void deveDeletarUmaPessoaComSucesso() {
        Long id = 1L;
        when(pessoaRepository.existsById(id)).thenReturn(true);

        assertDoesNotThrow(() -> {
            pessoaService.excluirPessoaPorId(id);
        });

        verify(pessoaRepository, times(1)).deleteById(id);
    }

    @Test
    void deveLancarExcecaoAoDeletarIdInexistente() {
        Long idInexistente = 99L;
        when(pessoaRepository.existsById(idInexistente)).thenReturn(false);

        // O Service lança ResponseStatusException (404) conforme seu código anterior
        assertThrows(ResponseStatusException.class, () -> {
            pessoaService.excluirPessoaPorId(idInexistente);
        });

        verify(pessoaRepository, never()).deleteById(anyLong());
    }
}
