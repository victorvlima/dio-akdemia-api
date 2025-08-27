package br.com.akdemia.api.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Builder;

import java.math.BigDecimal;

@Entity
@Table(name = "exercicio_treino")
@Data
@NoArgsConstructor
@AllArgsConstructor
//@Builder
public class ExercicioTreino {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "treino_id", nullable = false)
    private Treino treino;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exercicio_id", nullable = false)
    private Exercicio exercicio;
    
    @Min(value = 1, message = "Ordem deve ser pelo menos 1")
    @Column(nullable = false)
    private Integer ordem;
    
    @Min(value = 1, message = "Séries deve ser pelo menos 1")
    @Column(nullable = false)
    private Integer series;
    
    @Min(value = 1, message = "Repetições deve ser pelo menos 1")
    @Column(nullable = false)
    private Integer repeticoes;
    
    @DecimalMin(value = "0.0", message = "Peso deve ser positivo")
    @Column(precision = 6, scale = 2)
    private BigDecimal peso;
    
    @Min(value = 0, message = "Descanso deve ser positivo")
    @Column(name = "descanso_segundos")
    private Integer descansoSegundos;
    
    @Size(max = 200, message = "Observações devem ter no máximo 200 caracteres")
    @Column(length = 200)
    private String observacoes;
}