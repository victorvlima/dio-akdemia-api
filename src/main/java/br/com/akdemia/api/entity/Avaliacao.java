package br.com.akdemia.api.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import br.com.akdemia.api.enums.Objetivo;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tb_avaliacoes")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Avaliacao {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "personal_id", nullable = false)
    private Instrutor instrutor;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "aluno_id", nullable = false)
    private Aluno aluno;
    
    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private Objetivo objetivo;

    @DecimalMin(value = "0.0", message = "Peso deve ser positivo")
    @DecimalMax(value = "500.0", message = "Peso deve ser menor que 500kg")
    @Column(precision = 5, scale = 2)
    private BigDecimal peso;
    
    @DecimalMin(value = "0.0", message = "Altura deve ser positiva")
    @DecimalMax(value = "3.0", message = "Altura deve ser menor que 3m")
    @Column(precision = 3, scale = 2)
    private BigDecimal altura;
    
    @DecimalMin(value = "0.0", message = "Percentual de gordura deve ser positivo")
    @DecimalMax(value = "100.0", message = "Percentual de gordura deve ser menor que 100%")
    @Column(name = "percentual_gordura", precision = 5, scale = 2)
    private BigDecimal percentualGordura;
    
    @DecimalMin(value = "0.0", message = "Massa muscular deve ser positiva")
    @Column(name = "massa_muscular", precision = 5, scale = 2)
    private BigDecimal massaMuscular;
    
    @Size(max = 1000, message = "Observações devem ter no máximo 1000 caracteres")
    @Column(length = 1000)
    private String observacoes;
    
    @CreationTimestamp
    @Column(name = "criado_em", nullable = false, updatable = false)
    private LocalDateTime criadoEm;
}