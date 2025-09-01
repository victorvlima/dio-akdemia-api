package br.com.akdemia.api.repository;

import br.com.akdemia.api.entity.Aluno;
import br.com.akdemia.api.enums.TipoUsuario;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repository para operações de persistência da entidade Aluno.
 * 
 * <p>Fornece métodos para CRUD completo, incluindo:</p>
 * <ul>
 *   <li>Operações básicas (busca, criação, atualização)</li>
 *   <li>Soft delete (desativação/reativação)</li>
 *   <li>Validações de unicidade</li>
 *   <li>Buscas avançadas e filtros</li>
 *   <li>Relatórios e estatísticas</li>
 *   <li>Auditoria e controle de datas</li>
 * </ul>
 * 
 * <h3>Padrões de Nomenclatura:</h3>
 * <ul>
 *   <li><strong>findBy*</strong> - Buscas que retornam entidades</li>
 *   <li><strong>existsBy*</strong> - Verificações de existência (boolean)</li>
 *   <li><strong>countBy*</strong> - Contadores (Long)</li>
 *   <li><strong>*AndAtivoTrue</strong> - Considera apenas registros ativos</li>
 * </ul>
 * 
 * <h3>Soft Delete:</h3>
 * <p>A entidade Aluno utiliza soft delete através do campo 'ativo'. 
 * Registros não são removidos fisicamente, apenas marcados como inativos.</p>
 * 
 * <h3>Uso Recomendado:</h3>
 * <ul>
 *   <li><strong>Criação:</strong> Use existsByEmailAndAtivoTrue() para validar unicidade</li>
 *   <li><strong>Atualização:</strong> Use existsByEmailAndAtivoTrueAndIdNot() para validar</li>
 *   <li><strong>Listagens:</strong> Use findByAtivoTrue() com paginação</li>
 *   <li><strong>Exclusão:</strong> Use desativarAluno() ao invés de delete()</li>
 * </ul>
 * 
 * @author Sistema Akdemia
 * @version 1.0
 * @since 2025-01-29
 */
@Repository
public interface AlunoRepository extends JpaRepository<Aluno, Long> {
    
    // ========== BUSCAS BÁSICAS ==========
    
    /**
     * Busca aluno por email (incluindo inativos).
     * 
     * @param email Email do aluno
     * @return Optional contendo o aluno se encontrado
     */
    Optional<Aluno> findByEmail(String email);
    
    /**
     * Busca aluno por CPF (incluindo inativos).
     * 
     * @param cpf CPF do aluno (11 dígitos)
     * @return Optional contendo o aluno se encontrado
     */
    Optional<Aluno> findByCpf(String cpf);
    
    /**
     * Busca aluno por número de matrícula (incluindo inativos).
     * 
     * @param numeroMatricula Número de matrícula único
     * @return Optional contendo o aluno se encontrado
     */
    Optional<Aluno> findByNumeroMatricula(String numeroMatricula);
    
    /**
     * Busca aluno ativo por ID.
     * Utilizado para operações que devem considerar apenas alunos ativos.
     * 
     * @param id ID do aluno
     * @return Optional contendo o aluno se encontrado e ativo
     */
    Optional<Aluno> findByIdAndAtivoTrue(Long id);
    
    // ========== VERIFICAÇÕES DE EXISTÊNCIA ==========
    
    /**
     * Verifica se existe aluno com o email informado (incluindo inativos).
     * 
     * @param email Email para verificação
     * @return true se existe aluno com este email
     */
    boolean existsByEmail(String email);
    
    /**
     * Verifica se existe aluno com o CPF informado (incluindo inativos).
     * 
     * @param cpf CPF para verificação
     * @return true se existe aluno com este CPF
     */
    boolean existsByCpf(String cpf);
    
    /**
     * Verifica se existe aluno com o número de matrícula informado (incluindo inativos).
     * 
     * @param numeroMatricula Número de matrícula para verificação
     * @return true se existe aluno com este número de matrícula
     */
    boolean existsByNumeroMatricula(String numeroMatricula);
    
    /**
     * Verifica se existe aluno ATIVO com o email informado.
     * Utilizado para validações de unicidade considerando apenas registros ativos.
     * 
     * <p><strong>Uso:</strong> Validação na criação de novos alunos</p>
     * 
     * @param email Email para verificação
     * @return true se existe aluno ativo com este email
     */
    boolean existsByEmailAndAtivoTrue(String email);
    
    /**
     * Verifica se existe aluno ATIVO com o CPF informado.
     * Utilizado para validações de unicidade considerando apenas registros ativos.
     * 
     * <p><strong>Uso:</strong> Validação na criação de novos alunos</p>
     * 
     * @param cpf CPF para verificação
     * @return true se existe aluno ativo com este CPF
     */
    boolean existsByCpfAndAtivoTrue(String cpf);
    
    /**
     * Verifica se existe aluno ATIVO com o número de matrícula informado.
     * Utilizado para validações de unicidade considerando apenas registros ativos.
     * 
     * <p><strong>Uso:</strong> Validação na criação de novos alunos</p>
     * 
     * @param numeroMatricula Número de matrícula para verificação
     * @return true se existe aluno ativo com este número de matrícula
     */
    boolean existsByNumeroMatriculaAndAtivoTrue(String numeroMatricula);
    
    /**
     * Verifica se existe outro aluno ativo com o mesmo email (excluindo o aluno atual).
     * Utilizado em operações de atualização para validar unicidade.
     * 
     * <p><strong>Uso:</strong> Validação na atualização de alunos existentes</p>
     * 
     * @param email Email para verificação
     * @param id ID do aluno atual (para excluir da busca)
     * @return true se existe outro aluno ativo com este email
     */
    boolean existsByEmailAndAtivoTrueAndIdNot(String email, Long id);
    
    /**
     * Verifica se existe outro aluno ativo com o mesmo CPF (excluindo o aluno atual).
     * Utilizado em operações de atualização para validar unicidade.
     * 
     * <p><strong>Uso:</strong> Validação na atualização de alunos existentes</p>
     * 
     * @param cpf CPF para verificação
     * @param id ID do aluno atual (para excluir da busca)
     * @return true se existe outro aluno ativo com este CPF
     */
    boolean existsByCpfAndAtivoTrueAndIdNot(String cpf, Long id);
    
    // ========== FILTROS POR STATUS E TIPO ==========
    
    /**
     * Busca todos os alunos ativos.
     * 
     * <p><strong>Performance:</strong> Para listas grandes, prefira a versão com paginação</p>
     * 
     * @return Lista de alunos ativos
     */
    List<Aluno> findByAtivoTrue();
    
    /**
     * Busca todos os alunos ativos com paginação.
     * 
     * <p><strong>Uso recomendado:</strong> Para listagens em interfaces de usuário</p>
     * 
     * @param pageable Configuração de paginação
     * @return Página de alunos ativos
     */
    Page<Aluno> findByAtivoTrue(Pageable pageable);
    
    /**
     * Busca alunos ativos por tipo de usuário.
     * 
     * @param tipo Tipo de usuário (ALUNO, INSTRUTOR, etc.)
     * @return Lista de alunos ativos do tipo especificado
     */
    List<Aluno> findByTipoAndAtivoTrue(TipoUsuario tipo);
    
    /**
     * Busca todos os alunos inativos.
     * 
     * <p><strong>Uso:</strong> Relatórios de alunos desativados</p>
     * 
     * @return Lista de alunos desativados
     */
    List<Aluno> findByAtivoFalse();
    
    /**
     * Busca todos os alunos inativos com paginação.
     * 
     * @param pageable Configuração de paginação
     * @return Página de alunos desativados
     */
    Page<Aluno> findByAtivoFalse(Pageable pageable);
    
    // ========== BUSCAS POR NOME ==========
    
    /**
     * Busca alunos ativos por nome (busca parcial, case insensitive).
     * 
     * <p><strong>Comportamento:</strong> Busca por substring no nome, ignorando maiúsculas/minúsculas</p>
     * 
     * @param nome Nome ou parte do nome para busca
     * @return Lista de alunos ativos que contêm o nome especificado
     */
    List<Aluno> findByNomeContainingIgnoreCaseAndAtivoTrue(String nome);
    
    // ========== BUSCAS AVANÇADAS ==========
    
    /**
     * Busca alunos ativos com filtros múltiplos e paginação.
     * Permite filtrar por nome e/ou tipo simultaneamente.
     * 
     * <p><strong>Flexibilidade:</strong> Parâmetros null são ignorados no filtro</p>
     * <p><strong>Performance:</strong> Otimizada para grandes volumes de dados</p>
     * 
     * @param nome Nome para filtro (opcional - pode ser null)
     * @param tipo Tipo de usuário para filtro (opcional - pode ser null)
     * @param pageable Configuração de paginação
     * @return Página de alunos filtrados
     */
    @Query("SELECT a FROM Aluno a WHERE " +
           "(:nome IS NULL OR LOWER(a.nome) LIKE LOWER(CONCAT('%', :nome, '%'))) AND " +
           "(:tipo IS NULL OR a.tipo = :tipo) AND " +
           "a.ativo = true")
    Page<Aluno> findByFiltros(@Param("nome") String nome, 
                             @Param("tipo") TipoUsuario tipo, 
                             Pageable pageable);
    
    /**
     * Busca alunos ativos sem matrículas.
     * Útil para identificar alunos cadastrados mas não matriculados em cursos.
     * 
     * <p><strong>Uso:</strong> Relatórios de alunos pendentes de matrícula</p>
     * 
     * @return Lista de alunos ativos sem matrículas
     */
    @Query("SELECT a FROM Aluno a WHERE a.ativo = true AND SIZE(a.matriculas) = 0")
    List<Aluno> findAlunosSemMatricula();
    
    // ========== BUSCAS POR PERÍODO ==========
    
    /**
     * Busca alunos cadastrados em um período específico.
     * 
     * <p><strong>Uso:</strong> Relatórios de crescimento, análises temporais</p>
     * 
     * @param dataInicio Data inicial do período (inclusive)
     * @param dataFim Data final do período (inclusive)
     * @return Lista de alunos cadastrados no período
     */
    @Query("SELECT a FROM Aluno a WHERE a.dataCadastro BETWEEN :dataInicio AND :dataFim")
    List<Aluno> findAlunosCadastradosNoPeriodo(@Param("dataInicio") LocalDateTime dataInicio,
                                              @Param("dataFim") LocalDateTime dataFim);
    
    /**
     * Busca alunos desativados em um período específico.
     * 
     * <p><strong>Uso:</strong> Análise de churn, relatórios de cancelamentos</p>
     * 
     * @param dataInicio Data inicial do período (inclusive)
     * @param dataFim Data final do período (inclusive)
     * @return Lista de alunos desativados no período
     */
    @Query("SELECT a FROM Aluno a WHERE a.ativo = false AND " +
           "a.dataDesativacao BETWEEN :dataInicio AND :dataFim")
    List<Aluno> findAlunosDesativadosNoPeriodo(@Param("dataInicio") LocalDateTime dataInicio,
                                              @Param("dataFim") LocalDateTime dataFim);
    
    /**
     * Busca alunos por status e período de cadastro com paginação.
     * 
     * <p><strong>Flexibilidade:</strong> Permite filtrar ativos ou inativos por período</p>
     * 
     * @param ativo Status do aluno (true para ativo, false para inativo)
     * @param dataInicio Data inicial do período (inclusive)
     * @param dataFim Data final do período (inclusive)
     * @param pageable Configuração de paginação
     * @return Página de alunos filtrados
     */
    @Query("SELECT a FROM Aluno a WHERE a.ativo = :ativo AND " +
           "a.dataCadastro BETWEEN :dataInicio AND :dataFim")
    Page<Aluno> findByAtivoAndDataCadastroBetween(@Param("ativo") Boolean ativo,
                                                  @Param("dataInicio") LocalDateTime dataInicio,
                                                  @Param("dataFim") LocalDateTime dataFim,
                                                  Pageable pageable);
    
    // ========== CONTADORES E ESTATÍSTICAS ==========
    
    /**
     * Conta total de alunos ativos.
     * 
     * <p><strong>Performance:</strong> Mais eficiente que count() do JpaRepository para filtros</p>
     * 
     * @return Número de alunos ativos no sistema
     */
    @Query("SELECT COUNT(a) FROM Aluno a WHERE a.ativo = true")
    Long countByAtivoTrue();
    
    /**
     * Conta alunos ativos por tipo de usuário.
     * 
     * <p><strong>Uso:</strong> Dashboards, relatórios estatísticos</p>
     * 
     * @param tipo Tipo de usuário
     * @return Número de alunos ativos do tipo especificado
     */
    @Query("SELECT COUNT(a) FROM Aluno a WHERE a.tipo = :tipo AND a.ativo = true")
    Long countByTipoAndAtivoTrue(@Param("tipo") TipoUsuario tipo);
    
    /**
     * Conta total de alunos (ativos e inativos).
     * 
     * <p><strong>Uso:</strong> Estatísticas gerais do sistema</p>
     * 
     * @return Total de alunos no sistema
     */
    @Query("SELECT COUNT(a) FROM Aluno a")
    Long countTotalAlunos();
    
    /**
     * Conta alunos inativos.
     * 
     * <p><strong>Uso:</strong> Análise de churn, relatórios de cancelamentos</p>
     * 
     * @return Número de alunos desativados
     */
    @Query("SELECT COUNT(a) FROM Aluno a WHERE a.ativo = false")
    Long countAlunosInativos();
    
    /**
     * Estatísticas de alunos ativos agrupados por tipo.
     * Retorna array de objetos onde [0] = TipoUsuario e [1] = Long (count).
     * 
     * <p><strong>Uso:</strong> Dashboards, gráficos de distribuição por tipo</p>
     * <p><strong>Formato de retorno:</strong> List&lt;Object[]&gt; onde cada array contém [TipoUsuario, Long]</p>
     * 
     * @return Lista de arrays com tipo e quantidade de alunos
     */
    @Query("SELECT a.tipo, COUNT(a) FROM Aluno a WHERE a.ativo = true GROUP BY a.tipo")
    List<Object[]> countAlunosAtivosPorTipo();
    
    // ========== GERAÇÃO DE MATRÍCULA ==========
    
    /**
     * Busca o próximo número de matrícula disponível.
     * Analisa matrículas existentes no formato 'AKD###' e retorna o próximo número sequencial.
     * 
     * <p><strong>Formato:</strong> AKD001, AKD002, AKD003...</p>
     * <p><strong>Comportamento:</strong> Se não houver matrículas, retorna 1</p>
     * 
     * @return Próximo número sequencial de matrícula
     */
    @Query("SELECT COALESCE(MAX(CAST(SUBSTRING(a.numeroMatricula, 4) AS int)), 0) + 1 " +
           "FROM Aluno a WHERE a.numeroMatricula LIKE 'AKD%'")
    Integer findNextMatriculaNumber();
    
    // ========== OPERAÇÕES DE SOFT DELETE ==========
    
    /**
     * Desativa um aluno (soft delete).
     * Marca o aluno como inativo e registra a data de desativação.
     * 
     * <p><strong>Comportamento:</strong> Apenas alunos ativos podem ser desativados</p>
     * <p><strong>Auditoria:</strong> Preserva todos os dados históricos</p>
     * 
     * @param id ID do aluno a ser desativado
     * @param dataDesativacao Data da desativação
     * @return Número de registros afetados (0 se aluno não encontrado ou já inativo)
     */
    @Modifying
    @Transactional
    @Query("UPDATE Aluno a SET a.ativo = false, a.dataDesativacao = :dataDesativacao " +
           "WHERE a.id = :id AND a.ativo = true")
    int desativarAluno(@Param("id") Long id, @Param("dataDesativacao") LocalDateTime dataDesativacao);
    
    /**
     * Reativa um aluno previamente desativado.
     * Marca o aluno como ativo, remove a data de desativação e atualiza a data de modificação.
     * 
     * <p><strong>Comportamento:</strong> Apenas alunos inativos podem ser reativados</p>
     * <p><strong>Auditoria:</strong> Atualiza automaticamente dataAtualizacao</p>
     * 
     * @param id ID do aluno a ser reativado
     * @return Número de registros afetados (0 se aluno não encontrado ou já ativo)
     */
    @Modifying
    @Transactional
    @Query("UPDATE Aluno a SET a.ativo = true, a.dataDesativacao = null, a.dataAtualizacao = CURRENT_TIMESTAMP " +
           "WHERE a.id = :id AND a.ativo = false")
    int reativarAluno(@Param("id") Long id);
    
    // ========== OPERAÇÕES DE AUDITORIA ==========
    
    /**
     * Atualiza manualmente a data de última modificação do aluno.
     * 
     * <p><strong>Atenção:</strong> Geralmente não é necessário pois o @UpdateTimestamp faz isso automaticamente</p>
     * <p><strong>Uso:</strong> Casos específicos onde é necessário controle manual da auditoria</p>
     * 
     * @param id ID do aluno
     * @param dataAtualizacao Nova data de atualização
     */
    @Modifying
    @Transactional
    @Query("UPDATE Aluno a SET a.dataAtualizacao = :dataAtualizacao WHERE a.id = :id")
    void updateDataAtualizacao(@Param("id") Long id, @Param("dataAtualizacao") LocalDateTime dataAtualizacao);
}