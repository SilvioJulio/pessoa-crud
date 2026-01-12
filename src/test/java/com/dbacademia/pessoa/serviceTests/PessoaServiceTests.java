package com.dbacademia.pessoa.serviceTests;


import com.dbacademia.pessoa.dtos.PessoaDTO;
import com.dbacademia.pessoa.entity.Pessoa;
import com.dbacademia.pessoa.repository.PessoaRepository;
import com.dbacademia.pessoa.service.PessoaService;
import com.dbacademia.pessoa.util.PessoaCreator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

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
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PessoaServiceTests {

    @Mock
    private PessoaRepository pessoaRepository;

    @InjectMocks
    private PessoaService pessoaService;

    private Pessoa pessoaMock;
    private PessoaDTO pessoaDTOMock;

    @BeforeEach
    void setUp() {
        pessoaMock = PessoaCreator.createPessoaEntity();
        pessoaDTOMock = PessoaCreator.createPesssoaDTO();
    }

    @Test
    void deveSalvarUmaPessoaComSuscessoTest() {

        when(pessoaRepository.existsByCpf(anyString())).thenReturn(false);
        when(pessoaRepository.save(any(Pessoa.class))).thenReturn(pessoaMock);

        PessoaDTO resultado = pessoaService.criar(pessoaMock);

        assertNotNull(resultado);
        assertEquals(pessoaMock.getNome(), resultado.nome());
        assertEquals(pessoaMock.getIdade(), resultado.idade());
        verify(pessoaRepository).save(any(Pessoa.class));

    }

    @Test
    void deveAtualizarUmaPessoaEnderecoTest() {
        Long id = 1L;
        Pessoa pessoaBD = PessoaCreator.createPessoaEntity();
        pessoaBD.setId(id);


        Pessoa registrosNovos = PessoaCreator.createPessoaEntity();
        registrosNovos.setNome("Nome Atualizado");

        when(pessoaRepository.findById(id)).thenReturn(Optional.of(pessoaBD));
        when(pessoaRepository.existsByCpf(anyString())).thenReturn(false);


        when(pessoaRepository.saveAndFlush(any(Pessoa.class))).thenAnswer(invocation -> {
            Pessoa pessoa = invocation.getArgument(0);
            return pessoa;
        });

        PessoaDTO resultado = pessoaService.atualizar(id, registrosNovos);

        assertNotNull(resultado);
        assertEquals("Nome Atualizado", resultado.nome());

        assertNotNull( resultado.enderecos());
        assertFalse(resultado.enderecos().isEmpty());

        verify(pessoaRepository).saveAndFlush(any(Pessoa.class));

    }

    @Test
    void deveVerificarIdadeVaiSerCalculada() {

        Integer idadeCalculada = pessoaMock.getIdade();

        assertNotNull(idadeCalculada, "Deve ser calculada a idade automaticamente");
        assertEquals(pessoaMock.getIdade(), idadeCalculada, "Idade calculada está inválida para início de 2026 ");

        assertTrue(pessoaMock.getDataNascimento().isBefore(LocalDate.now()),
                "A data de nascimento deve ser anterior à data atual"
        );


    }

    @Test
    void deveListarTodasPessoasEndereco() {


        pessoaMock.setId(1l);
        Page<Pessoa> paginaPessoas = new PageImpl<>(List.of(pessoaMock));
        Pageable pageable = PageRequest.of(0, 10);
        when(pessoaRepository.findAll(pageable)).thenReturn(paginaPessoas);

        Page<PessoaDTO> resultado = pessoaService.listarTodos(pageable);


        assertNotNull(resultado);
        assertFalse(resultado.isEmpty());
        assertEquals(1, resultado.getTotalElements());
        verify(pessoaRepository, times(1)).findAll(pageable);


    }


    @Test
    void deveBuscarUmaPessaoPorIdComSucesso() {

        Long id = 1L;
        pessoaMock.setId(id);

        when(pessoaRepository.findById(id)).thenReturn(Optional.of(pessoaMock));

        PessoaDTO resultado = pessoaService.buscarPorId(id);

        assertEquals(id, resultado.id());
        assertEquals(pessoaMock.getNome(), resultado.nome());


    }

    @Test
    void deveLancarExcecaoQuandoNomeForVazio() {


        pessoaMock.setId(2L);
        pessoaMock.setNome("");

        when(pessoaRepository.existsByCpf(anyString())).thenReturn(false);
        when(pessoaRepository.save(any(Pessoa.class))).thenReturn(pessoaMock);

        PessoaDTO resultado = pessoaService.criar(pessoaMock);

        assertTrue(resultado.nome().isEmpty(),
                "Deveria lançar uma exceção ou retornar um erro ao tentar criar uma pessoa com nome vazio");

        assertEquals(
                "",
                resultado.nome(),
                "Deveria lançar uma exceção ou retornar um erro ao tentar criar uma pessoa com nome vazio"
        );


    }


    @Test
    void deveDeletarPessoaComSucesso() {
        Long idExistente = 1L;

        when(pessoaRepository.existsById(idExistente)).thenReturn(true);

        pessoaService.deletarPorId(idExistente);

        verify(pessoaRepository).deleteById(idExistente);

    }

    @Test
    void deveLancarExcecaoAoDeletarIdInexistente() {

        Long idInexistente = 60L;
        when(pessoaRepository.existsById(idInexistente)).thenReturn(false);

        assertThrows(RuntimeException.class, () -> pessoaService.deletarPorId(idInexistente));

        verify(pessoaRepository, never()).deleteById(anyLong());
    }


}
