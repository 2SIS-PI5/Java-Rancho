package sptech.school.projeto_rancho.repository;

import sptech.school.projeto_rancho.model.Pagamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface PagamentoRepository extends JpaRepository<Pagamento, Integer> {

    List<Pagamento> findByFreelancerId(Long freelancerId);

    List<Pagamento> findByStatusPagamento(String status);

    List<Pagamento> findByFormaPagamentoUtilizada(String forma);

    @Query("SELECT COALESCE(SUM(p.valor), 0) FROM Pagamento p " +
           "WHERE p.statusPagamento = 'Pago' " +
           "AND p.dataPagamento BETWEEN :inicio AND :fim")
    BigDecimal totalPagoPeriodo(@Param("inicio") LocalDateTime inicio, @Param("fim") LocalDateTime fim);

    @Query("SELECT COALESCE(SUM(p.valor), 0) FROM Pagamento p " +
           "WHERE p.statusPagamento = 'Pago' " +
           "AND p.formaPagamentoUtilizada = :forma " +
           "AND p.dataPagamento BETWEEN :inicio AND :fim")
    BigDecimal totalPorFormaPeriodo(
        @Param("forma") String forma,
        @Param("inicio") LocalDateTime inicio,
        @Param("fim") LocalDateTime fim
    );

    @Query("SELECT COALESCE(SUM(p.valor), 0) FROM Pagamento p " +
           "WHERE p.statusPagamento = 'Pago' " +
           "AND p.escalaFuncionario.setor.id = :setorId " +
           "AND p.dataPagamento BETWEEN :inicio AND :fim")
    BigDecimal totalPorSetorPeriodo(
        @Param("setorId") Integer setorId,
        @Param("inicio") LocalDateTime inicio,
        @Param("fim") LocalDateTime fim
    );

    @Query("SELECT DISTINCT p FROM Pagamento p " +
           "ORDER BY p.escalaFuncionario.escala.dataEscala DESC")
    List<Pagamento> listarTodosComFreelancer();

    List<Pagamento> findByStatusPagamentoOrderByDataPagamentoDesc(String status);

    @Query("SELECT p FROM Pagamento p " +
           "WHERE p.escalaFuncionario.setor.id = :setorId " +
           "ORDER BY p.escalaFuncionario.escala.dataEscala DESC")
    List<Pagamento> findBySetorId(@Param("setorId") Integer setorId);

    @Query("SELECT p FROM Pagamento p " +
           "WHERE p.escalaFuncionario.escala.id = :escalaId")
    List<Pagamento> findByEscalaId(@Param("escalaId") Long escalaId);

    @Query("SELECT COALESCE(SUM(p.valor), 0) FROM Pagamento p " +
           "WHERE p.statusPagamento = 'Pago' " +
           "AND DATE(p.dataPagamento) = :data")
    BigDecimal totalPagoPorDia(@Param("data") LocalDate data);

    @Query("SELECT COALESCE(SUM(p.valor), 0) FROM Pagamento p " +
           "WHERE p.statusPagamento = 'Pago' " +
           "AND p.formaPagamentoUtilizada = :forma " +
           "AND DATE(p.dataPagamento) = :data")
    BigDecimal totalPorFormaDia(
        @Param("forma") String forma,
        @Param("data") LocalDate data
    );

    @Query("SELECT COUNT(DISTINCT p.freelancer.id) FROM Pagamento p " +
           "WHERE p.statusPagamento = 'Pago' " +
           "AND DATE(p.dataPagamento) = :data")
    Long countFuncionariosPagosDia(@Param("data") LocalDate data);

    @Query("SELECT p FROM Pagamento p " +
           "WHERE p.dataPagamento BETWEEN :inicio AND :fim " +
           "ORDER BY p.dataPagamento DESC")
    List<Pagamento> findBySemana(
        @Param("inicio") LocalDateTime inicio,
        @Param("fim") LocalDateTime fim
    );

    @Query("SELECT p FROM Pagamento p " +
           "WHERE p.statusPagamento = 'Pago' " +
           "AND p.dataPagamento BETWEEN :inicio AND :fim " +
           "ORDER BY p.dataPagamento ASC")
    List<Pagamento> findPagosEntre(
        @Param("inicio") LocalDateTime inicio,
        @Param("fim") LocalDateTime fim
    );
}
