package com.dbacademia.pessoa.dtos.endereco;

public record EnderecoResponseDTO(
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
