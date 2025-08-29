package br.com.akdemia.api.controller;

import br.com.akdemia.api.dto.UsuarioDTO;
import br.com.akdemia.api.enums.TipoUsuario;
import br.com.akdemia.api.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
@Tag(name = "Usuários", description = "Operações relacionadas aos usuários da academia")
public class UsuarioController {
    
    private final UsuarioService usuarioService;
    
    @PostMapping
    @Operation(summary = "Criar novo usuário", description = "Cria um novo usuário no sistema")
    public ResponseEntity<UsuarioDTO> criar(@Valid @RequestBody UsuarioDTO usuarioDTO) {
        UsuarioDTO usuarioCriado = usuarioService.criar(usuarioDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioCriado);
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Buscar usuário por ID", description = "Retorna um usuário específico pelo ID")
    public ResponseEntity<UsuarioDTO> buscarPorId(
            @Parameter(description = "ID do usuário") @PathVariable Long id) {
        UsuarioDTO usuario = usuarioService.buscarPorId(id);
        return ResponseEntity.ok(usuario);
    }
    
    @GetMapping
    @Operation(summary = "Listar todos os usuários", description = "Retorna lista de todos os usuários ativos")
    public ResponseEntity<List<UsuarioDTO>> listarTodos() {
        List<UsuarioDTO> usuarios = usuarioService.listarTodos();
        return ResponseEntity.ok(usuarios);
    }
    
    @GetMapping("/tipo/{tipo}")
    @Operation(summary = "Listar usuários por tipo", description = "Retorna lista de usuários por tipo específico")
    public ResponseEntity<List<UsuarioDTO>> listarPorTipo(
            @Parameter(description = "Tipo do usuário") @PathVariable TipoUsuario tipo) {
        List<UsuarioDTO> usuarios = usuarioService.listarPorTipo(tipo);
        return ResponseEntity.ok(usuarios);
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "Atualizar usuário", description = "Atualiza os dados de um usuário existente")
    public ResponseEntity<UsuarioDTO> atualizar(
            @Parameter(description = "ID do usuário") @PathVariable Long id,
            @Valid @RequestBody UsuarioDTO usuarioDTO) {
        UsuarioDTO usuarioAtualizado = usuarioService.atualizar(id, usuarioDTO);
        return ResponseEntity.ok(usuarioAtualizado);
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "Desativar usuário", description = "Desativa um usuário (soft delete)")
    public ResponseEntity<Void> desativar(
            @Parameter(description = "ID do usuário") @PathVariable Long id) {
        usuarioService.desativar(id);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/buscar")
    @Operation(summary = "Buscar usuários por nome", description = "Busca usuários que contenham o nome especificado")
    public ResponseEntity<List<UsuarioDTO>> buscarPorNome(
            @Parameter(description = "Nome para busca") @RequestParam String nome) {
        List<UsuarioDTO> usuarios = usuarioService.buscarPorNome(nome);
        return ResponseEntity.ok(usuarios);
    }
}