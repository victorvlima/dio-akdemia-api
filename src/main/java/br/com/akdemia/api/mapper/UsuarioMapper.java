package br.com.akdemia.api.mapper;

import br.com.akdemia.api.dto.UsuarioDTO;
import br.com.akdemia.api.entity.Usuario;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class UsuarioMapper {
    
    public UsuarioDTO toDTO(Usuario usuario) {
        if (usuario == null) {
            return null;
        }
        
        UsuarioDTO dto = new UsuarioDTO();
        dto.setId(usuario.getId());
        dto.setNome(usuario.getNome());
        dto.setEmail(usuario.getEmail());
        dto.setCpf(usuario.getCpf());
        dto.setTelefone(usuario.getTelefone());
        dto.setTipo(usuario.getTipo());
        dto.setAtivo(usuario.getAtivo());
        dto.setDataCriacao(usuario.getDataCriacao());
        dto.setDataAtualizacao(usuario.getDataAtualizacao());
        
        return dto;
    }
    
    public Usuario toEntity(UsuarioDTO dto) {
        if (dto == null) {
            return null;
        }
        
        Usuario usuario = new Usuario();
        usuario.setId(dto.getId());
        usuario.setNome(dto.getNome());
        usuario.setEmail(dto.getEmail());
        usuario.setCpf(dto.getCpf());
        usuario.setTelefone(dto.getTelefone());
        usuario.setTipo(dto.getTipo());
        usuario.setAtivo(dto.getAtivo() != null ? dto.getAtivo() : true);
        
        return usuario;
    }
    
    public List<UsuarioDTO> toDTOList(List<Usuario> usuarios) {
        return usuarios.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
    
    public void updateEntityFromDTO(UsuarioDTO dto, Usuario usuario) {
        if (dto.getNome() != null) {
            usuario.setNome(dto.getNome());
        }
        if (dto.getEmail() != null) {
            usuario.setEmail(dto.getEmail());
        }
        if (dto.getTelefone() != null) {
            usuario.setTelefone(dto.getTelefone());
        }
        if (dto.getTipo() != null) {
            usuario.setTipo(dto.getTipo());
        }
        if (dto.getAtivo() != null) {
            usuario.setAtivo(dto.getAtivo());
        }
    }
}