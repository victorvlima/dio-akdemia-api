package br.com.akdemia.api.repository;

import br.com.akdemia.api.entity.Usuario;
import br.com.akdemia.api.enums.TipoUsuario;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    
    Optional<Usuario> findByEmail(String email);
    
    Optional<Usuario> findByCpf(String cpf);
    
    List<Usuario> findByTipoAndAtivoTrue(TipoUsuario tipo);
    
    List<Usuario> findByAtivoTrue();
    
    @Query("SELECT u FROM Usuario u WHERE u.nome LIKE %:nome% AND u.ativo = true")
    List<Usuario> findByNomeContainingIgnoreCaseAndAtivoTrue(@Param("nome") String nome);
    
    @Query("SELECT COUNT(u) FROM Usuario u WHERE u.tipo = :tipo AND u.ativo = true")
    Long countByTipoAndAtivoTrue(@Param("tipo") TipoUsuario tipo);
    
    boolean existsByEmail(String email);
    
    boolean existsByCpf(String cpf);
}