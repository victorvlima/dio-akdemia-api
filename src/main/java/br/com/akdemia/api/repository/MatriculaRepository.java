package br.com.akdemia.api.repository;

import br.com.akdemia.api.model.Matricula;
import br.com.akdemia.api.model.StatusMatricula;
import br.com.akdemia.api.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface MatriculaRepository extends JpaRepository<Matricula, Long> {
    
    List<Matricula> findByAlunoAndStatus(Usuario aluno, StatusMatricula status);
    
    Optional<Matricula> findByAlunoAndStatusOrderByDataCriacaoDesc(Usuario aluno, StatusMatricula status);
    
    @Query("SELECT m FROM Matricula m WHERE m.dataFim < :data AND m.status = 'ATIVA'")
    List<Matricula> findMatriculasVencidas(@Param("data") LocalDate data);
    
    @Query("SELECT m FROM Matricula m WHERE m.dataFim BETWEEN :dataInicio AND :dataFim")
    List<Matricula> findMatriculasVencendoEntre(@Param("dataInicio") LocalDate dataInicio, 
                                                @Param("dataFim") LocalDate dataFim);
    
    @Query("SELECT COUNT(m) FROM Matricula m WHERE m.status = 'ATIVA'")
    Long countMatriculasAtivas();
    
    @Query("SELECT m FROM Matricula m WHERE m.aluno.id = :alunoId ORDER BY m.dataCriacao DESC")
    List<Matricula> findByAlunoIdOrderByDataCriacaoDesc(@Param("alunoId") Long alunoId);
}