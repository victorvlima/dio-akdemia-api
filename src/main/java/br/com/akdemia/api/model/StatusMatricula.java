package br.com.akdemia.api.model;

public enum StatusMatricula {
    ATIVA("Ativa"),
    SUSPENSA("Suspensa"),
    CANCELADA("Cancelada"),
    VENCIDA("Vencida");
    
    private final String descricao;
    
    StatusMatricula(String descricao) {
        this.descricao = descricao;
    }
    
    public String getDescricao() {
        return descricao;
    }
}