package br.com.akdemia.api.service;

import br.com.akdemia.api.dto.UsuarioDTO;
import br.com.akdemia.api.exception.BusinessException;
import br.com.akdemia.api.exception.ResourceNotFoundException;
import br.com.akdemia.api.mapper.UsuarioMapper;
import br.com.akdemia.api.model.TipoUsuario;
import br.com.akdemia.api.model.Usuario;
import br.com.akdemia.api.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class UsuarioService {
    
    private final UsuarioRepository usuarioRepository;
    private final UsuarioMapper usuarioMapper;
    
    public UsuarioDTO criar(UsuarioDTO usuarioDTO) {
        log.info("Criando novo usuário: {}", usuarioDTO.getEmail());
        
        validarUsuarioUnico(usuarioDTO);
        
        Usuario usuario = usuarioMapper.toEntity(usuarioDTO);
        usuario = usuarioRepository.save(usuario);
        
        log.info("Usuário criado com sucesso. ID: {}", usuario.getId());
        return usuarioMapper.toDTO(usuario);
    }
    
    @Transactional(readOnly = true)
    public UsuarioDTO buscarPorId(Long id) {
        log.info("Buscando usuário por ID: {}", id);
        
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado com ID: " + id));
        
        return usuarioMapper.toDTO(usuario);
    }
    
    @Transactional(readOnly = true)
    public List<UsuarioDTO> listarTodos() {
        log.info("Listando todos os usuários ativos");
        
        List<Usuario> usuarios = usuarioRepository.findByAtivoTrue();
        return usuarioMapper.toDTOList(usuarios);
    }
    
    @Transactional(readOnly = true)
    public List<UsuarioDTO> listarPorTipo(TipoUsuario tipo) {
        log.info("Listando usuários por tipo: {}", tipo);
        
        List<Usuario> usuarios = usuarioRepository.findByTipoAndAtivoTrue(tipo);
        return usuarioMapper.toDTOList(usuarios);
    }
    
    public UsuarioDTO atualizar(Long id, UsuarioDTO usuarioDTO) {
        log.info("Atualizando usuário ID: {}", id);
        
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado com ID: " + id));
        
        validarUsuarioUnicoParaAtualizacao(usuarioDTO, id);
        
        usuarioMapper.updateEntityFromDTO(usuarioDTO, usuario);
        usuario = usuarioRepository.save(usuario);
        
        log.info("Usuário atualizado com sucesso. ID: {}", id);
        return usuarioMapper.toDTO(usuario);
    }
    
    public void desativar(Long id) {
        log.info("Desativando usuário ID: {}", id);
        
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado com ID: " + id));
        
        usuario.setAtivo(false);
        usuarioRepository.save(usuario);
        
        log.info("Usuário desativado com sucesso. ID: {}", id);
    }
    
    @Transactional(readOnly = true)
    public List<UsuarioDTO> buscarPorNome(String nome) {
        log.info("Buscando usuários por nome: {}", nome);
        
        List<Usuario> usuarios = usuarioRepository.findByNomeContainingIgnoreCaseAndAtivoTrue(nome);
        return usuarioMapper.toDTOList(usuarios);
    }
    
    private void validarUsuarioUnico(UsuarioDTO usuarioDTO) {
        if (usuarioRepository.existsByEmail(usuarioDTO.getEmail())) {
            throw new BusinessException("Já existe um usuário com este email: " + usuarioDTO.getEmail());
        }
        
        if (usuarioRepository.existsByCpf(usuarioDTO.getCpf())) {
            throw new BusinessException("Já existe um usuário com este CPF: " + usuarioDTO.getCpf());
        }
    }
    
    private void validarUsuarioUnicoParaAtualizacao(UsuarioDTO usuarioDTO, Long id) {
        usuarioRepository.findByEmail(usuarioDTO.getEmail())
                .ifPresent(usuario -> {
                    if (!usuario.getId().equals(id)) {
                        throw new BusinessException("Já existe um usuário com este email: " + usuarioDTO.getEmail());
                    }
                });
    }
}