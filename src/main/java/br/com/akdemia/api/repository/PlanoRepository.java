package br.com.akdemia.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.com.akdemia.api.entity.Plano;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface PlanoRepository extends JpaRepository<Plano, Long> {
    
    List<Plano> findByAtivoTrue();
    
    List<Plano> findByValorBetweenAndAtivoTrue(BigDecimal valorMin, BigDecimal valorMax);
    
    @Query("SELECT p FROM Plano p WHERE p.duracaoDias <= :dias AND p.ativo = true ORDER BY p.valor ASC")
    List<Plano> findByDuracaoMenorOuIgualAndAtivoTrue(Integer dias);
    
    @Query("SELECT p FROM Plano p JOIN p.matriculas m GROUP BY p ORDER BY COUNT(m) DESC")
    List<Plano> findPlanosOrdenadosPorPopularidade();
}