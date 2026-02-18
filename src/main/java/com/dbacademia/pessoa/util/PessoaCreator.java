package com.dbacademia.pessoa.util;

import com.dbacademia.pessoa.dtos.EnderecoDTO;
import com.dbacademia.pessoa.dtos.PessoaDTO;
import com.dbacademia.pessoa.entity.Endereco;
import com.dbacademia.pessoa.entity.Pessoa;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PessoaCreator {

    public static PessoaDTO createPesssoaDTO(Long id, String nome, String cpf, LocalDate dataNasc, Integer idade, List<EnderecoDTO> enderecos) {
        return new PessoaDTO(
                id,
                nome,
                cpf,
                dataNasc,
                idade,
                enderecos != null ? enderecos : new ArrayList<>()
        );
    }

    // ADICIONE O PARÂMETRO (PessoaDTO dto) AQUI ABAIXO:
    public static Pessoa createPessoaEntity(PessoaDTO dto) {
        Pessoa pessoa = new Pessoa();
        pessoa.setId(dto.id());
        pessoa.setNome(dto.nome());
        pessoa.setCpf(dto.cpf());
        pessoa.setDataNascimento(dto.dataNascimento());

        if (dto.enderecos() != null) {
            List<Endereco> enderecos = dto.enderecos().stream().map(e -> {
                Endereco end = new Endereco();
                end.setId(e.id());
                end.setRua(e.rua());
                end.setNumero(e.numero());
                end.setBairro(e.bairro());
                end.setCidade(e.cidade());
                end.setEstado(e.estado());
                end.setCep(e.cep());
                end.setPrincipal(e.principal());
                end.setPessoa(pessoa);
                return end;
            }).toList();
            pessoa.setEnderecos(new ArrayList<>(enderecos));
        }

        return pessoa;
    }
}
