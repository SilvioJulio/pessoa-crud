package com.dbacademia.pessoa.util;

import com.dbacademia.pessoa.dtos.endereco.EnderecoRequestDTO;
import com.dbacademia.pessoa.dtos.endereco.EnderecoResponseDTO;
import com.dbacademia.pessoa.dtos.pessoa.PessoaRequestDTO;
import com.dbacademia.pessoa.dtos.pessoa.PessoaResponseDTO;
import com.dbacademia.pessoa.entity.Endereco;
import com.dbacademia.pessoa.entity.Pessoa;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PessoaCreator {

    // Método para criar o Response (Saída - com ID e Idade)
    public static PessoaResponseDTO createPessoaResponseDTO(Long id, String nome, String cpf, LocalDate dataNasc, Integer idade, List<EnderecoResponseDTO> enderecos) {
        return new PessoaResponseDTO(
                id,
                nome,
                cpf,
                dataNasc,
                idade,
                enderecos != null ? enderecos : new ArrayList<>()
        );
    }

    // Método para criar o Request (Entrada - sem ID e sem Idade)
    public static PessoaRequestDTO createPessoaRequestDTO(String nome, String cpf, LocalDate dataNasc, List<EnderecoRequestDTO> enderecos) {
        return new PessoaRequestDTO(
                nome,
                cpf,
                dataNasc,
                enderecos != null ? enderecos : new ArrayList<>()
        );
    }

    // Converte Request em Entity para os testes de integração/service
    public static Pessoa createPessoaEntity(PessoaRequestDTO dto) {
        Pessoa pessoa = new Pessoa();
        // Nota: RequestDTO não tem ID, então a entity começa com ID null para o banco gerar
        pessoa.setNome(dto.nome());
        pessoa.setCpf(dto.cpf());
        pessoa.setDataNascimento(dto.dataNascimento());

        if (dto.enderecos() != null) {
            List<Endereco> enderecos = dto.enderecos().stream().map(e -> {
                Endereco end = new Endereco();
                // RequestDTO de endereço também não costuma ter ID na criação
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
