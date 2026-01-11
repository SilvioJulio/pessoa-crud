package com.dbacademia.pessoa.controller;


import com.dbacademia.pessoa.dtos.PessoaDTO;
import com.dbacademia.pessoa.entity.Pessoa;
import com.dbacademia.pessoa.service.PessoaService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(value = "/pessoas", produces = "application/json") // Adicionado produces
public class PessoaController {

    @Autowired
    private PessoaService service;

    @PostMapping
    public ResponseEntity<PessoaDTO> criar(@Valid @RequestBody Pessoa pessoa) {
        PessoaDTO novaPessoa = service.criar(pessoa);
        return ResponseEntity.status(HttpStatus.CREATED).body(novaPessoa);
    }

    @GetMapping
    @Operation(summary = "Listar pessoas")
    public ResponseEntity<Page<PessoaDTO>> listar(@ParameterObject Pageable pageable) {
        Page<PessoaDTO> paginaDTO = service.listarTodos(pageable);
        return ResponseEntity.ok(paginaDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PessoaDTO> buscar(@PathVariable Long id) {
        PessoaDTO dto = service.buscarPorId(id); // Alterado de PessoaDTO para dto
        return ResponseEntity.ok(dto);
    }


    @PutMapping("/{id}")
    public ResponseEntity<PessoaDTO> atualizar(@PathVariable Long id, @Valid @RequestBody Pessoa pessoaParaAtualizar) {
        PessoaDTO pessoaSalvar = service.atualizar(id, pessoaParaAtualizar);
        return ResponseEntity.ok(pessoaSalvar);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletarPorId(id);
        return ResponseEntity.noContent().build();
    }
}
