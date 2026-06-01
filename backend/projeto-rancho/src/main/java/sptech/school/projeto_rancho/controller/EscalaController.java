package sptech.school.projeto_rancho.controller;

import sptech.school.projeto_rancho.dto.EscalaDTO;
import sptech.school.projeto_rancho.service.EscalaFuncionarioService;
import sptech.school.projeto_rancho.service.EscalaService;
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
@RequestMapping("/api/escalas")
@CrossOrigin(origins = "*")
public class EscalaController {

    @Autowired
    private EscalaService escalaService;

    @Autowired
    private EscalaFuncionarioService escalaFuncionarioService;

    @GetMapping
    public ResponseEntity<List<EscalaDTO>> listar(
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicio,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFim) {

        List<EscalaDTO> lista = escalaService.listar(dataInicio, dataFim);
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscar(@PathVariable Long id) {
        return escalaService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> criar(@Valid @RequestBody EscalaDTO dto) {
        try {
            EscalaDTO criada = escalaService.criar(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(criada);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody EscalaDTO dto) {
        try {
            EscalaDTO atualizada = escalaService.atualizar(id, dto);
            return ResponseEntity.ok(atualizada);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> excluir(@PathVariable Long id) {
        try {
            escalaService.excluir(id);
            return ResponseEntity.ok(Map.of("success", true));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", e.getMessage()));
        }
    }

    @GetMapping("/estatisticas/semana-passada")
    public ResponseEntity<Map<String, Object>> contagemSemanaPassada() {
        Long total = escalaFuncionarioService.contarSemanaPassada();
        return ResponseEntity.ok(Map.of(
                "semanaPassada", total
        ));
    }
}
