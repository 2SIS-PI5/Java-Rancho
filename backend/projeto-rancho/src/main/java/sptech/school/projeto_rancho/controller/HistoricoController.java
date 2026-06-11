package sptech.school.projeto_rancho.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import sptech.school.projeto_rancho.dto.EscalaDTO;
import sptech.school.projeto_rancho.dto.PagamentoDTO;
import sptech.school.projeto_rancho.service.EscalaService;
import sptech.school.projeto_rancho.service.PagamentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Tag(name = "Histórico", description = "Consultas históricas de escalas e pagamentos")
@RestController
@RequestMapping("/api/historico")
@CrossOrigin(origins = "*")
public class HistoricoController {

    @Autowired
    private PagamentoService pagamentoService;

    @Autowired
    private EscalaService escalaService;

    @Operation(summary = "Resumo histórico da semana",
               description = "Totais de pagamentos da semana e do dia informado, por forma de pagamento.")
    @GetMapping("/resumo")
    public ResponseEntity<Map<String, Object>> resumo(
            @Parameter(description = "Data de referência — usa hoje se omitido")
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data) {
        return ResponseEntity.ok(pagamentoService.resumoHistorico(data));
    }

    @Operation(summary = "Pagamentos da semana")
    @GetMapping("/pagamentos")
    public ResponseEntity<List<PagamentoDTO>> pagamentosSemana(
            @Parameter(description = "Data de referência — usa semana atual se omitido")
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data) {
        return ResponseEntity.ok(pagamentoService.listarSemana(data));
    }

    @Operation(summary = "Escalas por período")
    @ApiResponse(responseCode = "200", description = "Lista de escalas no período")
    @ApiResponse(responseCode = "400", description = "Parâmetros inicio e fim são obrigatórios")
    @GetMapping("/escalas")
    public ResponseEntity<?> escalas(
            @Parameter(description = "Data de início (yyyy-MM-dd)", required = true)
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @Parameter(description = "Data de fim (yyyy-MM-dd)", required = true)
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fim) {

        if (inicio == null || fim == null) {
            return ResponseEntity.badRequest().build();
        }
        List<EscalaDTO> escalas = escalaService.listar(inicio, fim);
        return ResponseEntity.ok(escalas);
    }

    @Operation(summary = "Escalas da semana",
               description = "Retorna as escalas da semana que contém a data informada, ou da semana atual.")
    @GetMapping("/escalas/semana")
    public ResponseEntity<Map<String, Object>> escalasSemana(
            @Parameter(description = "Data de referência — usa hoje se omitido")
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data) {

        LocalDate d = data != null ? data : LocalDate.now();
        LocalDate inicioSemana = d.with(DayOfWeek.MONDAY);
        LocalDate fimSemana = inicioSemana.plusDays(6);

        List<EscalaDTO> escalas = escalaService.listar(inicioSemana, fimSemana);

        Map<String, Object> resposta = new HashMap<>();
        resposta.put("semanaInicio", inicioSemana.toString());
        resposta.put("semanaFim", fimSemana.toString());
        resposta.put("escalas", escalas);
        return ResponseEntity.ok(resposta);
    }
}
