package com.dbacademia.pessoa.dtos.pessoa;

import com.dbacademia.pessoa.dtos.endereco.EnderecoResponseDTO;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;
import java.util.List;

public record PessoaResponseDTO(
        Long id,
        String nome,
        String cpf,
        @JsonFormat(pattern = "dd/MM/yyyy")
        LocalDate dataNascimento,
        Integer idade,
        List<EnderecoResponseDTO> enderecos
) {
}
