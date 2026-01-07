package com.dbacademia.pessoa.controller;


import com.dbacademia.pessoa.entity.Pessoa;
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
    private  PessoaService service;

   @PostMapping
    public ResponseEntity<Pessoa> criar(@Valid @RequestBody Pessoa pessoa){
       return ResponseEntity.status(HttpStatus.CREATED)
               .body(service.criar(pessoa));

   }
   @GetMapping
   public ResponseEntity<Page<Pessoa>> listar (Pageable pageable){
       return ResponseEntity.ok(service.listarTodos(pageable));
   }
}
