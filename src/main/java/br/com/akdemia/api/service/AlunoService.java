package br.com.akdemia.api.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.akdemia.api.dto.AlunoDTO;
import br.com.akdemia.api.entity.Aluno;
import br.com.akdemia.api.enums.TipoUsuario;
import br.com.akdemia.api.exception.BusinessException;
import br.com.akdemia.api.exception.ResourceNotFoundException;
import br.com.akdemia.api.mapper.AlunoMapper;
import br.com.akdemia.api.repository.AlunoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Service para operações de negócio relacionadas à entidade Aluno.
 * 
 * Responsável por:
 * 
 * - Operações CRUD completas (criar, buscar, atualizar, desativar)
 * - Validações de regras de negócio
 * - Geração automática de números de matrícula
 * - Soft delete (desativação/reativação)
 * - Buscas avançadas e filtros
 * - Controle de transações
 * 
 * ## Regras de Negócio
 * 
 * - Email deve ser único entre alunos ativos
 * - CPF deve ser único entre alunos ativos
 * - Número de matrícula é gerado automaticamente
 * - Exclusão é sempre soft delete (desativação)
 * - Apenas alunos ativos aparecem em listagens padrão
 * 
 * ## Transações
 * 
 * - **Leitura:** @Transactional(readOnly = true) para otimização
 * - **Escrita:** @Transactional para garantir consistência
 * 
 * @author Sistema Akdemia
 * @version 1.0
 * @since 2025-01-29
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class AlunoService {
    
    private final AlunoRepository alunoRepository;
    private final AlunoMapper alunoMapper;
    
    // ========== OPERAÇÕES DE CRIAÇÃO ==========
    
    /**
     * Cria um novo aluno no sistema.
     * 
     * **Validações aplicadas:**
     * 
     * - Email único entre alunos ativos
     * - CPF único entre alunos ativos
     * - Dados obrigatórios preenchidos
     * 
     * **Comportamento:**
     * 
     * - Gera número de matrícula automaticamente
     * - Define status como ativo
     * - Registra data de cadastro automaticamente
     * 
     * @param alunoDTO Dados do aluno a ser criado
     * @return DTO do aluno criado com ID e dados de auditoria
     * @throws BusinessException se email ou CPF já existir entre alunos ativos
     * @throws IllegalArgumentException se dados obrigatórios estiverem ausentes
     */
    public AlunoDTO criar(AlunoDTO alunoDTO) {
        log.info("Iniciando criação de novo aluno: {}", alunoDTO.getEmail());
        
        // Validações de negócio
        validarDadosObrigatorios(alunoDTO);
        validarAlunoUnico(alunoDTO);
        
        // Gerar número de matrícula
        String numeroMatricula = gerarNumeroMatricula();
        alunoDTO.setNumeroMatricula(numeroMatricula);
        
        // Converter e salvar
        Aluno aluno = alunoMapper.toEntityForCreation(alunoDTO);
        aluno = alunoRepository.save(aluno);
        
        log.info("Aluno criado com sucesso. ID: {}, Matrícula: {}", aluno.getId(), aluno.getNumeroMatricula());
        return alunoMapper.toDTO(aluno);
    }
    
    // ========== OPERAÇÕES DE BUSCA ==========
    
    /**
     * Busca aluno por ID (incluindo inativos).
     * 
     * **Comportamento:** Retorna aluno independente do status ativo/inativo
     * 
     * @param id ID do aluno
     * @return DTO do aluno encontrado
     * @throws ResourceNotFoundException se aluno não for encontrado
     */
    @Transactional(readOnly = true)
    public AlunoDTO buscarPorId(Long id) {
        log.info("Buscando aluno por ID: {}", id);
        
        Aluno aluno = alunoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Aluno não encontrado com ID: " + id));
        
        return alunoMapper.toDTO(aluno);
    }
    
    /**
     * Busca aluno ativo por ID.
     * 
     * **Comportamento:** Retorna apenas se o aluno estiver ativo  
     * **Uso:** Operações que devem considerar apenas alunos ativos
     * 
     * @param id ID do aluno
     * @return DTO do aluno ativo encontrado
     * @throws ResourceNotFoundException se aluno não for encontrado ou estiver inativo
     */
    @Transactional(readOnly = true)
    public AlunoDTO buscarAtivoPorId(Long id) {
        log.info("Buscando aluno ativo por ID: {}", id);
        
        Aluno aluno = alunoRepository.findByIdAndAtivoTrue(id)
                .orElseThrow(() -> new ResourceNotFoundException("Aluno ativo não encontrado com ID: " + id));
        
        return alunoMapper.toDTO(aluno);
    }
    
    /**
     * Busca aluno por email (incluindo inativos).
     * 
     * @param email Email do aluno
     * @return DTO do aluno encontrado
     * @throws ResourceNotFoundException se aluno não for encontrado
     */
    @Transactional(readOnly = true)
    public AlunoDTO buscarPorEmail(String email) {
        log.info("Buscando aluno por email: {}", email);
        
        Aluno aluno = alunoRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Aluno não encontrado com email: " + email));
        
        return alunoMapper.toDTO(aluno);
    }
    
    /**
     * Busca aluno por CPF (incluindo inativos).
     * 
     * @param cpf CPF do aluno
     * @return DTO do aluno encontrado
     * @throws ResourceNotFoundException se aluno não for encontrado
     */
    @Transactional(readOnly = true)
    public AlunoDTO buscarPorCpf(String cpf) {
        log.info("Buscando aluno por CPF: {}", cpf);
        
        Aluno aluno = alunoRepository.findByCpf(cpf)
                .orElseThrow(() -> new ResourceNotFoundException("Aluno não encontrado com CPF: " + cpf));
        
        return alunoMapper.toDTO(aluno);
    }
    
    /**
     * Busca aluno por número de matrícula (incluindo inativos).
     * 
     * @param numeroMatricula Número de matrícula do aluno
     * @return DTO do aluno encontrado
     * @throws ResourceNotFoundException se aluno não for encontrado
     */
    @Transactional(readOnly = true)
    public AlunoDTO buscarPorMatricula(String numeroMatricula) {
        log.info("Buscando aluno por matrícula: {}", numeroMatricula);
        
        Aluno aluno = alunoRepository.findByNumeroMatricula(numeroMatricula)
                .orElseThrow(() -> new ResourceNotFoundException("Aluno não encontrado com matrícula: " + numeroMatricula));
        
        return alunoMapper.toDTO(aluno);
    }
    
    // ========== OPERAÇÕES DE LISTAGEM ==========
    
    /**
     * Lista todos os alunos ativos.
     * 
     * **Performance:** Para grandes volumes, prefira listarTodosComPaginacao()  
     * **Uso:** Relatórios, exportações, operações que precisam de todos os registros
     * 
     * @return Lista de DTOs de alunos ativos
     */
    @Transactional(readOnly = true)
    public List<AlunoDTO> listarTodos() {
        log.info("Listando todos os alunos ativos");
        
        List<Aluno> alunos = alunoRepository.findByAtivoTrue();
        return alunoMapper.toDTOSimpleList(alunos);
    }
    
    /**
     * Lista todos os alunos ativos com paginação.
     * 
     * **Uso recomendado:** Interfaces de usuário, APIs REST  
     * **Performance:** Otimizada para grandes volumes
     * 
     * @param pageable Configuração de paginação
     * @return Página de DTOs de alunos ativos
     */
    @Transactional(readOnly = true)
    public Page<AlunoDTO> listarTodosComPaginacao(Pageable pageable) {
        log.info("Listando alunos ativos com paginação: página {}, tamanho {}", 
                pageable.getPageNumber(), pageable.getPageSize());
        
        Page<Aluno> alunos = alunoRepository.findByAtivoTrue(pageable);
        return alunos.map(alunoMapper::toDTOSimple);
    }
    
    /**
     * Lista alunos ativos por tipo de usuário.
     * 
     * @param tipo Tipo de usuário (ALUNO, INSTRUTOR, etc.)
     * @return Lista de DTOs de alunos do tipo especificado
     */
    @Transactional(readOnly = true)
    public List<AlunoDTO> listarPorTipo(TipoUsuario tipo) {
        log.info("Listando alunos ativos por tipo: {}", tipo);
        
        List<Aluno> alunos = alunoRepository.findByTipoAndAtivoTrue(tipo);
        return alunoMapper.toDTOSimpleList(alunos);
    }
    
    /**
     * Lista todos os alunos inativos.
     * 
     * **Uso:** Relatórios de alunos desativados, auditoria
     * 
     * @return Lista de DTOs de alunos inativos
     */
    @Transactional(readOnly = true)
    public List<AlunoDTO> listarInativos() {
        log.info("Listando todos os alunos inativos");
        
        List<Aluno> alunos = alunoRepository.findByAtivoFalse();
        return alunoMapper.toDTOSimpleList(alunos);
    }
    
    // ========== OPERAÇÕES DE BUSCA AVANÇADA ==========
    
    /**
     * Busca alunos ativos por nome (busca parcial, case insensitive).
     * 
     * **Comportamento:** Busca por substring no nome
     * 
     * @param nome Nome ou parte do nome para busca
     * @return Lista de DTOs de alunos que contêm o nome especificado
     */
    @Transactional(readOnly = true)
    public List<AlunoDTO> buscarPorNome(String nome) {
        log.info("Buscando alunos ativos por nome: {}", nome);
        
        List<Aluno> alunos = alunoRepository.findByNomeContainingIgnoreCaseAndAtivoTrue(nome);
        return alunoMapper.toDTOSimpleList(alunos);
    }
    
    /**
     * Busca alunos ativos com filtros múltiplos e paginação.
     * 
     * **Flexibilidade:** Parâmetros null são ignorados
     * 
     * @param nome Nome para filtro (opcional)
     * @param tipo Tipo de usuário para filtro (opcional)
     * @param pageable Configuração de paginação
     * @return Página de DTOs de alunos filtrados
     */
    @Transactional(readOnly = true)
    public Page<AlunoDTO> buscarComFiltros(String nome, TipoUsuario tipo, Pageable pageable) {
        log.info("Buscando alunos com filtros - Nome: {}, Tipo: {}", nome, tipo);
        
        Page<Aluno> alunos = alunoRepository.findByFiltros(nome, tipo, pageable);
        return alunos.map(alunoMapper::toDTOSimple);
    }
    
    /**
     * Busca alunos ativos sem matrículas em cursos.
     * 
     * **Uso:** Identificar alunos pendentes de matrícula
     * 
     * @return Lista de DTOs de alunos sem matrículas
     */
    @Transactional(readOnly = true)
    public List<AlunoDTO> buscarSemMatricula() {
        log.info("Buscando alunos ativos sem matrícula em cursos");
        
        List<Aluno> alunos = alunoRepository.findAlunosSemMatricula();
        return alunoMapper.toDTOSimpleList(alunos);
    }
    
    // ========== OPERAÇÕES DE ATUALIZAÇÃO ==========
    
    /**
     * Atualiza dados de um aluno existente.
     * 
     * **Validações aplicadas:**
     * 
     * - Aluno deve existir e estar ativo
     * - Email único (se alterado)
     * - CPF único (se alterado)
     * 
     * **Comportamento:**
     * 
     * - Atualização parcial (apenas campos não-null)
     * - Atualiza dataAtualizacao automaticamente
     * - Preserva dados não informados
     * 
     * @param id ID do aluno a ser atualizado
     * @param alunoDTO Dados para atualização
     * @return DTO do aluno atualizado
     * @throws ResourceNotFoundException se aluno não for encontrado ou estiver inativo
     * @throws BusinessException se email ou CPF já existir para outro aluno ativo
     */
    public AlunoDTO atualizar(Long id, AlunoDTO alunoDTO) {
        log.info("Iniciando atualização do aluno ID: {}", id);
        
        // Buscar aluno ativo
        Aluno aluno = alunoRepository.findByIdAndAtivoTrue(id)
                .orElseThrow(() -> new ResourceNotFoundException("Aluno ativo não encontrado com ID: " + id));
        
        // Validações de negócio
        validarAlunoUnicoParaAtualizacao(alunoDTO, id);
        
        // Atualizar dados
        alunoMapper.updateEntityFromDTO(alunoDTO, aluno);
        aluno = alunoRepository.save(aluno);
        
        log.info("Aluno atualizado com sucesso. ID: {}", id);
        return alunoMapper.toDTO(aluno);
    }
    
    // ========== OPERAÇÕES DE SOFT DELETE ==========
    
    /**
     * Desativa um aluno (soft delete).
     * 
     * **Comportamento:**
     * 
     * - Marca aluno como inativo
     * - Registra data de desativação
     * - Preserva todos os dados históricos
     * - Aluno não aparece mais em listagens padrão
     * 
     * @param id ID do aluno a ser desativado
     * @throws ResourceNotFoundException se aluno não for encontrado
     * @throws BusinessException se aluno já estiver inativo
     */
    public void desativar(Long id) {
        log.info("Iniciando desativação do aluno ID: {}", id);
        
        // Verificar se aluno existe e está ativo
        Aluno aluno = alunoRepository.findByIdAndAtivoTrue(id)
                .orElseThrow(() -> new ResourceNotFoundException("Aluno ativo não encontrado com ID: " + id));
        
        // Usar método do repository para soft delete
        int registrosAfetados = alunoRepository.desativarAluno(id, LocalDateTime.now());
        
        if (registrosAfetados == 0) {
            throw new BusinessException("Não foi possível desativar o aluno com ID: " + id);
        }
        
        log.info("Aluno desativado com sucesso. ID: {}", id);
    }
    
    /**
     * Reativa um aluno previamente desativado.
     * 
     * **Comportamento:**
     * 
     * - Marca aluno como ativo
     * - Remove data de desativação
     * - Atualiza data de modificação
     * - Aluno volta a aparecer em listagens
     * 
     * @param id ID do aluno a ser reativado
     * @throws ResourceNotFoundException se aluno não for encontrado
     * @throws BusinessException se aluno já estiver ativo
     */
    public void reativar(Long id) {
        log.info("Iniciando reativação do aluno ID: {}", id);
        
        // Verificar se aluno existe
        if (!alunoRepository.existsById(id)) {
            throw new ResourceNotFoundException("Aluno não encontrado com ID: " + id);
        }
        
        // Usar método do repository para reativação
        int registrosAfetados = alunoRepository.reativarAluno(id);
        
        if (registrosAfetados == 0) {
            throw new BusinessException("Aluno não está inativo ou não foi encontrado com ID: " + id);
        }
        
        log.info("Aluno reativado com sucesso. ID: {}", id);
    }
    
    // ========== OPERAÇÕES DE ESTATÍSTICAS ==========
    
    /**
     * Conta total de alunos ativos.
     * 
     * @return Número de alunos ativos
     */
    @Transactional(readOnly = true)
    public Long contarAtivos() {
        log.info("Contando alunos ativos");
        return alunoRepository.countByAtivoTrue();
    }
    
    /**
     * Conta alunos ativos por tipo.
     * 
     * @param tipo Tipo de usuário
     * @return Número de alunos ativos do tipo especificado
     */
    @Transactional(readOnly = true)
    public Long contarPorTipo(TipoUsuario tipo) {
        log.info("Contando alunos ativos por tipo: {}", tipo);
        return alunoRepository.countByTipoAndAtivoTrue(tipo);
    }
    
    /**
     * Conta total de alunos (ativos e inativos).
     * 
     * @return Total de alunos no sistema
     */
    @Transactional(readOnly = true)
    public Long contarTodos() {
        log.info("Contando todos os alunos");
        return alunoRepository.countTotalAlunos();
    }
    
    /**
     * Conta alunos inativos.
     * 
     * @return Número de alunos desativados
     */
    @Transactional(readOnly = true)
    public Long contarInativos() {
        log.info("Contando alunos inativos");
        return alunoRepository.countAlunosInativos();
    }
    
    // ========== MÉTODOS PRIVADOS DE VALIDAÇÃO ==========
    
    /**
     * Valida dados obrigatórios para criação de aluno.
     */
    private void validarDadosObrigatorios(AlunoDTO alunoDTO) {
        if (alunoDTO.getNome() == null || alunoDTO.getNome().trim().isEmpty()) {
            throw new IllegalArgumentException("Nome é obrigatório");
        }
        if (alunoDTO.getEmail() == null || alunoDTO.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("Email é obrigatório");
        }
        if (alunoDTO.getCpf() == null || alunoDTO.getCpf().trim().isEmpty()) {
            throw new IllegalArgumentException("CPF é obrigatório");
        }
        if (alunoDTO.getTelefone() == null || alunoDTO.getTelefone().trim().isEmpty()) {
            throw new IllegalArgumentException("Telefone é obrigatório");
        }
        if (alunoDTO.getTipo() == null) {
            throw new IllegalArgumentException("Tipo de usuário é obrigatório");
        }
    }
    
    /**
     * Valida unicidade de email e CPF para criação de novo aluno.
     */
    private void validarAlunoUnico(AlunoDTO alunoDTO) {
        if (alunoRepository.existsByEmailAndAtivoTrue(alunoDTO.getEmail())) {
            throw new BusinessException("Já existe um aluno ativo com este email: " + alunoDTO.getEmail());
        }
        
        if (alunoRepository.existsByCpfAndAtivoTrue(alunoDTO.getCpf())) {
            throw new BusinessException("Já existe um aluno ativo com este CPF: " + alunoDTO.getCpf());
        }
    }
    
    /**
     * Valida unicidade de email e CPF para atualização de aluno existente.
     */
    private void validarAlunoUnicoParaAtualizacao(AlunoDTO alunoDTO, Long id) {
        if (alunoDTO.getEmail() != null && 
            alunoRepository.existsByEmailAndAtivoTrueAndIdNot(alunoDTO.getEmail(), id)) {
            throw new BusinessException("Já existe outro aluno ativo com este email: " + alunoDTO.getEmail());
        }
        
        if (alunoDTO.getCpf() != null && 
            alunoRepository.existsByCpfAndAtivoTrueAndIdNot(alunoDTO.getCpf(), id)) {
            throw new BusinessException("Já existe outro aluno ativo com este CPF: " + alunoDTO.getCpf());
        }
    }
    
    /**
     * Gera próximo número de matrícula no formato AKD###.
     */
    private String gerarNumeroMatricula() {
        Integer proximoNumero = alunoRepository.findNextMatriculaNumber();
        return String.format("AKD%03d", proximoNumero);
    }
}