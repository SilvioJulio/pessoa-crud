package com.dbacademia.pessoa.service;

import com.dbacademia.pessoa.dtos.pessoa.PessoaRequestDTO;
import com.dbacademia.pessoa.dtos.pessoa.PessoaResponseDTO;
import com.dbacademia.pessoa.entity.Pessoa;
import com.dbacademia.pessoa.exception.BusinessRuleException;
import com.dbacademia.pessoa.mapper.PessoaMapper;
import com.dbacademia.pessoa.repository.PessoaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@Transactional
public class PessoaServiceImpl implements PessoaService {

    private final PessoaRepository repository;
    private final PessoaMapper pessoaMapper;

    public PessoaServiceImpl(PessoaRepository repository, PessoaMapper pessoaMapper) {
        this.repository = repository;
        this.pessoaMapper = pessoaMapper;
    }

    @Override
    public PessoaResponseDTO criarPessoa(PessoaRequestDTO dto) {
        if (repository.existsByCpf(dto.cpf())) {
            throw new BusinessRuleException("CPF já cadastrado", "cpf");
        }
        Pessoa pessoa = pessoaMapper.toEntity(dto);
        Pessoa savedPessoa = repository.save(pessoa);
        return pessoaMapper.toResponseDTO(savedPessoa);
    }


    @Override
    public PessoaResponseDTO atualizarPessoa(Long id, PessoaRequestDTO dto) {
        Pessoa pessoaExistente = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pessoa não encontrada"));

        if (!pessoaExistente.getCpf().equals(dto.cpf())) {
            throw new BusinessRuleException("Não é permitido alterar o CPF de uma pessoa cadastrada", "cpf");
        }

        pessoaMapper.copyUpdatableFields(dto, pessoaExistente);

        Pessoa updatedPessoa = repository.save(pessoaExistente);
        return pessoaMapper.toResponseDTO(updatedPessoa);
    }

    @Override
    public Page<PessoaResponseDTO> listarPessoas(Pageable pageable) {
        // Retorna Page de ResponseDTO
        return repository.findAll(pageable).map(pessoaMapper::toResponseDTO);
    }

    @Override
    public PessoaResponseDTO buscarPessoaPorId(Long id) {
        Pessoa pessoa = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pessoa não encontrada"));
        return pessoaMapper.toResponseDTO(pessoa);
    }

    @Override
    public void excluirPessoaPorId(Long id) {
        if (!repository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "ID não encontrado");
        }
        repository.deleteById(id);
    }
}

