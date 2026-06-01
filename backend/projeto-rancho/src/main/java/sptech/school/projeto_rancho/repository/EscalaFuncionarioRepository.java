package sptech.school.projeto_rancho.repository;

import sptech.school.projeto_rancho.model.EscalaFuncionario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface EscalaFuncionarioRepository extends JpaRepository<EscalaFuncionario, Integer> {

    List<EscalaFuncionario> findByEscalaId(Long escalaId);

    List<EscalaFuncionario> findByFreelancerId(Long freelancerId);

    boolean existsByEscalaIdAndFreelancerId(Long escalaId, Long freelancerId);

    List<EscalaFuncionario> findByEscalaIdAndSetorId(Long escalaId, Integer setorId);

    List<EscalaFuncionario> findByEscalaIdAndCompareceu(Long escalaId, Boolean compareceu);

    @Query("SELECT ef FROM EscalaFuncionario ef " +
           "WHERE ef.escala.dataEscala BETWEEN :inicio AND :fim " +
           "ORDER BY ef.escala.dataEscala ASC")
    List<EscalaFuncionario> findByPeriodo(
        @Param("inicio") LocalDate inicio,
        @Param("fim") LocalDate fim
    );

    @Query("SELECT ef FROM EscalaFuncionario ef " +
           "WHERE ef.freelancer.id = :freelancerId " +
           "AND ef.escala.dataEscala BETWEEN :inicio AND :fim")
    List<EscalaFuncionario> findByFreelancerIdAndPeriodo(
        @Param("freelancerId") Long freelancerId,
        @Param("inicio") LocalDate inicio,
        @Param("fim") LocalDate fim
    );

    @Query("SELECT s.nome, COUNT(DISTINCT ef.freelancer.id) FROM EscalaFuncionario ef " +
           "JOIN ef.setor s " +
           "GROUP BY s.nome " +
           "ORDER BY s.nome")
    List<Object[]> countFreelancersBySetor();
}
