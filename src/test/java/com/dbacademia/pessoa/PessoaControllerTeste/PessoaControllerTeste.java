package com.dbacademia.pessoa.PessoaControllerTeste;


import com.dbacademia.pessoa.controller.PessoaController;
import com.dbacademia.pessoa.repository.PessoaRepository;
import org.mockito.InjectMocks;
import org.mockito.Mock;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

//@WebMvcTest(PessoaController.class)
public class PessoaControllerTeste {

    @Mock
    private PessoaRepository pessoaRepository;

    @InjectMocks
    private PessoaController pessoaController;

}
