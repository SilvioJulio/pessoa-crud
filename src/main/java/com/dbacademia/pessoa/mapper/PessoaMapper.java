
package com.dbacademia.pessoa.mapper;

import com.dbacademia.pessoa.dtos.endereco.EnderecoRequestDTO;
import com.dbacademia.pessoa.dtos.endereco.EnderecoResponseDTO;
import com.dbacademia.pessoa.dtos.pessoa.PessoaRequestDTO;
import com.dbacademia.pessoa.dtos.pessoa.PessoaResponseDTO;
import com.dbacademia.pessoa.entity.Endereco;
import com.dbacademia.pessoa.entity.Pessoa;
import org.mapstruct.*;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", imports = {
        java.time.Period.class,
        java.time.LocalDate.class,
        java.util.stream.Collectors.class
})
public interface PessoaMapper {

    @Mapping(target = "idade", expression = "java(pessoa.getDataNascimento() != null ? Period.between(pessoa.getDataNascimento(), LocalDate.now()).getYears() : null)")
    PessoaResponseDTO toResponseDTO(Pessoa pessoa);

    @Mapping(target = "cpf", expression = "java(pessoaRequestDTO.cpf() != null ? pessoaRequestDTO.cpf().trim() : null)")
    Pessoa toEntity(PessoaRequestDTO pessoaRequestDTO);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "cpf", ignore = true)
    @Mapping(target = "enderecos", expression = "java(updateEnderecos(dto.enderecos(), target))")
    void copyUpdatableFields(PessoaRequestDTO dto, @MappingTarget Pessoa target);

    EnderecoResponseDTO toEnderecoResponseDTO(Endereco endereco);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "pessoa", ignore = true)
    Endereco toEnderecoEntity(EnderecoRequestDTO enderecoRequestDTO);

    default List<Endereco> updateEnderecos(List<EnderecoRequestDTO> dtos, Pessoa target) {
        if (target.getEnderecos() != null) {
            target.getEnderecos().clear();
        }

        if (dtos == null || dtos.isEmpty()) return target.getEnderecos();

        List<Endereco> novos = dtos.stream()
                .map(dto -> {
                    Endereco e = toEnderecoEntity(dto);
                    e.setPessoa(target);
                    return e;
                }).collect(Collectors.toList());

        target.getEnderecos().addAll(novos);
        return target.getEnderecos();
    }

    @AfterMapping
    default void linkEnderecos(@MappingTarget Pessoa pessoa) {
        if (pessoa.getEnderecos() != null) {
            pessoa.getEnderecos().forEach(e -> e.setPessoa(pessoa));
        }
    }
}
