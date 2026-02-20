package com.dbacademia.pessoa.dtos.pessoa;

import com.dbacademia.pessoa.dtos.endereco.EnderecoRequestDTO;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import java.time.LocalDate;
import java.util.List;

public record PessoaRequestDTO(
        @Size(max = 120)
        String nome,

        @NotBlank(message = "O CPF é obrigatório")
        @Pattern(regexp = "\\d{11}", message = "O CPF deve ter 11 dígitos")
        String cpf,

        @JsonFormat(pattern = "dd/MM/yyyy")
        @NotNull LocalDate dataNascimento,

        @NotEmpty(message = "Ao menos um endereço é obrigatório")
        @Valid List<EnderecoRequestDTO> enderecos
) {
}
