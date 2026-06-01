package sptech.school.projeto_rancho.controller;

import sptech.school.projeto_rancho.dto.PagamentoDTO;
import sptech.school.projeto_rancho.service.PagamentoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/pagamentos")
@CrossOrigin(origins = "*")
public class PagamentoController {

    @Autowired
    private PagamentoService service;

    @GetMapping
    public ResponseEntity<List<PagamentoDTO>> listar(
            @RequestParam(required = false) String forma,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) Integer setorId) {

        if (forma   != null && !forma.isBlank())   return ResponseEntity.ok(service.listarPorForma(forma));
        if (status  != null && !status.isBlank())  return ResponseEntity.ok(service.listarPorStatus(status));
        if (setorId != null)                        return ResponseEntity.ok(service.listarPorSetor(setorId));
        return ResponseEntity.ok(service.listarTodos());
    }

    @GetMapping("/escala/{escalaId}")
    public ResponseEntity<List<PagamentoDTO>> porEscala(@PathVariable Long escalaId) {
        return ResponseEntity.ok(service.listarPorEscala(escalaId));
    }

    @PostMapping
    public ResponseEntity<?> criar(@Valid @RequestBody PagamentoDTO dto) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(service.criar(dto));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    @PatchMapping("/{id}/pagar")
    public ResponseEntity<?> pagar(
            @PathVariable Integer id,
            @RequestBody Map<String, String> body) {
        try {
            String forma = body.get("forma");
            return ResponseEntity.ok(service.pagar(id, forma));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    @GetMapping("/resumo/home")
    public ResponseEntity<Map<String, Object>> resumoHome() {
        return ResponseEntity.ok(service.resumoHome());
    }

    @GetMapping("/resumo/setor/{setorId}")
    public ResponseEntity<Map<String, Object>> resumoSetor(
            @PathVariable Integer setorId,
            @RequestParam(required = false, defaultValue = "mensal") String periodo) {
        return ResponseEntity.ok(service.resumoSetorPeriodo(setorId, periodo));
    }

    @GetMapping("/resumo/historico")
    public ResponseEntity<Map<String, Object>> resumoHistorico(
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data) {
        return ResponseEntity.ok(service.resumoHistorico(data));
    }

    @GetMapping("/semana")
    public ResponseEntity<List<PagamentoDTO>> listaSemana(
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data) {
        return ResponseEntity.ok(service.listarSemana(data));
    }

    @GetMapping("/resumo/fim-de-semana")
    public ResponseEntity<Map<String, Object>> resumoFimDeSemana(
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data) {
        return ResponseEntity.ok(service.resumoFimDeSemana(data));
    }

    @GetMapping("/comparativo/fins-de-semana")
    public ResponseEntity<List<Map<String, Object>>> comparativoFinsDeSemana(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fim) {
        return ResponseEntity.ok(service.comparativoFinsDeSemana(inicio, fim));
    }
}
