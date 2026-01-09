package com.dbacademia.pessoa.controller;


import com.dbacademia.pessoa.dtos.PessoaDTO;
import com.dbacademia.pessoa.entity.Pessoa;
import com.dbacademia.pessoa.mapper.PessoaMapper;
import com.dbacademia.pessoa.service.PessoaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/pessoas")
public class PessoaController {

    @Autowired
    private PessoaService service;

    @PostMapping
    public ResponseEntity<PessoaDTO> criar(@Valid @RequestBody Pessoa pessoa) {
        Pessoa novaPessoa = service.criar(pessoa);
        return ResponseEntity.status(HttpStatus.CREATED).body(PessoaMapper.toDTO(novaPessoa));
    }

    @GetMapping
    public ResponseEntity<Page<PessoaDTO>> listar(Pageable pageable) {
        Page<PessoaDTO> paginaDTO = service.listarTodos(pageable).map(PessoaMapper::toDTO);
        return ResponseEntity.ok(paginaDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PessoaDTO> buscar(@PathVariable Long id) {
        Pessoa pessoa = service.buscarPorId(id);
        return ResponseEntity.ok(PessoaMapper.toDTO(pessoa));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PessoaDTO> atualizar(@PathVariable Long id, @Valid @RequestBody Pessoa pessoaParaAtualizar) {
        Pessoa pessoaSalvar = service.atualizar(id, pessoaParaAtualizar);
        return ResponseEntity.ok(PessoaMapper.toDTO(pessoaSalvar));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletarPorId(id);
        return ResponseEntity.noContent().build();
    }
}
