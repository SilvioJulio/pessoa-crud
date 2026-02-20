package com.dbacademia.pessoa.controller;


import com.dbacademia.pessoa.dtos.pessoa.PessoaRequestDTO;
import com.dbacademia.pessoa.dtos.pessoa.PessoaResponseDTO;
import com.dbacademia.pessoa.service.PessoaService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/pessoas", produces = "application/json")
public class PessoaController {

    private final PessoaService service;

    public PessoaController(PessoaService service) {
        this.service = service;
    }

    @PostMapping
    // Use @Valid ou especifique o grupo OnCreate se ele existir no PessoaRequestDTO
    public ResponseEntity<PessoaResponseDTO> criar(@Valid @RequestBody PessoaRequestDTO dto) {
        PessoaResponseDTO pessoaCriada = service.criarPessoa(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(pessoaCriada);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PessoaResponseDTO> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody PessoaRequestDTO dto) {
        PessoaResponseDTO pessoaAtualizada = service.atualizarPessoa(id, dto);
        return ResponseEntity.ok(pessoaAtualizada);
    }

    @GetMapping
    @Operation(summary = "Listar pessoas")
    public ResponseEntity<Page<PessoaResponseDTO>> listar(@ParameterObject Pageable pageable) {
        // O retorno da service agora é Page<PessoaResponseDTO>
        Page<PessoaResponseDTO> pessoas = service.listarPessoas(pageable);
        return ResponseEntity.ok(pessoas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PessoaResponseDTO> buscar(@PathVariable Long id) {
        // O tipo deve ser PessoaResponseDTO
        PessoaResponseDTO pessoa = service.buscarPessoaPorId(id);
        return ResponseEntity.ok(pessoa);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.excluirPessoaPorId(id);
        return ResponseEntity.noContent().build();
    }
}

