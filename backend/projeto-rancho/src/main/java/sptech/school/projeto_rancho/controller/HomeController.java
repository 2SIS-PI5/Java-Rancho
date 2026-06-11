package sptech.school.projeto_rancho.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import sptech.school.projeto_rancho.service.PagamentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Tag(name = "Home", description = "Resumos e indicadores para a tela inicial")
@RestController
@RequestMapping("/api/home")
@CrossOrigin(origins = "*")
public class HomeController {

    @Autowired
    private PagamentoService pagamentoService;

    @Operation(summary = "Resumo completo da home",
               description = "Totais financeiros mensais, trimestrais e anuais separados por forma de pagamento.")
    @GetMapping("/resumo")
    public ResponseEntity<Map<String, Object>> resumoCompleto() {
        return ResponseEntity.ok(pagamentoService.resumoHome());
    }

    @Operation(summary = "Resumo de pagamentos",
               description = "Alias de /resumo — retorna os mesmos totais financeiros.")
    @GetMapping("/pagamentos")
    public ResponseEntity<Map<String, Object>> resumoPagamentos() {
        return ResponseEntity.ok(pagamentoService.resumoHome());
    }

    @Operation(summary = "Pagamentos por setor e período")
    @GetMapping("/pagamentos-por-setor/{setorId}")
    public ResponseEntity<Map<String, Object>> pagamentosPorSetor(
            @Parameter(description = "ID do setor") @PathVariable Integer setorId,
            @Parameter(description = "Período: mensal, trimestral ou anual", example = "mensal")
            @RequestParam(required = false, defaultValue = "mensal") String periodo) {
        return ResponseEntity.ok(pagamentoService.resumoSetorPeriodo(setorId, periodo));
    }
}
