package br.com.akdemia.api.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDateTime;

import br.com.akdemia.api.enums.TipoUsuario;

@Data
public class UsuarioDTO {
    private Long id;
    
    @NotBlank(message = "Nome é obrigatório")
    @Size(min = 2, max = 100, message = "Nome deve ter entre 2 e 100 caracteres")
    private String nome;
    
    @Email(message = "Email deve ser válido")
    @NotBlank(message = "Email é obrigatório")
    private String email;
    
    @NotBlank(message = "CPF é obrigatório")
    @Pattern(regexp = "'\'d{11}", message = "CPF deve conter 11 dígitos")
    private String cpf;
    
    @NotBlank(message = "Telefone é obrigatório")
    private String telefone;
    
    @NotNull(message = "Tipo de usuário é obrigatório")
    private TipoUsuario tipo;
    
    private Boolean ativo;
    private LocalDateTime dataCriacao;
    private LocalDateTime dataAtualizacao;
}