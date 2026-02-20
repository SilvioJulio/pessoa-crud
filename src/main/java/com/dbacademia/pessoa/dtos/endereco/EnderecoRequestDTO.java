package com.dbacademia.pessoa.dtos.endereco;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record EnderecoRequestDTO (
        @NotBlank String rua,
        @NotNull Integer numero,
        @NotBlank String bairro,
        @NotBlank String cidade,
        @NotBlank String estado,
        @Pattern(regexp = "\\d{8}") String cep,
        boolean principal
) {}
