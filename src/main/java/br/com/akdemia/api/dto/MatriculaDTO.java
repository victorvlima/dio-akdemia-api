package br.com.akdemia.api.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

import br.com.akdemia.api.enums.StatusMatricula;

@Data
public class MatriculaDTO {
    private Long id;
    
    @NotNull(message = "Data de início é obrigatória")
    private LocalDate dataInicio;
    
    @NotNull(message = "Data de fim é obrigatória")
    private LocalDate dataFim;
    
    private StatusMatricula status;
    
    @NotNull(message = "ID do aluno é obrigatório")
    private Long alunoId;
    
    @NotNull(message = "ID do plano é obrigatório")
    private Long planoId;
    
    private String nomeAluno;
    private String nomePlano;
    private LocalDateTime dataCriacao;
}