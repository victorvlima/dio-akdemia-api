package br.com.akdemia.api.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "personais")
@Data
//@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
//@SuperBuilder
public class Personal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "CREF é obrigatório")
    @Column(nullable = false, unique = true, length = 20)
    private String cref;
    
    @NotBlank(message = "Especialidade é obrigatória")
    @Size(max = 200, message = "Especialidade deve ter no máximo 200 caracteres")
    @Column(nullable = false, length = 200)
    private String especialidade;
    
    @DecimalMin(value = "0.0", message = "Valor da hora deve ser positivo")
    @Column(name = "valor_hora", precision = 8, scale = 2)
    private BigDecimal valorHora;
    
    @Min(value = 0, message = "Anos de experiência deve ser positivo")
    @Column(name = "anos_experiencia")
    private Integer anosExperiencia;
    
    @OneToMany(mappedBy = "personal", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Treino> treinos = new ArrayList<>();
    
    @OneToMany(mappedBy = "personal", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Avaliacao> avaliacoes = new ArrayList<>();
}