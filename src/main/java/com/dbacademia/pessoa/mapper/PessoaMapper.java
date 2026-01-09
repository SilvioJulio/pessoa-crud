package com.dbacademia.pessoa.mapper;

import com.dbacademia.pessoa.dtos.EnderecoDTO;
import com.dbacademia.pessoa.dtos.PessoaDTO;
import com.dbacademia.pessoa.entity.Endereco;
import com.dbacademia.pessoa.entity.Pessoa;

import java.util.List;

public class PessoaMapper {
    public static PessoaDTO toDTO(Pessoa pessoa) {
        List<EnderecoDTO> enderecoDTO = pessoa.getEnderecos().stream()
                .map(PessoaMapper::toEnderecoDTO)
                .toList();
        return new PessoaDTO(
                pessoa.getId(),
                pessoa.getNome(),
                pessoa.getCpf(),
                pessoa.getDataNascimento(),
                pessoa.getIdade(),
                enderecoDTO

        );
    }

    public static EnderecoDTO toEnderecoDTO(Endereco endereco) {
        return new EnderecoDTO(
                endereco.getId(),
                endereco.getRua(),
                endereco.getNumero(),
                endereco.getBairro(),
                endereco.getCidade(),
                endereco.getEstado(),
                endereco.getCep(),
                endereco.isPrincipal()
        );

    }
}


