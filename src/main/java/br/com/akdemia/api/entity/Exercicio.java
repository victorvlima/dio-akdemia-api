package br.com.akdemia.api.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Builder;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import br.com.akdemia.api.enums.GrupoMuscular;
import br.com.akdemia.api.enums.TipoExercicio;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tb_exercicios")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Exercicio {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "Nome do exercício é obrigatório")
    @Size(min = 2, max = 100, message = "Nome deve ter entre 2 e 100 caracteres")
    @Column(nullable = false, length = 100)
    private String nome;
    
    @Size(max = 1000, message = "Descrição deve ter no máximo 1000 caracteres")
    @Column(length = 1000)
    private String descricao;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "grupo_muscular", nullable = false, length = 30)
    private GrupoMuscular grupoMuscular;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TipoExercicio tipo;
    
    @Size(max = 200, message = "Equipamento deve ter no máximo 200 caracteres")
    @Column(length = 200)
    private String equipamento;
    
    @Size(max = 500, message = "Instruções devem ter no máximo 500 caracteres")
    @Column(length = 500)
    private String instrucoes;
    
    @OneToMany(mappedBy = "exercicio", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    //@Builder.Default
    private List<ExercicioTreino> exercicioTreinos = new ArrayList<>();
    
    @CreationTimestamp
    @Column(name = "criado_em", nullable = false, updatable = false)
    private LocalDateTime criadoEm;
    
    @UpdateTimestamp
    @Column(name = "atualizado_em")
    private LocalDateTime atualizadoEm;
}