package br.com.akdemia.api.repository;

import br.com.akdemia.api.entity.Aluno;
import br.com.akdemia.api.enums.TipoUsuario;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface AlunoRepository extends JpaRepository<Aluno, Long> {
    
    // ✅ Buscas básicas
    Optional<Aluno> findByEmail(String email);
    Optional<Aluno> findByCpf(String cpf);
    Optional<Aluno> findByNumeroMatricula(String numeroMatricula);
    
    // ✅ Verificações de existência
    boolean existsByEmail(String email);
    boolean existsByCpf(String cpf);
    boolean existsByNumeroMatricula(String numeroMatricula);
    
    // ✅ Filtros por status e tipo
    List<Aluno> findByTipoAndAtivoTrue(TipoUsuario tipo);
    List<Aluno> findByAtivoTrue();
    Page<Aluno> findByAtivoTrue(Pageable pageable);
    
    // ✅ Busca por nome (Spring Data JPA faz automaticamente)
    List<Aluno> findByNomeContainingIgnoreCaseAndAtivoTrue(String nome);
    
    // ✅ Contadores
    @Query("SELECT COUNT(a) FROM Aluno a WHERE a.tipo = :tipo AND a.ativo = true")
    Long countByTipoAndAtivoTrue(@Param("tipo") TipoUsuario tipo);
    
    @Query("SELECT COUNT(a) FROM Aluno a WHERE a.ativo = true")
    Long countByAtivoTrue();
    
    @Query("SELECT a FROM Aluno a WHERE a.ativo = true AND SIZE(a.matriculas) = 0")
    List<Aluno> findAlunosSemMatricula();
    
    // ✅ Busca avançada com múltiplos filtros
    @Query("SELECT a FROM Aluno a WHERE " +
           "(:nome IS NULL OR LOWER(a.nome) LIKE LOWER(CONCAT('%', :nome, '%'))) AND " +
           "(:tipo IS NULL OR a.tipo = :tipo) AND " +
           "a.ativo = true")
    Page<Aluno> findByFiltros(@Param("nome") String nome, 
                             @Param("tipo") TipoUsuario tipo, 
                             Pageable pageable);
    
    // ✅ Estatísticas
    @Query("SELECT a.tipo, COUNT(a) FROM Aluno a WHERE a.ativo = true GROUP BY a.tipo")
    List<Object[]> countAlunosAtivosPorTipo();
}