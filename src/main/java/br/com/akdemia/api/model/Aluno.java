package br.com.akdemia.api.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "alunos")
@Data
// @EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Aluno {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Matrícula é obrigatória")
    @Column(nullable = false, unique = true, length = 20)
    private String matricula;

    @DecimalMin(value = "0.0", message = "Peso deve ser positivo")
    @DecimalMax(value = "500.0", message = "Peso deve ser menor que 500kg")
    @Column(precision = 5, scale = 2)
    private BigDecimal peso;

    @DecimalMin(value = "0.0", message = "Altura deve ser positiva")
    @DecimalMax(value = "3.0", message = "Altura deve ser menor que 3m")
    @Column(precision = 3, scale = 2)
    private BigDecimal altura;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private Objetivo objetivo;

    @Column(name = "data_matricula", nullable = false)
    private LocalDate dataMatricula = LocalDate.now();

    @Column(name = "data_vencimento")
    private LocalDate dataVencimento;

    @OneToMany(mappedBy = "aluno", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Treino> treinos = new ArrayList<>();

    @OneToMany(mappedBy = "aluno", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Avaliacao> avaliacoes = new ArrayList<>();
}