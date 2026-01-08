package com.dbacademia.pessoa.service;

import com.dbacademia.pessoa.entity.Pessoa;
import com.dbacademia.pessoa.repository.PessoaRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;


@Service
@Transactional
public class PessoaService {

    @Autowired
    private PessoaRepository repository;

    public Pessoa criar(Pessoa pessoa) {
        if(repository.existsByCpf(pessoa.getCpf())){
            throw new RuntimeException("Erro: CPF já existente no banco ");
        }
        if(pessoa.getEnderecos() != null){
            pessoa.getEnderecos().forEach((endereco -> endereco.setPessoa(pessoa)));
        }
        return repository.save(pessoa);
    }

    public Pessoa atualizar (Long id, Pessoa pessoaAtualizada){
         Pessoa pessoaExistente = repository.findById(id)
                 .orElseThrow(() -> new RuntimeException("Pessoa não foi localizada com o ID: " + id));

                 pessoaExistente.setNome(pessoaAtualizada.getNome());
                 pessoaExistente.setDataNascimento(pessoaAtualizada.getDataNascimento());

                 if(!pessoaExistente.getCpf().equals(pessoaAtualizada.getCpf())){
                     if (repository.existsByCpf(pessoaAtualizada.getCpf())){
                         throw new RuntimeException("O CPF já está cadastrado no sistema");
                     }
                     pessoaExistente.setCpf(pessoaAtualizada.getCpf());

                 }
                 if(pessoaAtualizada.getEnderecos()!=null){
                     pessoaAtualizada.getEnderecos().clear();

                     pessoaAtualizada.getEnderecos().forEach(novoEndereco ->{
                         novoEndereco.setPessoa(pessoaExistente);
                         pessoaExistente.getEnderecos().add(novoEndereco);
                     });
                 }

            return repository.save(pessoaExistente);
    }

    public Page<Pessoa> listarTodos(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public void deletarPorId(Long id){
        if(!repository.existsById(id)){
            throw new RuntimeException("ID não encontrado");
        }
        repository.deleteById(id);
    }
}
