package br.com.akdemia.api.mapper;

import br.com.akdemia.api.dto.AlunoDTO;
import br.com.akdemia.api.entity.Aluno;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper responsável pela conversão entre entidade Aluno e AlunoDTO.
 * 
 * <h3>Uso Recomendado:</h3>
 * <ul>
 *   <li><strong>toDTO()</strong> - Para busca individual com relacionamentos completos</li>
 *   <li><strong>toDTOSimple()</strong> - Para listagens e buscas simples (melhor performance)</li>
 *   <li><strong>toEntityForCreation()</strong> - Para criar novos alunos (ignora ID e auditoria)</li>
 *   <li><strong>updateEntityFromDTO()</strong> - Para atualizar alunos existentes (atualização parcial)</li>
 *   <li><strong>toDTOList()</strong> - Para conversão de listas com relacionamentos</li>
 *   <li><strong>toDTOSimpleList()</strong> - Para conversão de listas sem relacionamentos (performance)</li>
 * </ul>
 * 
 * <h3>Exemplos de Uso:</h3>
 * <pre>
 * // Busca individual (com relacionamentos)
 * AlunoDTO dto = alunoMapper.toDTO(aluno);
 * 
 * // Listagem simples (sem relacionamentos - melhor performance)
 * List&lt;AlunoDTO&gt; dtos = alunoMapper.toDTOSimpleList(alunos);
 * 
 * // Criação de novo aluno
 * Aluno novoAluno = alunoMapper.toEntityForCreation(alunoDTO);
 * 
 * // Atualização de aluno existente
 * alunoMapper.updateEntityFromDTO(alunoDTO, alunoExistente);
 * </pre>
 * 
 * @author Sistema Akdemia
 * @version 1.0
 * @since 2025-01-29
 */
@Component
public class AlunoMapper {
    
    /**
     * Converte entidade Aluno para AlunoDTO completo.
     * Inclui todos os campos e relacionamentos como IDs.
     * 
     * <p><strong>Uso:</strong> Busca individual, detalhes do aluno</p>
     * <p><strong>Performance:</strong> Pode ser mais lenta devido aos relacionamentos</p>
     * 
     * @param aluno Entidade a ser convertida
     * @return DTO correspondente com relacionamentos, ou null se aluno for null
     */
    public AlunoDTO toDTO(Aluno aluno) {
        if (aluno == null) {
            return null;
        }
        
        AlunoDTO dto = new AlunoDTO();
        dto.setId(aluno.getId());
        dto.setNome(aluno.getNome());
        dto.setEmail(aluno.getEmail());
        dto.setCpf(aluno.getCpf());
        dto.setTelefone(aluno.getTelefone());
        dto.setTipo(aluno.getTipo());
        dto.setNumeroMatricula(aluno.getNumeroMatricula());
        dto.setAtivo(aluno.getAtivo());
        dto.setDataCadastro(aluno.getDataCadastro());
        dto.setDataAtualizacao(aluno.getDataAtualizacao());
        dto.setDataDesativacao(aluno.getDataDesativacao());
        
        // Mapear relacionamentos como IDs para evitar lazy loading
        if (aluno.getMatriculas() != null) {
            dto.setMatriculasIds(aluno.getMatriculas().stream()
                    .map(matricula -> matricula.getId())
                    .collect(Collectors.toList()));
        }
        
        if (aluno.getTreinos() != null) {
            dto.setTreinosIds(aluno.getTreinos().stream()
                    .map(treino -> treino.getId())
                    .collect(Collectors.toList()));
        }
        
        if (aluno.getAvaliacoes() != null) {
            dto.setAvaliacoesIds(aluno.getAvaliacoes().stream()
                    .map(avaliacao -> avaliacao.getId())
                    .collect(Collectors.toList()));
        }
        
        return dto;
    }
    
    /**
     * Converte entidade Aluno para AlunoDTO simplificado.
     * Não inclui relacionamentos para melhor performance.
     * 
     * <p><strong>Uso:</strong> Listagens, buscas simples, APIs que não precisam de relacionamentos</p>
     * <p><strong>Performance:</strong> Mais rápida, recomendada para listagens</p>
     * 
     * @param aluno Entidade a ser convertida
     * @return DTO simplificado sem relacionamentos, ou null se aluno for null
     */
    public AlunoDTO toDTOSimple(Aluno aluno) {
        if (aluno == null) {
            return null;
        }
        
        AlunoDTO dto = new AlunoDTO();
        dto.setId(aluno.getId());
        dto.setNome(aluno.getNome());
        dto.setEmail(aluno.getEmail());
        dto.setCpf(aluno.getCpf());
        dto.setTelefone(aluno.getTelefone());
        dto.setTipo(aluno.getTipo());
        dto.setNumeroMatricula(aluno.getNumeroMatricula());
        dto.setAtivo(aluno.getAtivo());
        dto.setDataCadastro(aluno.getDataCadastro());
        dto.setDataAtualizacao(aluno.getDataAtualizacao());
        dto.setDataDesativacao(aluno.getDataDesativacao());
        
        // Não inclui relacionamentos para melhor performance
        
        return dto;
    }
    
    /**
     * Converte AlunoDTO para entidade Aluno para criação.
     * Ignora ID e campos de auditoria que são gerenciados automaticamente.
     * 
     * <p><strong>Uso:</strong> Criação de novos alunos</p>
     * <p><strong>Importante:</strong> Define ativo=true automaticamente</p>
     * 
     * @param dto DTO com dados para criação
     * @return Nova entidade para persistência, ou null se dto for null
     */
    public Aluno toEntityForCreation(AlunoDTO dto) {
        if (dto == null) {
            return null;
        }
        
        Aluno aluno = new Aluno();
        aluno.setNome(dto.getNome());
        aluno.setEmail(dto.getEmail());
        aluno.setCpf(dto.getCpf());
        aluno.setTelefone(dto.getTelefone());
        aluno.setTipo(dto.getTipo());
        aluno.setNumeroMatricula(dto.getNumeroMatricula());
        aluno.setAtivo(true); // Sempre ativo na criação
        
        // ID, datas de auditoria e relacionamentos são gerenciados automaticamente
        
        return aluno;
    }
    
    /**
     * Converte AlunoDTO para entidade Aluno (conversão completa).
     * Inclui ID e todos os campos do DTO.
     * 
     * <p><strong>Uso:</strong> Casos específicos onde é necessária conversão completa</p>
     * <p><strong>Cuidado:</strong> Para criação, prefira toEntityForCreation()</p>
     * 
     * @param dto DTO a ser convertido
     * @return Entidade correspondente, ou null se dto for null
     */
    public Aluno toEntity(AlunoDTO dto) {
        if (dto == null) {
            return null;
        }
        
        Aluno aluno = new Aluno();
        aluno.setId(dto.getId());
        aluno.setNome(dto.getNome());
        aluno.setEmail(dto.getEmail());
        aluno.setCpf(dto.getCpf());
        aluno.setTelefone(dto.getTelefone());
        aluno.setTipo(dto.getTipo());
        aluno.setNumeroMatricula(dto.getNumeroMatricula());
        aluno.setAtivo(dto.getAtivo() != null ? dto.getAtivo() : true);
        
        // Não mapear relacionamentos na criação para evitar problemas
        // Os relacionamentos devem ser gerenciados separadamente
        
        return aluno;
    }
    
    /**
     * Atualiza entidade existente com dados do DTO (atualização parcial).
     * Apenas campos não-nulos do DTO são aplicados à entidade.
     * 
     * <p><strong>Uso:</strong> Operações de update/PUT</p>
     * <p><strong>Comportamento:</strong> Atualização parcial - apenas campos preenchidos</p>
     * <p><strong>Importante:</strong> Campos de auditoria são gerenciados automaticamente</p>
     * 
     * @param dto DTO com novos dados (campos null são ignorados)
     * @param aluno Entidade existente a ser atualizada
     */
    public void updateEntityFromDTO(AlunoDTO dto, Aluno aluno) {
        if (dto == null || aluno == null) {
            return;
        }
        
        if (dto.getNome() != null) {
            aluno.setNome(dto.getNome());
        }
        if (dto.getEmail() != null) {
            aluno.setEmail(dto.getEmail());
        }
        if (dto.getCpf() != null) {
            aluno.setCpf(dto.getCpf());
        }
        if (dto.getTelefone() != null) {
            aluno.setTelefone(dto.getTelefone());
        }
        if (dto.getTipo() != null) {
            aluno.setTipo(dto.getTipo());
        }
        if (dto.getNumeroMatricula() != null) {
            aluno.setNumeroMatricula(dto.getNumeroMatricula());
        }
        if (dto.getAtivo() != null) {
            aluno.setAtivo(dto.getAtivo());
        }
        
        // Campos de auditoria não devem ser atualizados manualmente
        // dataAtualizacao é gerenciada automaticamente pelo @UpdateTimestamp
        // dataCadastro nunca deve ser alterada
        // dataDesativacao é gerenciada pelo método de desativação
    }
    
    /**
     * Converte lista de entidades para lista de DTOs completos.
     * Inclui relacionamentos - pode impactar performance em listas grandes.
     * 
     * <p><strong>Uso:</strong> Quando relacionamentos são necessários</p>
     * <p><strong>Performance:</strong> Para listas grandes, prefira toDTOSimpleList()</p>
     * 
     * @param alunos Lista de entidades
     * @return Lista de DTOs com relacionamentos, ou null se alunos for null
     */
    public List<AlunoDTO> toDTOList(List<Aluno> alunos) {
        if (alunos == null) {
            return null;
        }
        
        return alunos.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Converte lista de entidades para lista de DTOs simplificados.
     * Não inclui relacionamentos para melhor performance.
     * 
     * <p><strong>Uso:</strong> Listagens, paginação, APIs de consulta</p>
     * <p><strong>Performance:</strong> Recomendada para listas grandes</p>
     * 
     * @param alunos Lista de entidades
     * @return Lista de DTOs simplificados, ou null se alunos for null
     */
    public List<AlunoDTO> toDTOSimpleList(List<Aluno> alunos) {
        if (alunos == null) {
            return null;
        }
        
        return alunos.stream()
                .map(this::toDTOSimple)
                .collect(Collectors.toList());
    }
}