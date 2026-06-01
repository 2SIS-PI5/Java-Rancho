package sptech.school.projeto_rancho.controller;

import sptech.school.projeto_rancho.service.PagamentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/home")
@CrossOrigin(origins = "*")
public class HomeController {

    @Autowired
    private PagamentoService pagamentoService;

    @GetMapping("/resumo")
    public ResponseEntity<Map<String, Object>> resumoCompleto() {
        return ResponseEntity.ok(pagamentoService.resumoHome());
    }

    @GetMapping("/pagamentos")
    public ResponseEntity<Map<String, Object>> resumoPagamentos() {
        return ResponseEntity.ok(pagamentoService.resumoHome());
    }

    @GetMapping("/pagamentos-por-setor/{setorId}")
    public ResponseEntity<Map<String, Object>> pagamentosPorSetor(
            @PathVariable Integer setorId,
            @RequestParam(required = false, defaultValue = "mensal") String periodo) {
        return ResponseEntity.ok(pagamentoService.resumoSetorPeriodo(setorId, periodo));
    }
}
