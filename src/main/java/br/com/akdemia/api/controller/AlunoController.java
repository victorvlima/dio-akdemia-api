package br.com.akdemia.api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.akdemia.api.dto.AlunoDTO;
import br.com.akdemia.api.enums.TipoUsuario;
import br.com.akdemia.api.service.AlunoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

/**
 * Controller REST para operações relacionadas aos alunos da academia.
 * 
 * Fornece endpoints para gerenciamento completo de alunos, incluindo:
 * 
 * - Criação de novos alunos
 * - Consulta e listagem com filtros
 * - Atualização de dados
 * - Desativação (soft delete)
 * - Buscas avançadas
 * 
 * ## Endpoints Disponíveis
 * 
 * - **POST /alunos/novo** - Criar novo aluno
 * - **GET /alunos/todos** - Listar todos os alunos ativos
 * - **GET /alunos/{id}** - Buscar aluno por ID
 * - **GET /alunos/tipo/{tipo}** - Listar alunos por tipo
 * - **PUT /alunos/{id}** - Atualizar dados do aluno
 * - **DELETE /alunos/{id}** - Desativar aluno (soft delete)
 * - **GET /alunos/buscar** - Buscar alunos por nome
 * 
 * ## Códigos de Status HTTP
 * 
 * - **200 OK** - Operação realizada com sucesso
 * - **201 CREATED** - Aluno criado com sucesso
 * - **204 NO CONTENT** - Aluno desativado com sucesso
 * - **400 BAD REQUEST** - Dados inválidos ou regras de negócio violadas
 * - **404 NOT FOUND** - Aluno não encontrado
 * - **500 INTERNAL SERVER ERROR** - Erro interno do servidor
 * 
 * ## Validações
 * 
 * - **Criação:** Validação completa via Bean Validation (@Valid)
 * - **Atualização:** Validação parcial (campos opcionais)
 * - **Regras de negócio:** Email e CPF únicos entre alunos ativos
 * 
 * @author Sistema Akdemia
 * @version 1.0
 * @since 2025-01-29
 */
@RestController
@RequestMapping("/alunos")
@Tag(name = "Aluno", description = "Operações relacionadas aos alunos da academia")
public class AlunoController {
    
    @Autowired
    private AlunoService alunoService;
    
    /**
     * Cria um novo aluno no sistema.
     * 
     * **Validações aplicadas:**
     * - Todos os campos obrigatórios devem estar preenchidos
     * - Email deve ter formato válido e ser único entre alunos ativos
     * - CPF deve ter 11 dígitos e ser único entre alunos ativos
     * - Nome deve ter entre 2 e 100 caracteres
     * 
     * **Comportamento:**
     * - Gera número de matrícula automaticamente
     * - Define status como ativo
     * - Registra data de cadastro
     * 
     * @param alunoDTO Dados do aluno a ser criado
     * @return ResponseEntity com o aluno criado e status 201 CREATED
     */
    @PostMapping("/novo")
    @Operation(summary = "Criar novo aluno", description = "Cria um novo aluno no sistema")
    public ResponseEntity<AlunoDTO> criar(@Valid @RequestBody AlunoDTO alunoDTO) {
        AlunoDTO usuarioCriado = alunoService.criar(alunoDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioCriado);
    }
    
    /**
     * Lista todos os alunos ativos do sistema.
     * 
     * **Comportamento:**
     * - Retorna apenas alunos com status ativo = true
     * - Utiliza DTO simplificado para melhor performance
     * - Não inclui relacionamentos para otimização
     * 
     * **Performance:**
     * Para grandes volumes de dados, considere usar paginação
     * 
     * @return ResponseEntity com lista de alunos ativos
     */
    @GetMapping("/todos")
    @Operation(summary = "Listar todos os alunos", description = "Retorna lista de todos os alunos ativos")
    public ResponseEntity<List<AlunoDTO>> listarTodos() {
        List<AlunoDTO> alunos = alunoService.listarTodos();
        return ResponseEntity.ok(alunos);
    }
    
    /**
     * Busca um aluno específico pelo ID.
     * 
     * **Comportamento:**
     * - Retorna aluno independente do status (ativo/inativo)
     * - Inclui todos os dados e relacionamentos
     * - Lança exceção se aluno não for encontrado
     * 
     * @param id ID do aluno a ser buscado
     * @return ResponseEntity com o aluno encontrado
     * @throws ResourceNotFoundException se aluno não existir
     */
    @GetMapping("/{id}")
    @Operation(summary = "Buscar aluno por ID", description = "Retorna um aluno específico pelo ID")
    public ResponseEntity<AlunoDTO> buscarPorId(
            @Parameter(description = "ID do aluno") @PathVariable Long id) {
        AlunoDTO aluno = alunoService.buscarPorId(id);
        return ResponseEntity.ok(aluno);
    }
    
    /**
     * Lista alunos ativos filtrados por tipo de usuário.
     * 
     * **Tipos disponíveis:**
     * - ALUNO - Alunos regulares
     * - INSTRUTOR - Instrutores
     * - ADMIN - Administradores
     * 
     * **Comportamento:**
     * - Retorna apenas alunos ativos do tipo especificado
     * - Lista vazia se não houver alunos do tipo
     * 
     * @param tipo Tipo de usuário para filtro
     * @return ResponseEntity com lista de alunos do tipo especificado
     */
    @GetMapping("/tipo/{tipo}")
    @Operation(summary = "Listar alunos por tipo", description = "Retorna lista de alunos por tipo específico")
    public ResponseEntity<List<AlunoDTO>> listarPorTipo(
            @Parameter(description = "Tipo do aluno") @PathVariable TipoUsuario tipo) {
        List<AlunoDTO> alunos = alunoService.listarPorTipo(tipo);
        return ResponseEntity.ok(alunos);
    }
    
    /**
     * Atualiza os dados de um aluno existente.
     * 
     * **Validações aplicadas:**
     * - Aluno deve existir e estar ativo
     * - Email único (se informado e diferente do atual)
     * - CPF único (se informado e diferente do atual)
     * 
     * **Comportamento:**
     * - Atualização parcial (apenas campos não-null são atualizados)
     * - Preserva dados não informados
     * - Atualiza dataAtualizacao automaticamente
     * - Número de matrícula não pode ser alterado
     * 
     * **Nota:** Validação @Valid comentada para permitir atualização parcial
     * 
     * @param id ID do aluno a ser atualizado
     * @param alunoDTO Dados para atualização (campos opcionais)
     * @return ResponseEntity com o aluno atualizado
     * @throws ResourceNotFoundException se aluno não existir ou estiver inativo
     * @throws BusinessException se email/CPF já existir para outro aluno
     */
    @PutMapping("/{id}")
    @Operation(summary = "Atualizar aluno", description = "Atualiza os dados de um aluno existente")
    public ResponseEntity<AlunoDTO> atualizar(
            @Parameter(description = "ID do aluno") @PathVariable Long id,
            /*@Valid*/ @RequestBody AlunoDTO alunoDTO) {
        AlunoDTO usuarioAtualizado = alunoService.atualizar(id, alunoDTO);
        return ResponseEntity.ok(usuarioAtualizado);
    }
    
    /**
     * Desativa um aluno do sistema (soft delete).
     * 
     * **Comportamento:**
     * - Marca aluno como inativo (ativo = false)
     * - Registra data de desativação
     * - Preserva todos os dados históricos
     * - Aluno não aparece mais em listagens padrão
     * - Não remove fisicamente do banco de dados
     * 
     * **Reversibilidade:**
     * A operação pode ser revertida através de reativação
     * 
     * @param id ID do aluno a ser desativado
     * @return ResponseEntity com status 204 NO CONTENT
     * @throws ResourceNotFoundException se aluno não existir
     * @throws BusinessException se aluno já estiver inativo
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Desativar aluno", description = "Desativa um aluno (soft delete)")
    public ResponseEntity<Void> desativar(
            @Parameter(description = "ID do aluno") @PathVariable Long id) {
        alunoService.desativar(id);
        return ResponseEntity.noContent().build();
    }
    
    /**
     * Busca alunos ativos por nome (busca parcial, case insensitive).
     * 
     * **Comportamento:**
     * - Busca por substring no nome (LIKE %nome%)
     * - Ignora maiúsculas e minúsculas
     * - Retorna apenas alunos ativos
     * - Lista vazia se nenhum aluno for encontrado
     * 
     * **Exemplos:**
     * - "joão" encontra "João Silva", "Maria João", etc.
     * - "silva" encontra "João Silva", "Ana Silva", etc.
     * 
     * @param nome Nome ou parte do nome para busca
     * @return ResponseEntity com lista de alunos que contêm o nome especificado
     */
    @GetMapping("/buscar")
    @Operation(summary = "Buscar alunos por nome", description = "Busca alunos que contenham o nome especificado")
    public ResponseEntity<List<AlunoDTO>> buscarPorNome(
            @Parameter(description = "Nome para busca") @RequestParam String nome) {
        List<AlunoDTO> alunos = alunoService.buscarPorNome(nome);
        return ResponseEntity.ok(alunos);
    }
}