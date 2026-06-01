package sptech.school.projeto_rancho.repository;

import sptech.school.projeto_rancho.model.Avaliacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AvaliacaoRepository extends JpaRepository<Avaliacao, Integer> {

    List<Avaliacao> findByFreelancerId(Long freelancerId);

    @Query("SELECT AVG(a.nota) FROM Avaliacao a WHERE a.freelancer.id = :freelancerId")
    Double mediaNotaPorFreelancer(@Param("freelancerId") Long freelancerId);

    @Query("SELECT a.freelancer.id, a.freelancer.nome, AVG(a.nota) " +
           "FROM Avaliacao a GROUP BY a.freelancer.id, a.freelancer.nome")
    List<Object[]> mediaNotaPorTodosFreelancers();
}
