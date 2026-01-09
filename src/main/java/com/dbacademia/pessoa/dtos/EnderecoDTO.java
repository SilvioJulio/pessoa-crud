package com.dbacademia.pessoa.dtos;

import java.util.List;

public record EnderecoDTO(
        Long id,
        String rua,
        Integer numero,
        String bairro,
        String cidade,
        String estado,
        String cep,
        boolean principal
) {
}
