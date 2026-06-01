package sptech.school.projeto_rancho.repository;

import sptech.school.projeto_rancho.model.Escala;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface EscalaRepository extends JpaRepository<Escala, Long> {

    List<Escala> findByDataEscalaBetween(LocalDate inicio, LocalDate fim);

    List<Escala> findByStatusEscala(String status);
}
