package com.dbacademia.pessoa.dtos;


import java.time.LocalDate;
import java.util.List;

public record PessoaDTO(
        Long id,
        String nome,
        String cpf,
        LocalDate dataNascimento,
        List<EnderecoDTO> enderecos


) {}
