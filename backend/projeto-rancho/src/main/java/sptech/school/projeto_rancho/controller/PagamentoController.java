package sptech.school.projeto_rancho.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
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

@Tag(name = "Pagamentos", description = "Gestão de pagamentos a freelancers")
@RestController
@RequestMapping("/api/pagamentos")
@CrossOrigin(origins = "*")
public class PagamentoController {

    @Autowired
    private PagamentoService service;

    @Operation(summary = "Listar pagamentos",
               description = "Retorna todos os pagamentos. Filtra por forma de pagamento, status ou setor.")
    @GetMapping
    public ResponseEntity<List<PagamentoDTO>> listar(
            @Parameter(description = "Forma de pagamento (Pix, Dinheiro)") @RequestParam(required = false) String forma,
            @Parameter(description = "Status do pagamento (Pendente, Pago)") @RequestParam(required = false) String status,
            @Parameter(description = "ID do setor") @RequestParam(required = false) Integer setorId) {

        if (forma != null && !forma.isBlank()) return ResponseEntity.ok(service.listarPorForma(forma));
        if (status != null && !status.isBlank()) return ResponseEntity.ok(service.listarPorStatus(status));
        if (setorId != null) return ResponseEntity.ok(service.listarPorSetor(setorId));
        return ResponseEntity.ok(service.listarTodos());
    }

    @Operation(summary = "Listar pagamentos de uma escala")
    @GetMapping("/escala/{escalaId}")
    public ResponseEntity<List<PagamentoDTO>> porEscala(
            @Parameter(description = "ID da escala") @PathVariable Long escalaId) {
        return ResponseEntity.ok(service.listarPorEscala(escalaId));
    }

    @Operation(summary = "Criar pagamento")
    @ApiResponse(responseCode = "201", description = "Pagamento criado com sucesso")
    @PostMapping
    public ResponseEntity<?> criar(@Valid @RequestBody PagamentoDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.criar(dto));
    }

    @Operation(summary = "Registrar pagamento realizado",
               description = "Marca o pagamento como 'Pago' e registra a forma utilizada (Pix ou Dinheiro).")
    @ApiResponse(responseCode = "200", description = "Pagamento registrado com sucesso")
    @ApiResponse(responseCode = "404", description = "Pagamento não encontrado")
    @PatchMapping("/{id}/pagar")
    public ResponseEntity<?> pagar(
            @Parameter(description = "ID do pagamento") @PathVariable Integer id,
            @RequestBody Map<String, String> body) {
        String forma = body.get("forma");
        return ResponseEntity.ok(service.pagar(id, forma));
    }

    @Operation(summary = "Resumo financeiro da home",
               description = "Totais mensal, trimestral e anual separados por forma de pagamento.")
    @GetMapping("/resumo/home")
    public ResponseEntity<Map<String, Object>> resumoHome() {
        return ResponseEntity.ok(service.resumoHome());
    }

    @Operation(summary = "Resumo financeiro por setor e período")
    @GetMapping("/resumo/setor/{setorId}")
    public ResponseEntity<Map<String, Object>> resumoSetor(
            @Parameter(description = "ID do setor") @PathVariable Integer setorId,
            @Parameter(description = "Período: mensal, trimestral ou anual", example = "mensal")
            @RequestParam(required = false, defaultValue = "mensal") String periodo) {
        return ResponseEntity.ok(service.resumoSetorPeriodo(setorId, periodo));
    }

    @Operation(summary = "Resumo histórico de pagamentos por semana",
               description = "Totais da semana e do dia informado, separados por forma de pagamento.")
    @GetMapping("/resumo/historico")
    public ResponseEntity<Map<String, Object>> resumoHistorico(
            @Parameter(description = "Data de referência — usa hoje se omitido")
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data) {
        return ResponseEntity.ok(service.resumoHistorico(data));
    }

    @Operation(summary = "Listar pagamentos da semana")
    @GetMapping("/semana")
    public ResponseEntity<List<PagamentoDTO>> listaSemana(
            @Parameter(description = "Data de referência — usa semana atual se omitido")
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data) {
        return ResponseEntity.ok(service.listarSemana(data));
    }

    @Operation(summary = "Resumo de pagamentos do fim de semana")
    @GetMapping("/resumo/fim-de-semana")
    public ResponseEntity<Map<String, Object>> resumoFimDeSemana(
            @Parameter(description = "Data de referência — usa semana atual se omitido")
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data) {
        return ResponseEntity.ok(service.resumoFimDeSemana(data));
    }

    @Operation(summary = "Comparativo de pagamentos por fins de semana",
               description = "Agrega pagamentos semana a semana dentro do período informado.")
    @GetMapping("/comparativo/fins-de-semana")
    public ResponseEntity<List<Map<String, Object>>> comparativoFinsDeSemana(
            @Parameter(description = "Data de início do período (yyyy-MM-dd)")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @Parameter(description = "Data de fim do período (yyyy-MM-dd)")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fim) {
        return ResponseEntity.ok(service.comparativoFinsDeSemana(inicio, fim));
    }
}
