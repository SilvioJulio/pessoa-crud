package com.dbacademia.pessoa.config;

import com.dbacademia.pessoa.repository.PessoaRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.dbacademia.pessoa.util.PessoaCreator;

@Configuration
public class DatabaseSeeder {
    @Bean
    CommandLineRunner initDatabase(PessoaRepository repository) {
        return args -> {
            if (repository.count() == 0) {
                for (int i = 0; i < 10; i++) {
                    repository.save(PessoaCreator.createPessoaEntity());
                }
            }
        };
    }
}
