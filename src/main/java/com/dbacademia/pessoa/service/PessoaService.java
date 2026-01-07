package com.dbacademia.pessoa.service;

import com.dbacademia.pessoa.entity.Pessoa;
import com.dbacademia.pessoa.repository.PessoaRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.awt.print.Pageable;

@Service
@Transactional
public class PessoaService {

    @Autowired
    private PessoaRepository repository;

    public Pessoa criar(Pessoa pessoa) {
        // Vincula a pessoa a cada endereço da lista para o JPA salvar as chaves estrangeiras
        if (pessoa.getEnderecos() != null) {
            pessoa.getEnderecos().forEach(endereco -> endereco.setPessoa(pessoa));
        }
        return repository.save(pessoa);
    }

    public Page<Pessoa> listarTodos(Pageable pageable) {
        return repository.findAll(pageable);
    }
}
