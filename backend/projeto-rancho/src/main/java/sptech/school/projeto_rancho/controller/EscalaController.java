package sptech.school.projeto_rancho.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
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

@Tag(name = "Escalas", description = "Gestão de escalas de trabalho")
@RestController
@RequestMapping("/api/escalas")
@CrossOrigin(origins = "*")
public class EscalaController {

    @Autowired
    private EscalaService escalaService;

    @Autowired
    private EscalaFuncionarioService escalaFuncionarioService;

    @Operation(summary = "Listar escalas",
               description = "Retorna todas as escalas. Filtra por período quando dataInicio e dataFim são informados.")
    @GetMapping
    public ResponseEntity<List<EscalaDTO>> listar(
            @Parameter(description = "Data de início do filtro (yyyy-MM-dd)")
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicio,
            @Parameter(description = "Data de fim do filtro (yyyy-MM-dd)")
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFim) {

        List<EscalaDTO> lista = escalaService.listar(dataInicio, dataFim);
        return ResponseEntity.ok(lista);
    }

    @Operation(summary = "Buscar escala por ID")
    @ApiResponse(responseCode = "200", description = "Escala encontrada")
    @ApiResponse(responseCode = "404", description = "Escala não encontrada")
    @GetMapping("/{id}")
    public ResponseEntity<?> buscar(
            @Parameter(description = "ID da escala") @PathVariable Long id) {
        return escalaService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Criar escala")
    @ApiResponse(responseCode = "201", description = "Escala criada com sucesso")
    @PostMapping
    public ResponseEntity<?> criar(@Valid @RequestBody EscalaDTO dto) {
        EscalaDTO criada = escalaService.criar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(criada);
    }

    @Operation(summary = "Atualizar escala")
    @ApiResponse(responseCode = "200", description = "Escala atualizada")
    @ApiResponse(responseCode = "404", description = "Escala não encontrada")
    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(
            @Parameter(description = "ID da escala") @PathVariable Long id,
            @Valid @RequestBody EscalaDTO dto) {
        EscalaDTO atualizada = escalaService.atualizar(id, dto);
        return ResponseEntity.ok(atualizada);
    }

    @Operation(summary = "Excluir escala")
    @ApiResponse(responseCode = "200", description = "Escala excluída")
    @ApiResponse(responseCode = "404", description = "Escala não encontrada")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> excluir(
            @Parameter(description = "ID da escala") @PathVariable Long id) {
        escalaService.excluir(id);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Total de freelancers escalados na semana passada")
    @GetMapping("/estatisticas/semana-passada")
    public ResponseEntity<Map<String, Object>> contagemSemanaPassada() {
        Long total = escalaFuncionarioService.contarSemanaPassada();
        return ResponseEntity.ok(Map.of(
                "semanaPassada", total
        ));
    }
}
