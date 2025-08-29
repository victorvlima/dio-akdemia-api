package br.com.akdemia.api.enums;

public enum TipoUsuario {
    ALUNO("Aluno"),
    INSTRUTOR("Instrutor"),
    ADMINISTRADOR("Administrador");
    
    private final String descricao;
    
    TipoUsuario(String descricao) {
        this.descricao = descricao;
    }
    
    public String getDescricao() {
        return descricao;
    }
}