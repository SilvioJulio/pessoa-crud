
package com.dbacademia.pessoa.mapper;

import com.dbacademia.pessoa.dtos.EnderecoDTO;
import com.dbacademia.pessoa.dtos.PessoaDTO;
import com.dbacademia.pessoa.entity.Endereco;
import com.dbacademia.pessoa.entity.Pessoa;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class PessoaMapper {

    public static PessoaDTO toDTO(Pessoa pessoa) {
        if (pessoa == null) return null;


        List<EnderecoDTO> enderecoDTOs = new ArrayList<>();
                if (pessoa.getEnderecos() != null) {
                    enderecoDTOs = pessoa.getEnderecos().stream()
                            .filter(Objects::nonNull)
                            .map(PessoaMapper::toEnderecoDTO)
                            .toList();
                }

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
    public static Pessoa toEntity(PessoaDTO pessoaDTO) {
        if (pessoaDTO == null) return null;

        Pessoa pessoa = new Pessoa();
        pessoa.setId(pessoaDTO.id());
        pessoa.setNome(pessoaDTO.nome());
        pessoa.setCpf(pessoaDTO.cpf());
        pessoa.setDataNascimento(pessoaDTO.dataNascimento());

        if(pessoaDTO.enderecos() != null) {
            List<Endereco> enderecos = pessoaDTO.enderecos().stream()
                    .filter(Objects::nonNull)
                    .map(enderecoDTO -> toEnderecoEntity(enderecoDTO, pessoa))
                    .collect(Collectors.toList());
            pessoa.setEnderecos(enderecos);
        }

        return pessoa;
    }
    public static Endereco toEnderecoEntity(EnderecoDTO enderecoDTO, Pessoa pessoa) {
        if (enderecoDTO == null) return null;

        Endereco endereco = new Endereco();
        endereco.setId(enderecoDTO.id());
        endereco.setRua(enderecoDTO.rua());
        endereco.setNumero(enderecoDTO.numero());
        endereco.setBairro(enderecoDTO.bairro());
        endereco.setCidade(enderecoDTO.cidade());
        endereco.setEstado(enderecoDTO.estado());
        endereco.setCep(enderecoDTO.cep());
        endereco.setPrincipal(enderecoDTO.principal());
        endereco.setPessoa(pessoa);

        return endereco;
    }
}


