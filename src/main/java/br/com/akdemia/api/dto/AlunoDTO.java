package br.com.akdemia.api.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import br.com.akdemia.api.enums.TipoUsuario;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para transferência de dados de Aluno.
 * 
 * Utilizado para comunicação entre as camadas da aplicação,
 * evitando exposição direta da entidade.
 * 
 * **Validações:** Aplicadas apenas nos campos obrigatórios para criação.
 * Para atualização, as validações são opcionais (campos podem ser null).
 * 
 * @author Sistema Akdemia
 * @version 1.0
 * @since 2025-01-29
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AlunoDTO {

    /**
     * Identificador único do aluno.
     * Gerado automaticamente pelo banco de dados.
     */
    private Long id;

    /**
     * Nome completo do aluno.
     * Obrigatório para criação, entre 2 e 100 caracteres.
     */
    @NotBlank(message = "Nome é obrigatório")
    @Size(min = 2, max = 100, message = "Nome deve ter entre 2 e 100 caracteres")
    private String nome;

    /**
     * Email do aluno.
     * Deve ser único no sistema e ter formato válido.
     */
    @Email(message = "Email deve ser válido")
    @NotBlank(message = "Email é obrigatório")
    private String email;

    /**
     * CPF do aluno.
     * Deve ser único no sistema e conter exatamente 11 dígitos.
     */
    @NotBlank(message = "CPF é obrigatório")
    @Pattern(regexp = "\\d{11}", message = "CPF deve conter 11 dígitos")
    private String cpf;

    /**
     * Telefone de contato do aluno.
     * Obrigatório para criação.
     */
    @NotBlank(message = "Telefone é obrigatório")
    private String telefone;

    /**
     * Tipo de usuário (ALUNO, INSTRUTOR, etc.).
     * Obrigatório para criação.
     */
    @NotNull(message = "Tipo de usuário é obrigatório")
    private TipoUsuario tipo;

    /**
     * Número de matrícula único do aluno.
     * Gerado automaticamente pelo sistema.
     */
    private String numeroMatricula;

    /**
     * Status ativo/inativo do aluno.
     * Usado para soft delete.
     */
    private Boolean ativo;

    /**
     * Data e hora de cadastro do aluno.
     * Preenchida automaticamente na criação.
     */
    private LocalDateTime dataCadastro;
    
    /**
     * Data e hora da última atualização.
     * Atualizada automaticamente a cada modificação.
     */
    private LocalDateTime dataAtualizacao;

    /**
     * Data e hora de desativação do aluno.
     * Preenchida quando o aluno é desativado (soft delete).
     */
    private LocalDateTime dataDesativacao;

    /**
     * Lista de IDs das matrículas do aluno.
     * Evita referências circulares e lazy loading.
     */
    private List<Long> matriculasIds = new ArrayList<>();

    /**
     * Lista de IDs dos treinos do aluno.
     * Evita referências circulares e lazy loading.
     */
    private List<Long> treinosIds = new ArrayList<>();

    /**
     * Lista de IDs das avaliações do aluno.
     * Evita referências circulares e lazy loading.
     */
    private List<Long> avaliacoesIds = new ArrayList<>();
}