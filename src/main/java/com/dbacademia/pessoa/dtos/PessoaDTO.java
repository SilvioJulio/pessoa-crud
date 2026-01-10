package com.dbacademia.pessoa.dtos;


import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;
import java.util.List;

public record PessoaDTO(
        Long id,
        String nome,
        String cpf,
        @JsonFormat(pattern = "dd/MM/yyyy") // Define o formato  Brasil
        LocalDate dataNascimento,
        Integer idade,
        List<EnderecoDTO> endereco


) {}
