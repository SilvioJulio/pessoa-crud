package com.dbacademia.pessoa.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Table (name = "endereco")
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class Endereco {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "rua é obrigatória")
    @Column(nullable = false)
    private String rua;

    @NotNull(message = "O número  é obrigatória")
    @Column(nullable = false)
    private Integer numero;

    @NotBlank(message = "Bairro é obrigatória")
    @Column(nullable = false)
    private String bairro;

    @NotBlank(message = "Cidade é obrigatória")
    @Column(nullable = false)
    private String cidade;

    @NotBlank(message = "Estado  é obrigatória")
    @Column(nullable = false)
    private String estado;

    @NotBlank(message = "O campo cep é obrigatória")
    @Column(nullable = false)
    private String cep;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pessoa_id", nullable = false)
    private  Pessoa pessoa;

    @Column(nullable = false)
    private boolean principal;


}
