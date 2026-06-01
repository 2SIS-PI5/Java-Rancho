package sptech.school.projeto_rancho.service;

import sptech.school.projeto_rancho.dto.PagamentoDTO;
import sptech.school.projeto_rancho.exception.RecursoNaoEncontradoException;
import sptech.school.projeto_rancho.mapper.PagamentoMapper;
import sptech.school.projeto_rancho.model.EscalaFuncionario;
import sptech.school.projeto_rancho.model.Freelancer;
import sptech.school.projeto_rancho.model.Pagamento;
import sptech.school.projeto_rancho.repository.EscalaFuncionarioRepository;
import sptech.school.projeto_rancho.repository.FreelancerRepository;
import sptech.school.projeto_rancho.repository.PagamentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PagamentoService {

    @Autowired
    private PagamentoRepository repo;

    @Autowired
    private FreelancerRepository freelancerRepo;

    @Autowired
    private EscalaFuncionarioRepository efRepo;

    @Autowired
    private PagamentoMapper mapper;

    public List<PagamentoDTO> listarTodos() {
        return repo.findAll().stream().map(mapper::toDTO).collect(Collectors.toList());
    }

    public List<PagamentoDTO> listarPorForma(String forma) {
        return repo.findByFormaPagamentoUtilizada(forma)
                .stream().map(mapper::toDTO).collect(Collectors.toList());
    }

    public List<PagamentoDTO> listarPorStatus(String status) {
        return repo.findByStatusPagamento(status)
                .stream().map(mapper::toDTO).collect(Collectors.toList());
    }

    public List<PagamentoDTO> listarPorSetor(Integer setorId) {
        return repo.findBySetorId(setorId)
                .stream().map(mapper::toDTO).collect(Collectors.toList());
    }

    public List<PagamentoDTO> listarPorEscala(Long escalaId) {
        return repo.findByEscalaId(escalaId)
                .stream().map(mapper::toDTO).collect(Collectors.toList());
    }

    public PagamentoDTO criar(PagamentoDTO dto) {
        Freelancer freelancer = freelancerRepo.findById(dto.getFreelancerId())
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Freelancer não encontrado. ID: " + dto.getFreelancerId()));

        EscalaFuncionario ef = efRepo.findById(dto.getEscalaFuncionarioId())
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Escala-funcionário não encontrado. ID: " + dto.getEscalaFuncionarioId()));

        Pagamento p = mapper.toEntity(dto, freelancer, ef);
        return mapper.toDTO(repo.save(p));
    }

    public PagamentoDTO pagar(Integer id, String forma) {
        Pagamento p = repo.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Pagamento não encontrado. ID: " + id));

        if (!"Pix".equals(forma) && !"Dinheiro".equals(forma)) {
            throw new RuntimeException("Forma de pagamento inválida. Use 'Pix' ou 'Dinheiro'.");
        }

        p.setStatusPagamento("Pago");
        p.setFormaPagamentoUtilizada(forma);
        p.setDataPagamento(LocalDateTime.now());
        return mapper.toDTO(repo.save(p));
    }

    public Map<String, Object> resumoHome() {
        LocalDate hoje = LocalDate.now();

        LocalDateTime inicioMes  = hoje.withDayOfMonth(1).atStartOfDay();
        LocalDateTime fimMes     = hoje.withDayOfMonth(hoje.lengthOfMonth()).atTime(LocalTime.MAX);

        LocalDateTime inicioTrim = hoje.minusMonths(2).withDayOfMonth(1).atStartOfDay();
        LocalDateTime fimTrim    = fimMes;

        LocalDateTime inicioAno  = hoje.withDayOfYear(1).atStartOfDay();
        LocalDateTime fimAno     = hoje.withDayOfYear(hoje.lengthOfYear()).atTime(LocalTime.MAX);

        Map<String, Object> resumo = new HashMap<>();
        resumo.put("totalMensal",              safe(repo.totalPagoPeriodo(inicioMes,  fimMes)));
        resumo.put("totalTrimestral",          safe(repo.totalPagoPeriodo(inicioTrim, fimTrim)));
        resumo.put("totalAnual",               safe(repo.totalPagoPeriodo(inicioAno,  fimAno)));
        resumo.put("totalMensalDinheiro",      safe(repo.totalPorFormaPeriodo("Dinheiro", inicioMes, fimMes)));
        resumo.put("totalMensalPix",           safe(repo.totalPorFormaPeriodo("Pix",      inicioMes, fimMes)));
        return resumo;
    }

    public Map<String, Object> resumoSetorPeriodo(Integer setorId, String periodo) {
        LocalDate hoje = LocalDate.now();
        LocalDateTime inicio, fim;

        switch (periodo != null ? periodo.toLowerCase() : "mensal") {
            case "trimestral":
                inicio = hoje.minusMonths(2).withDayOfMonth(1).atStartOfDay();
                fim    = hoje.withDayOfMonth(hoje.lengthOfMonth()).atTime(LocalTime.MAX);
                break;
            case "anual":
                inicio = hoje.withDayOfYear(1).atStartOfDay();
                fim    = hoje.withDayOfYear(hoje.lengthOfYear()).atTime(LocalTime.MAX);
                break;
            default:
                inicio = hoje.withDayOfMonth(1).atStartOfDay();
                fim    = hoje.withDayOfMonth(hoje.lengthOfMonth()).atTime(LocalTime.MAX);
        }

        BigDecimal total = safe(repo.totalPorSetorPeriodo(setorId, inicio, fim));

        Map<String, Object> map = new HashMap<>();
        map.put("setorId",  setorId);
        map.put("periodo",  periodo);
        map.put("total",    total);
        return map;
    }

    public Map<String, Object> resumoHistorico(LocalDate data) {
        LocalDate d = data != null ? data : LocalDate.now();

        LocalDate inicioSemana = d.with(java.time.DayOfWeek.MONDAY);
        LocalDate fimSemana    = inicioSemana.plusDays(6);
        LocalDateTime ini = inicioSemana.atStartOfDay();
        LocalDateTime fim = fimSemana.atTime(LocalTime.MAX);

        Map<String, Object> resumo = new HashMap<>();
        resumo.put("diaSelecionado",        d.toString());
        resumo.put("totalSemana",           safe(repo.totalPagoPeriodo(ini, fim)));
        resumo.put("totalSemanaDinheiro",   safe(repo.totalPorFormaPeriodo("Dinheiro", ini, fim)));
        resumo.put("totalSemanaPix",        safe(repo.totalPorFormaPeriodo("Pix",      ini, fim)));
        resumo.put("totalDia",              safe(repo.totalPagoPorDia(d)));
        resumo.put("totalDiaDinheiro",      safe(repo.totalPorFormaDia("Dinheiro", d)));
        resumo.put("totalDiaPix",           safe(repo.totalPorFormaDia("Pix",      d)));
        resumo.put("funcionariosPagosDia",  repo.countFuncionariosPagosDia(d));
        return resumo;
    }

    public Map<String, Object> resumoFimDeSemana(LocalDate data) {
        LocalDate d = data != null ? data : LocalDate.now();
        LocalDate inicioSemana = d.with(DayOfWeek.MONDAY);
        LocalDate fimSemana = inicioSemana.plusDays(6);
        LocalDateTime ini = inicioSemana.atStartOfDay();
        LocalDateTime fim = fimSemana.atTime(LocalTime.MAX);

        Map<String, Object> resumo = new LinkedHashMap<>();
        resumo.put("semanaInicio", inicioSemana.toString());
        resumo.put("semanaFim", fimSemana.toString());
        resumo.put("total", safe(repo.totalPagoPeriodo(ini, fim)));
        resumo.put("totalDinheiro", safe(repo.totalPorFormaPeriodo("Dinheiro", ini, fim)));
        resumo.put("totalPix", safe(repo.totalPorFormaPeriodo("Pix", ini, fim)));
        return resumo;
    }

    public List<Map<String, Object>> comparativoFinsDeSemana(LocalDate inicio, LocalDate fim) {
        LocalDateTime ini = inicio.atStartOfDay();
        LocalDateTime fimDt = fim.atTime(LocalTime.MAX);

        List<Pagamento> pagamentos = repo.findPagosEntre(ini, fimDt);

        Map<LocalDate, BigDecimal> porSemana = new LinkedHashMap<>();
        Map<LocalDate, BigDecimal> porSemanaDinheiro = new LinkedHashMap<>();
        Map<LocalDate, BigDecimal> porSemanaPix = new LinkedHashMap<>();

        for (Pagamento p : pagamentos) {
            LocalDate semana = p.getDataPagamento().toLocalDate().with(DayOfWeek.MONDAY);
            porSemana.merge(semana, p.getValor(), BigDecimal::add);
            if ("Dinheiro".equals(p.getFormaPagamentoUtilizada())) {
                porSemanaDinheiro.merge(semana, p.getValor(), BigDecimal::add);
            } else if ("Pix".equals(p.getFormaPagamentoUtilizada())) {
                porSemanaPix.merge(semana, p.getValor(), BigDecimal::add);
            }
        }

        return porSemana.entrySet().stream()
                .map(e -> {
                    Map<String, Object> item = new LinkedHashMap<>();
                    item.put("semanaInicio", e.getKey().toString());
                    item.put("total", e.getValue());
                    item.put("totalDinheiro", porSemanaDinheiro.getOrDefault(e.getKey(), BigDecimal.ZERO));
                    item.put("totalPix", porSemanaPix.getOrDefault(e.getKey(), BigDecimal.ZERO));
                    return item;
                })
                .collect(Collectors.toList());
    }

    public List<PagamentoDTO> listarSemana(LocalDate data) {
        LocalDate d = data != null ? data : LocalDate.now();
        LocalDate inicio = d.with(java.time.DayOfWeek.MONDAY);
        LocalDate fim    = inicio.plusDays(6);
        return repo.findBySemana(inicio.atStartOfDay(), fim.atTime(LocalTime.MAX))
                .stream().map(mapper::toDTO).collect(Collectors.toList());
    }

    private BigDecimal safe(BigDecimal v) {
        return v != null ? v : BigDecimal.ZERO;
    }
}
