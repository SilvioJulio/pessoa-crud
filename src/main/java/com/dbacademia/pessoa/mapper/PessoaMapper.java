
package com.dbacademia.pessoa.mapper;

import com.dbacademia.pessoa.dtos.EnderecoDTO;
import com.dbacademia.pessoa.dtos.PessoaDTO;
import com.dbacademia.pessoa.entity.Endereco;
import com.dbacademia.pessoa.entity.Pessoa;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class PessoaMapper {

    public static PessoaDTO toDTO(Pessoa pessoa) {
        if (pessoa == null) return null;

        Integer idadeCalculada = null;
        if (pessoa.getDataNascimento() != null) {
            idadeCalculada = Period.between(pessoa.getDataNascimento(), LocalDate.now()).getYears();
        }

        List<EnderecoDTO> enderecoDTOs = new ArrayList<>();
        if (pessoa.getEnderecos() != null) {
            enderecoDTOs = pessoa.getEnderecos().stream()
                    .filter(Objects::nonNull)
                    .map(PessoaMapper::toEnderecoDTO)
                    .toList();
        }

        // 3. Retorna o DTO na ORDEM CORRETA do Record
        return new PessoaDTO(
                pessoa.getId(),
                pessoa.getNome(),
                pessoa.getCpf(),
                pessoa.getDataNascimento(),
                idadeCalculada, // A idade deve vir aqui para aparecer após a data no JSON
                enderecoDTOs     // A lista de endereços vem por último
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
                endereco.isPrincipal()
        );
    }

    // USO: CREATE
    public static Pessoa toEntity(PessoaDTO pessoaDTO) {
        if (pessoaDTO == null) return null;

        Pessoa pessoa = new Pessoa();
        pessoa.setId(pessoaDTO.id());
        pessoa.setNome(pessoaDTO.nome());
        pessoa.setCpf(pessoaDTO.cpf() != null ? pessoaDTO.cpf().trim() : null); // higiene mínima
        pessoa.setDataNascimento(pessoaDTO.dataNascimento());

        if (pessoaDTO.enderecos() != null) {
            List<Endereco> enderecos = pessoaDTO.enderecos().stream()
                    .filter(Objects::nonNull)
                    .map(enderecoDTO -> toEnderecoEntity(enderecoDTO, pessoa))
                    .collect(Collectors.toList());
            pessoa.setEnderecos(enderecos);
        }

        return pessoa;
    }

    // USO: UPDATE (ignora CPF)
    public static void copyUpdatableFields(PessoaDTO dto, Pessoa target) {
        if (dto == null || target == null) return;

        target.setNome(dto.nome());
        target.setDataNascimento(dto.dataNascimento());

        target.getEnderecos().clear();
        if (dto.enderecos() != null) {
            dto.enderecos().stream()
                    .filter(Objects::nonNull)
                    .map(end -> toEnderecoEntity(end, target))
                    .forEach(target.getEnderecos()::add);
        }
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

