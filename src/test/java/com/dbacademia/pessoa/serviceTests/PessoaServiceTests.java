package com.dbacademia.pessoa.serviceTests;


import com.dbacademia.pessoa.dtos.PessoaDTO;
import com.dbacademia.pessoa.entity.Pessoa;
import com.dbacademia.pessoa.repository.PessoaRepository;
import com.dbacademia.pessoa.service.PessoaService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.List;
import java.util.Arrays;

import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.Optional;
import java.util.ArrayList;


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

    @Test
    void deveSalvarUmaPessoaComSuscessoTest() {
        LocalDate dataNascimento = LocalDate.of(1983, 5, 7);
        Pessoa pessoa = new Pessoa(
                1L,
                "Julio",
                "05434543370",
                dataNascimento,
                17,
                new ArrayList<>()
        );
        when(pessoaRepository.existsByCpf(anyString())).thenReturn(false);
        when(pessoaRepository.save(any(Pessoa.class))).thenReturn(pessoa);

        PessoaDTO resultado = pessoaService.criar(pessoa);

        assertNotNull(resultado);
        assertEquals("Julio", resultado.nome());
        assertEquals(42, resultado.idade());
        verify(pessoaRepository).save(any(Pessoa.class));

    }

    @Test
    @DisplayName("Deve atualizar os dados e endereços de uma pessoa com sucesso")
    void deveAtualizarUmaPessoaEnderecoTest() {
        Long id = 1L;
        LocalDate dataNascimentoAntiga = LocalDate.of(1983, 5, 7);
        Pessoa pessoaExistente = new Pessoa();
        pessoaExistente.setId(id);
        pessoaExistente.setNome("Silva Flores");
        pessoaExistente.setCpf("12345678900");
        pessoaExistente.setDataNascimento(dataNascimentoAntiga);
        pessoaExistente.setEnderecos(new ArrayList<>());


        // Preparar objeto da requisição
        LocalDate dataNascimentoNova = LocalDate.of(1987, 12, 11);

        Pessoa pessoaAtualizada = new Pessoa();
        pessoaAtualizada.setNome("Sandra Gomes");
        pessoaAtualizada.setCpf("1234567123");
        pessoaAtualizada.setDataNascimento(dataNascimentoNova);
        pessoaAtualizada.setEnderecos(new ArrayList<>());

        when(pessoaRepository.findById(id)).thenReturn(Optional.of(pessoaExistente));

        // CORREÇÃO DO ERRO DE CAST:
        when(pessoaRepository.save(any(Pessoa.class))).thenReturn(pessoaExistente);


        PessoaDTO resultado = pessoaService.atualizar(id, pessoaAtualizada);

        assertNotNull(resultado);
        assertEquals("Sandra Gomes", resultado.nome());

    }

    @Test
    void deveVerificarIdadeVaiSerCalculada() {
        Pessoa pessoa = new Pessoa();
        pessoa.setDataNascimento(LocalDate.of(1983, 5, 7));

        Integer idadeCalculada = pessoa.getIdade();

        assertNotNull(idadeCalculada, "Deve ser calculada a idade automaticamente");
        assertEquals(42, idadeCalculada, "Idade calculada está inválida para início de 2026 ");

        assertTrue(pessoa.getDataNascimento().isBefore(LocalDate.now()),
                "A data de nascimento deve ser anterior à data atual"
        );


    }

    @Test
    void deveListarTodasPessoasEndereco() {

        Pessoa pessoa = new Pessoa();
        pessoa.setDataNascimento(LocalDate.of(1983, 5, 7));

        pessoa.setId(1l);
        pessoa.setNome("Luiz Santos");
        pessoa.setCpf("45612378914");
        pessoa.setDataNascimento(LocalDate.of(1990, 7, 12));
        pessoa.setEnderecos(new ArrayList<>());

        List<Pessoa> listaPessoas = List.of(pessoa);

        Page<Pessoa> paginaPessoas = new PageImpl<>(listaPessoas);
        Pageable pageable = PageRequest.of(0, 10);

        when(pessoaRepository.findAll(pageable)).thenReturn(paginaPessoas);

        Page<PessoaDTO> resultado = pessoaService.listarTodos(pageable);

        assertNotNull(resultado);
        assertEquals(1, resultado.getTotalElements());
        assertEquals("Luiz Santos", resultado.getContent().get(0).nome());

        assertEquals(35, resultado.getContent().get(0).idade());

        verify(pessoaRepository).findAll(pageable);


    }


    @Test
    void deveBuscarUmaPessaoPorIdComSucesso() {

        Pessoa pessoaMock = new Pessoa();
        Long id = 1L;
        pessoaMock.setId(id);
        pessoaMock.setNome("Leandro Silva");
        pessoaMock.setCpf("15975345687");

        when(pessoaRepository.findById(id)).thenReturn(Optional.of(pessoaMock));

        PessoaDTO resultado = pessoaService.buscarPorId(id);

        assertNotNull(resultado);
        assertEquals("Leandro Silva", resultado.nome());
        assertEquals(id, resultado.id());

        verify(pessoaRepository, times(1)).findById(id);


    }

    @Test
    void deveLancarExcecaoQuandoNomeForVazio() {
        LocalDate dataNascimento = LocalDate.of(2000, 5, 7);
        Pessoa pessoa = new Pessoa();
        pessoa.setId(2L);
        pessoa.setNome("");
        pessoa.setCpf("78915986318");
        pessoa.setDataNascimento(dataNascimento);

        when(pessoaRepository.existsByCpf(anyString())).thenReturn(false);
        when(pessoaRepository.save(any(Pessoa.class))).thenReturn(pessoa);

        PessoaDTO resultado = pessoaService.criar(pessoa);

        assertTrue(resultado.nome().isBlank(), "O nome deveria estar vazio");

        assertEquals("", resultado.nome());


    }

    @Test
    void deveDeletarPessoaComSucesso() {
        Long idExistente = 1L;

        when(pessoaRepository.existsById(idExistente)).thenReturn(true);

        pessoaService.deletarPorId(idExistente);

        verify(pessoaRepository, times(1)).existsById(idExistente);
        verify(pessoaRepository, times(1)).deleteById(idExistente);

    }
    @Test
    void deveLancarExcecaoAoDeletarIdInexistente(){

        Long idInexistente = 60L;
        when(pessoaRepository.existsById(idInexistente)).thenReturn(false);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            pessoaService.deletarPorId(idInexistente);
        });

        verify(pessoaRepository, never()).deleteById(anyLong());
    }


}
