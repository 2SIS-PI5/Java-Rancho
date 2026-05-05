package sptech.school.projeto_rancho.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sptech.school.projeto_rancho.model.Area;

@Repository
public interface AreaRepository extends JpaRepository<Area, Integer> {
}