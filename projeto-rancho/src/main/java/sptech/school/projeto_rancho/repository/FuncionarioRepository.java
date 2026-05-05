package sptech.school.projeto_rancho.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sptech.school.projeto_rancho.model.Funcionario;

import java.util.List;

@Repository
public interface FuncionarioRepository extends JpaRepository<Funcionario, Integer> {

    List<Funcionario> findByNomeContainingIgnoreCase(String nome);

    List<Funcionario> findByAreaId(Integer idArea);

    boolean existsByAreaId(Integer id);
}