package com.dbacademia.pessoa.repository;

import com.dbacademia.pessoa.entity.Pessoa;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PessoaRepository  extends JpaRepository<Pessoa,Long> {
             boolean existsByCpf(String cpf);

            boolean existsByCpfAndIdNot(String cpf, Long id);
}
