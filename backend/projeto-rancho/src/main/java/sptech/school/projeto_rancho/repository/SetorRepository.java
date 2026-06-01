package sptech.school.projeto_rancho.repository;

import sptech.school.projeto_rancho.model.Setor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SetorRepository extends JpaRepository<Setor, Integer> {
    boolean existsByNome(String nome);
}
