package com.dbacademia.pessoa.util;

import com.dbacademia.pessoa.dtos.PessoaDTO;
import com.dbacademia.pessoa.entity.Endereco;
import com.dbacademia.pessoa.entity.Pessoa;
import net.datafaker.Faker;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class PessoaCreator {
    private static final Faker faker = new Faker(new Locale("pt-BR"));

    public static PessoaDTO createPesssoaDTO() {
        return new PessoaDTO(
                null,
                faker.name().fullName(),
                faker.number().digits(11),
                LocalDate.now().minusYears(faker.number().numberBetween(18, 60)),
                35,
                new ArrayList<>()
        );

    }

    public static Pessoa createPessoaEntity() {
        Pessoa pessoa = new Pessoa();
        pessoa.setNome(faker.name().fullName());
        pessoa.setCpf(faker.number().digits(11));
        pessoa.setDataNascimento(LocalDate.now().minusYears(25));

        List<Endereco> enderecos = new ArrayList<>();

        Endereco endereco = new Endereco();
        endereco.setRua(faker.address().streetName());
        endereco.setNumero(Integer.parseInt(faker.address().streetAddressNumber()));
        endereco.setBairro(faker.address().secondaryAddress()); // ou outro campo de bairro
        endereco.setCidade(faker.address().city());
        endereco.setEstado(faker.address().stateAbbr());
        endereco.setCep(faker.address().zipCode());
        endereco.setPrincipal(true);
        endereco.setPessoa(pessoa); // Vínculo importante para o JPA

        enderecos.add(endereco);
        pessoa.setEnderecos(enderecos);

        return pessoa;
    }
}
