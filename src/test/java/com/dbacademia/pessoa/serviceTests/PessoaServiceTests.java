package com.dbacademia.pessoa.serviceTests;


import com.dbacademia.pessoa.entity.Pessoa;
import com.dbacademia.pessoa.repository.PessoaRepository;
import com.dbacademia.pessoa.service.PessoaService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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



}
