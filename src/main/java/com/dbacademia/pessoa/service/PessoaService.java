package com.dbacademia.pessoa.service;


import com.dbacademia.pessoa.dtos.pessoa.PessoaRequestDTO;
import com.dbacademia.pessoa.dtos.pessoa.PessoaResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PessoaService {

    PessoaResponseDTO criarPessoa(PessoaRequestDTO dto);

    PessoaResponseDTO atualizarPessoa(Long id, PessoaRequestDTO dto);

    Page<PessoaResponseDTO> listarPessoas(Pageable pageable);

    PessoaResponseDTO buscarPessoaPorId(Long id);

    void excluirPessoaPorId(Long id);
}
