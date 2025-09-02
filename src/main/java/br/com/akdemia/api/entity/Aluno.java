package br.com.akdemia.api.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import br.com.akdemia.api.enums.TipoUsuario;
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
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Entidade que representa um Aluno no sistema Akdemia.
 * 
 * Responsável por armazenar informações pessoais, dados de contato,
 * controle de status e relacionamentos com outras entidades do sistema.
 * 
 * ## Características Principais
 * 
 * - **Soft Delete:** Utiliza campo `ativo` para desativação lógica
 * - **Auditoria:** Controle automático de datas de criação e atualização
 * - **Validação:** Bean Validation aplicada em todos os campos obrigatórios
 * - **Relacionamentos:** OneToMany com Matrícula, Treino e Avaliação
 * - **Unicidade:** Email, CPF e número de matrícula únicos no sistema
 * 
 * ## Regras de Negócio
 * 
 * - Email deve ser único e válido
 * - CPF deve ter exatamente 11 dígitos e ser único
 * - Número de matrícula é gerado automaticamente no formato AKD###
 * - Status ativo controla visibilidade nas consultas
 * - Relacionamentos são carregados sob demanda (LAZY)
 * 
 * ## Auditoria
 * 
 * - **dataCadastro:** Preenchida automaticamente na criação
 * - **dataAtualizacao:** Atualizada automaticamente a cada modificação
 * - **dataDesativacao:** Preenchida quando o aluno é desativado
 * 
 * @author Sistema Akdemia
 * @version 1.0
 * @since 2025-01-29
 */
@Entity
@Table(name = "tb_alunos")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@ToString(exclude = { "matriculas", "treinos", "avaliacoes" })
public class Aluno {

    /**
     * Identificador único do aluno.
     * Gerado automaticamente pela estratégia IDENTITY.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Email do aluno.
     * Deve ser único no sistema e ter formato válido.
     * Utilizado para login e comunicações.
     */
    @Email(message = "Email deve ser válido")
    @NotBlank(message = "Email é obrigatório")
    @Column(unique = true, nullable = false)
    private String email;

    /**
     * CPF do aluno.
     * Deve ser único no sistema e conter exatamente 11 dígitos.
     * Armazenado apenas com números, sem formatação.
     */
    @NotBlank(message = "CPF é obrigatório")
    @Pattern(regexp = "\\d{11}", message = "CPF deve conter 11 dígitos")
    @Column(unique = true, nullable = false, length = 11)
    private String cpf;

    /**
     * Nome completo do aluno.
     * Obrigatório, entre 2 e 100 caracteres.
     */
    @NotBlank(message = "Nome é obrigatório")
    @Size(min = 2, max = 100, message = "Nome deve ter entre 2 e 100 caracteres")
    @Column(nullable = false, length = 100)
    private String nome;

    /**
     * Telefone de contato do aluno.
     * Obrigatório para comunicações e emergências.
     * Suporta diferentes formatos de telefone.
     */
    @NotBlank(message = "Telefone é obrigatório")
    @Column(nullable = false, length = 15)
    private String telefone;

    /**
     * Tipo de usuário no sistema.
     * Define permissões e funcionalidades disponíveis.
     * Valores possíveis: ALUNO, INSTRUTOR, ADMIN, etc.
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoUsuario tipo;

    /**
     * Número de matrícula único do aluno.
     * Gerado automaticamente no formato AKD### (ex: AKD001, AKD002).
     * Utilizado para identificação rápida e relatórios.
     */
    @NotNull(message = "Matrícula é obrigatória")
    @Column(nullable = false, unique = true, length = 20)
    private String numeroMatricula;

    /**
     * Status ativo/inativo do aluno.
     * Utilizado para soft delete - alunos inativos não aparecem em listagens padrão.
     * 
     * - **true:** Aluno ativo no sistema
     * - **false:** Aluno desativado (soft delete)
     */
    @Column(nullable = false)
    private Boolean ativo = true;

    /**
     * Data e hora de cadastro do aluno.
     * Preenchida automaticamente na criação do registro.
     * Campo imutável após a criação.
     */
    @CreationTimestamp
    @Column(name = "data_cadastro", nullable = false, updatable = false)
    private LocalDateTime dataCadastro;

    /**
     * Data e hora da última atualização.
     * Atualizada automaticamente a cada modificação do registro.
     * Utilizada para auditoria e controle de versão.
     */
    @UpdateTimestamp
    @Column(name = "data_atualizacao")
    private LocalDateTime dataAtualizacao;

    /**
     * Data e hora de desativação do aluno.
     * Preenchida quando o aluno é desativado (soft delete).
     * Null para alunos ativos.
     */
    @UpdateTimestamp
    @Column(name = "data_desativacao")
    private LocalDateTime dataDesativacao;

    /**
     * Lista de matrículas do aluno em cursos.
     * Relacionamento OneToMany com a entidade Matrícula.
     * 
     * **Características:**
     * - Carregamento LAZY para otimização de performance
     * - Cascade ALL para operações em cascata
     * - Inicializada como ArrayList vazia
     */
    @OneToMany(mappedBy = "aluno", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Matricula> matriculas = new ArrayList<>();

    /**
     * Lista de treinos do aluno.
     * Relacionamento OneToMany com a entidade Treino.
     * 
     * **Características:**
     * - Carregamento LAZY para otimização de performance
     * - Cascade ALL para operações em cascata
     * - Inicializada como ArrayList vazia
     */
    @OneToMany(mappedBy = "aluno", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Treino> treinos = new ArrayList<>();

    /**
     * Lista de avaliações físicas do aluno.
     * Relacionamento OneToMany com a entidade Avaliacao.
     * 
     * **Características:**
     * - Carregamento LAZY para otimização de performance
     * - Cascade ALL para operações em cascata
     * - Inicializada como ArrayList vazia
     */
    @OneToMany(mappedBy = "aluno", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Avaliacao> avaliacoes = new ArrayList<>();
}