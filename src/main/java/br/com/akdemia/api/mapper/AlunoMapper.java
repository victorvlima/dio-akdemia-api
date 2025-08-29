package br.com.akdemia.api.mapper;

import br.com.akdemia.api.dto.AlunoDTO;
import br.com.akdemia.api.entity.Aluno;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class AlunoMapper {
    
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
        dto.setAtivo(aluno.getAtivo());
        dto.setDataCriacao(aluno.getDataCriacao());
        dto.setDataAtualizacao(aluno.getDataAtualizacao());
        
        return dto;
    }
    
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
        aluno.setAtivo(dto.getAtivo() != null ? dto.getAtivo() : true);
        
        return aluno;
    }
    
    public List<AlunoDTO> toDTOList(List<Aluno> alunos) {
        return alunos.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
    
    public void updateEntityFromDTO(AlunoDTO dto, Aluno aluno) {
        if (dto.getNome() != null) {
            aluno.setNome(dto.getNome());
        }
        if (dto.getEmail() != null) {
            aluno.setEmail(dto.getEmail());
        }
        if (dto.getTelefone() != null) {
            aluno.setTelefone(dto.getTelefone());
        }
        if (dto.getTipo() != null) {
            aluno.setTipo(dto.getTipo());
        }
        if (dto.getAtivo() != null) {
            aluno.setAtivo(dto.getAtivo());
        }
    }
}