package com.dbacademia.pessoa.controller;


import com.dbacademia.pessoa.dtos.pessoa.PessoaRequestDTO;
import com.dbacademia.pessoa.dtos.pessoa.PessoaResponseDTO;
import com.dbacademia.pessoa.service.PessoaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/pessoas", produces = "application/json")
@Tag(name = "Pessoas", description = "Endpoints para gerenciamento de pessoas e endereços")
public class PessoaController {

    private final PessoaService service;

    public PessoaController(PessoaService service) {
        this.service = service;
    }

    @PostMapping
    @Operation(summary = "Criar uma nova pessoa", description = "Cadastra uma pessoa com pelo menos um endereço obrigatório.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Pessoa criada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Erro de validação ou CPF já existente"),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor")
    })
    public ResponseEntity<PessoaResponseDTO> criar(@Valid @RequestBody PessoaRequestDTO dto) {
        PessoaResponseDTO pessoaCriada = service.criarPessoa(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(pessoaCriada);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar uma pessoa", description = "Atualiza os dados de uma pessoa. O CPF não pode ser alterado.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pessoa atualizada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Tentativa de alterar CPF ou dados inválidos"),
            @ApiResponse(responseCode = "404", description = "Pessoa não encontrada"),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor")
    })
    public ResponseEntity<PessoaResponseDTO> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody PessoaRequestDTO dto) {
        PessoaResponseDTO pessoaAtualizada = service.atualizarPessoa(id, dto);
        return ResponseEntity.ok(pessoaAtualizada);
    }

    @GetMapping
    @Operation(summary = "Listar pessoas paginadas", description = "Retorna uma lista paginada de todas as pessoas cadastradas.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso"),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor")
    })
    public ResponseEntity<Page<PessoaResponseDTO>> listar(@ParameterObject Pageable pageable) {
        return ResponseEntity.ok(service.listarPessoas(pageable));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar pessoa por ID", description = "Retorna os detalhes de uma pessoa específica através do seu identificador.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pessoa localizada com sucesso"),
            @ApiResponse(responseCode = "404", description = "ID não encontrado no sistema")
    })
    public ResponseEntity<PessoaResponseDTO> buscar(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPessoaPorId(id));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir uma pessoa", description = "Remove permanentemente uma pessoa e seus endereços vinculados.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Pessoa excluída com sucesso (Sem conteúdo)"),
            @ApiResponse(responseCode = "404", description = "ID informado para exclusão não existe")
    })
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.excluirPessoaPorId(id);
        return ResponseEntity.noContent().build();
    }
}


