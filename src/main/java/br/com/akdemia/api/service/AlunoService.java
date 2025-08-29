package br.com.akdemia.api.service;

import java.util.List;

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

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class AlunoService {
    
    private final AlunoRepository alunoRepository;
    private final AlunoMapper alunoMapper;
    
    public AlunoDTO criar(AlunoDTO alunoDTO) {
        log.info("Criando novo aluno: {}", alunoDTO.getEmail());
        
        validarAlunoUnico(alunoDTO);
        
        Aluno aluno = alunoMapper.toEntity(alunoDTO);
        aluno = alunoRepository.save(aluno);
        
        log.info("Aluno criado com sucesso. ID: {}", aluno.getId());
        return alunoMapper.toDTO(aluno);
    }
    
    @Transactional(readOnly = true)
    public AlunoDTO buscarPorId(Long id) {
        log.info("Buscando aluno por ID: {}", id);
        
        Aluno aluno = alunoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Aluno não encontrado com ID: " + id));
        
        return alunoMapper.toDTO(aluno);
    }
    
    @Transactional(readOnly = true)
    public List<AlunoDTO> listarTodos() {
        log.info("Listando todos os usuários ativos");
        
        List<Aluno> alunos = alunoRepository.findByAtivoTrue();
        return alunoMapper.toDTOList(alunos);
    }
    
    @Transactional(readOnly = true)
    public List<AlunoDTO> listarPorTipo(TipoUsuario tipo) {
        log.info("Listando alunos por tipo: {}", tipo);
        
        List<Aluno> alunos = alunoRepository.findByTipoAndAtivoTrue(tipo);
        return alunoMapper.toDTOList(alunos);
    }
    
    public AlunoDTO atualizar(Long id, AlunoDTO alunoDTO) {
        log.info("Atualizando aluno ID: {}", id);
        
        Aluno aluno = alunoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Aluno não encontrado com ID: " + id));
        
        validarAlunoUnicoParaAtualizacao(alunoDTO, id);
        
        alunoMapper.updateEntityFromDTO(alunoDTO, aluno);
        aluno = alunoRepository.save(aluno);
        
        log.info("Aluno atualizado com sucesso. ID: {}", id);
        return alunoMapper.toDTO(aluno);
    }
    
    public void desativar(Long id) {
        log.info("Desativando usuário ID: {}", id);
        
        Aluno aluno =alunoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Aluno não encontrado com ID: " + id));
        
        aluno.setAtivo(false);
        alunoRepository.save(aluno);
        
        log.info("Aluno desativado com sucesso. ID: {}", id);
    }
    
    @Transactional(readOnly = true)
    public List<AlunoDTO> buscarPorNome(String nome) {
        log.info("Buscando alunos por nome: {}", nome);
        
        List<Aluno> alunos = alunoRepository.findByNomeContainingIgnoreCaseAndAtivoTrue(nome);
        return alunoMapper.toDTOList(alunos);
    }
    
    private void validarAlunoUnico(AlunoDTO alunoDTO) {
        if (alunoRepository.existsByEmail(alunoDTO.getEmail())) {
            throw new BusinessException("Já existe um aluno com este email: " + alunoDTO.getEmail());
        }
        
        if (alunoRepository.existsByCpf(alunoDTO.getCpf())) {
            throw new BusinessException("Já existe um aluno com este CPF: " + alunoDTO.getCpf());
        }
    }
    
    private void validarAlunoUnicoParaAtualizacao(AlunoDTO alunoDTO, Long id) {
        alunoRepository.findByEmail(alunoDTO.getEmail())
                .ifPresent(aluno -> {
                    if (!aluno.getId().equals(id)) {
                        throw new BusinessException("Já existe um aluno com este email: " + alunoDTO.getEmail());
                    }
                });
    }
}