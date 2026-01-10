
package com.dbacademia.pessoa.mapper;

import com.dbacademia.pessoa.dtos.EnderecoDTO;
import com.dbacademia.pessoa.dtos.PessoaDTO;
import com.dbacademia.pessoa.entity.Endereco;
import com.dbacademia.pessoa.entity.Pessoa;

import java.util.List;
import java.util.Objects;

public class PessoaMapper {

    public static PessoaDTO toDTO(Pessoa pessoa) {
        if (pessoa == null) return null;

        List<Endereco> enderecos = Objects.requireNonNullElse(pessoa.getEnderecos(), List.of());

        List<EnderecoDTO> enderecoDTOs = enderecos.stream()
                .map(PessoaMapper::toEnderecoDTO)
                .toList();

        return new PessoaDTO(
                pessoa.getId(),
                pessoa.getNome(),
                pessoa.getCpf(),
                pessoa.getDataNascimento(), // LocalDate; Jackson formata via @JsonFormat
                pessoa.getIdade(),
                enderecoDTOs
        );
    }

    public static EnderecoDTO toEnderecoDTO(Endereco endereco) {
        if (endereco == null) return null;

        return new EnderecoDTO(
                endereco.getId(),
                endereco.getRua(),
                endereco.getNumero(),
                endereco.getBairro(),
                endereco.getCidade(),
                endereco.getEstado(),
                endereco.getCep(),
                endereco.isPrincipal() // boolean -> isPrincipal()
        );
    }
}


