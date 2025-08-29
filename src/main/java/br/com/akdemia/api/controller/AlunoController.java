package br.com.akdemia.api.controller;

import br.com.akdemia.api.dto.AlunoDTO;
import br.com.akdemia.api.enums.TipoUsuario;
import br.com.akdemia.api.service.AlunoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@RestController
@RequestMapping("/api/alunos")
@Tag(name = "Aluno", description = "Operações relacionadas aos alunos da academia")
public class AlunoController {
    
    @Autowired
    private AlunoService alunoService;
    
    @PostMapping
    @Operation(summary = "Criar novo aluno", description = "Cria um novo aluno no sistema")
    public ResponseEntity<AlunoDTO> criar(@Valid @RequestBody AlunoDTO alunoDTO) {
        AlunoDTO usuarioCriado = alunoService.criar(alunoDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioCriado);
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Buscar aluno por ID", description = "Retorna um aluno específico pelo ID")
    public ResponseEntity<AlunoDTO> buscarPorId(
            @Parameter(description = "ID do aluno") @PathVariable Long id) {
        AlunoDTO aluno = alunoService.buscarPorId(id);
        return ResponseEntity.ok(aluno);
    }
    
    @GetMapping
    @Operation(summary = "Listar todos os alunos", description = "Retorna lista de todos os alunos ativos")
    public ResponseEntity<List<AlunoDTO>> listarTodos() {
        List<AlunoDTO> alunos = alunoService.listarTodos();
        return ResponseEntity.ok(alunos);
    }
    
    @GetMapping("/tipo/{tipo}")
    @Operation(summary = "Listar alunos por tipo", description = "Retorna lista de alunos por tipo específico")
    public ResponseEntity<List<AlunoDTO>> listarPorTipo(
            @Parameter(description = "Tipo do aluno") @PathVariable TipoUsuario tipo) {
        List<AlunoDTO> alunos = alunoService.listarPorTipo(tipo);
        return ResponseEntity.ok(alunos);
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "Atualizar aluno", description = "Atualiza os dados de um aluno existente")
    public ResponseEntity<AlunoDTO> atualizar(
            @Parameter(description = "ID do aluno") @PathVariable Long id,
            @Valid @RequestBody AlunoDTO alunoDTO) {
        AlunoDTO usuarioAtualizado = alunoService.atualizar(id, alunoDTO);
        return ResponseEntity.ok(usuarioAtualizado);
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "Desativar aluno", description = "Desativa um aluno (soft delete)")
    public ResponseEntity<Void> desativar(
            @Parameter(description = "ID do aluno") @PathVariable Long id) {
        alunoService.desativar(id);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/buscar")
    @Operation(summary = "Buscar alunos por nome", description = "Busca alunos que contenham o nome especificado")
    public ResponseEntity<List<AlunoDTO>> buscarPorNome(
            @Parameter(description = "Nome para busca") @RequestParam String nome) {
        List<AlunoDTO> alunos = alunoService.buscarPorNome(nome);
        return ResponseEntity.ok(alunos);
    }
}