package com.dbacademia.pessoa.service;

import com.dbacademia.pessoa.dtos.PessoaDTO;
import com.dbacademia.pessoa.entity.Pessoa;
import com.dbacademia.pessoa.exception.BusinessRuleException;
import com.dbacademia.pessoa.mapper.PessoaMapper;
import com.dbacademia.pessoa.repository.PessoaRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;
import org.springframework.web.server.ResponseStatusException;



@Service
@Transactional
public class PessoaService {

    @Autowired
    private PessoaRepository repository;


    public PessoaDTO criar(Pessoa pessoa) {
        // CPF deve chegar em 11 dígitos (sem máscara). A Entity já valida.
        if (repository.existsByCpf(pessoa.getCpf())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Erro: CPF já existente no banco");
        }

        vincularEnderecos(pessoa);
        Pessoa salva = repository.save(pessoa);
        return PessoaMapper.toDTO(salva);
    }

    public PessoaDTO atualizar(Long id, Pessoa pessoaAtualizada) {
        Pessoa pessoaExistente = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Pessoa não foi localizada"));

        // 1. Pega os valores e remove tudo que não for número
        String cpfEnviado = pessoaAtualizada.getCpf() != null
                ? pessoaAtualizada.getCpf().replaceAll("\\D", "")
                : null;

        String cpfNoBanco = pessoaExistente.getCpf().replaceAll("\\D", "");

        System.out.println("Enviado: '" + cpfEnviado + "'");
        System.out.println("No Banco: '" + cpfNoBanco + "'");


        // 2. Compara apenas se o CPF foi enviado
        if (cpfEnviado != null && !cpfEnviado.equals(cpfNoBanco)) {
            throw new BusinessRuleException("Não é permitido alterar o CPF de uma pessoa cadastrada", "cpf");
        }

        // 3. Garante que o CPF da entidade persistida NUNCA mude,
        // mesmo que o Mapper tente sobrescrever
        pessoaAtualizada.setCpf(pessoaExistente.getCpf());

        PessoaMapper.copyUpdatableFields(PessoaMapper.toDTO(pessoaAtualizada), pessoaExistente);

        Pessoa salva = repository.saveAndFlush(pessoaExistente);
        return PessoaMapper.toDTO(salva);
    }




    public Page<PessoaDTO> listarTodos(Pageable pageable) {
        return repository.findAll(pageable).map(PessoaMapper::toDTO);
    }

    public PessoaDTO buscarPorId(Long id) {
        Pessoa pessoa = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Pessoa com ID " + id + " não foi encontrada"));
        return PessoaMapper.toDTO(pessoa);
    }

    public void deletarPorId(Long id) {
        if (!repository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "ID não encontrado");
        }
        repository.deleteById(id);
    }

    private void vincularEnderecos(Pessoa pessoa) {
        if (pessoa.getEnderecos() != null) {
            pessoa.getEnderecos().forEach(endereco -> endereco.setPessoa(pessoa));
        }
    }
}