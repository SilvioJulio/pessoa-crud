package com.dbacademia.pessoa.service;

import com.dbacademia.pessoa.entity.Pessoa;
import com.dbacademia.pessoa.repository.PessoaRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;

import java.util.Collections;

@Service
@Transactional
public class PessoaService {

    @Autowired
    private PessoaRepository repository;

    public Pessoa criar(Pessoa pessoa) {
        if(repository.existaByCpf(pessoa.getCpf())){
            throw new RuntimeException("CPF já cadastradp");
        }
        if(pessoa.getEnderecos() != null){
            pessoa.getEnderecos().forEach((endereco -> endereco.setPessoa(pessoa)));
        }
        return repository.save(pessoa);
    }
    public Page<Pessoa> listarTodos(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public void deletar(Long id){
        if(!repository.existsBy(id){
            throw new RuntimeException("ID não exite");
        }
        repository.deleteById(id);
    }
}
