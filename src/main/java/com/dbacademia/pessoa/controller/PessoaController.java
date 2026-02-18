package com.dbacademia.pessoa.controller;


import com.dbacademia.pessoa.dtos.PessoaDTO;
import com.dbacademia.pessoa.mapper.PessoaMapper;
import com.dbacademia.pessoa.service.PessoaService;
import io.swagger.v3.oas.annotations.Operation;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(value = "/pessoas", produces = "application/json") // Adicionado produces
public class PessoaController {

    @Autowired
    private PessoaService service;

    @PostMapping
    public ResponseEntity<PessoaDTO> criar(@Validated(PessoaDTO.OnCreate.class) @RequestBody PessoaDTO dto) {
        var entity = PessoaMapper.toEntity(dto); // aqui CPF é aceito
        var resp = service.criar(entity);
        return ResponseEntity.status(HttpStatus.CREATED).body(resp);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PessoaDTO> atualizar(
            @PathVariable Long id,
            @Validated(PessoaDTO.OnUpdate.class) @RequestBody PessoaDTO dto ) {
        var entity = PessoaMapper.toEntity(dto); // aqui CPF é aceito
        var resp = service.atualizar(id, entity);
        return ResponseEntity.ok(resp);
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
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletarPorId(id);
        return ResponseEntity.noContent().build();
    }
}
