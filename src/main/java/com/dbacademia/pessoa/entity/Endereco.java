package com.dbacademia.pessoa.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Entity
@Table (name = "Endereco")
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class Endereco {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Campo rua  é obrigatória")
    @Column(nullable = false)
    @NonNull
    private String rua;

    @NotBlank(message = "O número  é obrigatória")
    @Column(nullable = false)
    @NonNull
    private Integer numero;

    @NotBlank(message = "Cidade é obrigatória")
    @Column(nullable = false)
    @NonNull
    private String cidade;

    @NotBlank(message = "Estado  é obrigatória")
    @Column(nullable = false)
    @NonNull
    private String estado;

    @NotBlank(message = "O campo cep é obrigatória")
    @Column(nullable = false, unique = true)
    @NonNull
    private String cep;

    @ManyToMany
    @JoinColumn(name = "pessoa_id", nullable = false)
    @JsonBackReference
    private  Pessoa pessoa;
}
