package sptech.school.projeto_rancho.controller;

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

@RestController
@RequestMapping("/api/historico")
@CrossOrigin(origins = "*")
public class HistoricoController {

    @Autowired
    private PagamentoService pagamentoService;

    @Autowired
    private EscalaService escalaService;

    @GetMapping("/resumo")
    public ResponseEntity<Map<String, Object>> resumo(
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data) {
        return ResponseEntity.ok(pagamentoService.resumoHistorico(data));
    }

    @GetMapping("/pagamentos")
    public ResponseEntity<List<PagamentoDTO>> pagamentosSemana(
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data) {
        return ResponseEntity.ok(pagamentoService.listarSemana(data));
    }

    @GetMapping("/escalas")
    public ResponseEntity<?> escalas(
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fim) {

        if (inicio == null || fim == null) {
            return ResponseEntity.badRequest().build();
        }
        List<EscalaDTO> escalas = escalaService.listar(inicio, fim);
        return ResponseEntity.ok(escalas);
    }

    @GetMapping("/escalas/semana")
    public ResponseEntity<Map<String, Object>> escalasSemana(
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
