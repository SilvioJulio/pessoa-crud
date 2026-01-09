package com.dbacademia.pessoa.serviceTests;


import com.dbacademia.pessoa.entity.Pessoa;
import com.dbacademia.pessoa.repository.PessoaRepository;
import com.dbacademia.pessoa.service.PessoaService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
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

        Pessoa resultado = pessoaService.criar(pessoa);

        assertNotNull(resultado);
        assertEquals("Julio",resultado.getNome());
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

        // CONFIGURAÇÃO DOS MOCKS
        when(pessoaRepository.findById(id)).thenReturn(Optional.of(pessoaExistente));

        // CORREÇÃO DO ERRO DE CAST:
        when(pessoaRepository.save(any(Pessoa.class))).thenReturn(pessoaExistente);


        Pessoa resultado = pessoaService.atualizar(id, pessoaAtualizada);

        assertNotNull(resultado);
        assertEquals("Sandra Gomes", resultado.getNome());

    }
    @Test
    void deveVerificarIdadeVaiSerCalculada(){
       Pessoa pessoa = new Pessoa();
       pessoa.setDataNascimento(LocalDate.of(1983,5,7));

       Integer idadeCalculada = pessoa.getIdade();

       assertNotNull(idadeCalculada,"Deve ser calculada a idade automaticamente");
       assertEquals(42, idadeCalculada, "Idade calculada está inválida para início de 2026 ");

       assertTrue(pessoa.getDataNascimento().isBefore(LocalDate.now()),
               "A data de nascimento deve ser anterior à data atual"
       );


    }


}
