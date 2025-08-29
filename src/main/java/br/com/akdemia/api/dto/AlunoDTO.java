package br.com.akdemia.api.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.UpdateTimestamp;

import br.com.akdemia.api.entity.Avaliacao;
import br.com.akdemia.api.entity.Treino;
import br.com.akdemia.api.enums.Objetivo;
import br.com.akdemia.api.enums.TipoUsuario;
import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AlunoDTO {

    private Long id;

    private String nome;

    private String email;

    private String cpf;

    private String telefone;

    private TipoUsuario tipo;
    
    private Boolean ativo = true;

    private String matricula;

    private BigDecimal peso;

    private BigDecimal altura;

    private Objetivo objetivo;

    private LocalDateTime dataCriacao;
    
    private LocalDateTime dataAtualizacao;

    private List<Treino> treinos = new ArrayList<>();

    private List<Avaliacao> avaliacoes = new ArrayList<>();
}