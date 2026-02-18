package com.dbacademia.pessoa.dtos;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import java.time.LocalDate;
import java.util.List;

public record PessoaDTO(

        @Null(groups = OnCreate.class, message = "O id deve ser nulo na criação")
        @NotNull(groups = OnUpdate.class, message = "O id é obrigatório na atualização")
        Long id,
        @NotBlank(message = "O nome é obrigatório")
        @Size(max = 120, message = "O nome deve ter no máximo {max} caracteres")
        String nome,
        @NotBlank(message = "O CPF é obrigatório")
        @Pattern(regexp = "\\d{11}", message = "O CPF deve conter exatamente 11 dígitos numéricos")
        String cpf,
        @JsonFormat(pattern = "dd/MM/yyyy")
        LocalDate dataNascimento,
        @JsonProperty("idade")
        Integer idade,
        @NotNull(message = "A lista de endereços é obrigatória")
        @Valid
        List<EnderecoDTO> enderecos


) {

    public interface OnCreate {}
    public interface OnUpdate {}

}
