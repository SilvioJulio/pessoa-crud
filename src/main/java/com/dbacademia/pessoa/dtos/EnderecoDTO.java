package com.dbacademia.pessoa.dtos;

public record EnderecoDTO (
        Long id,
        String rua,
        Integer numero,
        String bairro,
        String cidade,
        String estado,
        String cep,
        boolean principal
)
{ }
