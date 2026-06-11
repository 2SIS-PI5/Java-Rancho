package sptech.school.projeto_rancho.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import sptech.school.projeto_rancho.dto.EscalaFuncionarioDTO;
import sptech.school.projeto_rancho.service.EscalaFuncionarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Tag(name = "Escala de Funcionários", description = "Vínculo de freelancers a escalas de trabalho")
@RestController
@RequestMapping("/api/escalas/{escalaId}/funcionarios")
@CrossOrigin(origins = "*")
public class EscalaFuncionarioController {

    @Autowired
    private EscalaFuncionarioService service;

    @Operation(summary = "Listar freelancers de uma escala",
               description = "Filtra opcionalmente por comparecimento e/ou setor.")
    @GetMapping
    public ResponseEntity<List<EscalaFuncionarioDTO>> listar(
            @Parameter(description = "ID da escala") @PathVariable Long escalaId,
            @Parameter(description = "Filtrar por comparecimento") @RequestParam(required = false) Boolean compareceu,
            @Parameter(description = "Filtrar por ID do setor") @RequestParam(required = false) Integer setorId) {
        return ResponseEntity.ok(service.listar(escalaId, compareceu, setorId));
    }

    @Operation(summary = "Adicionar freelancer à escala")
    @ApiResponse(responseCode = "201", description = "Freelancer adicionado à escala")
    @ApiResponse(responseCode = "404", description = "Escala ou freelancer não encontrado")
    @PostMapping
    public ResponseEntity<?> adicionar(
            @Parameter(description = "ID da escala") @PathVariable Long escalaId,
            @Valid @RequestBody EscalaFuncionarioDTO dto) {
        dto.setEscalaId(escalaId);
        EscalaFuncionarioDTO criado = service.adicionar(escalaId, dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(criado);
    }

    @Operation(summary = "Remover freelancer da escala")
    @ApiResponse(responseCode = "200", description = "Freelancer removido da escala")
    @ApiResponse(responseCode = "404", description = "Vínculo não encontrado")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> remover(
            @Parameter(description = "ID da escala") @PathVariable Long escalaId,
            @Parameter(description = "ID do vínculo escala-funcionário") @PathVariable Integer id) {
        service.remover(escalaId, id);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Contagem de comparecimento em uma escala")
    @GetMapping("/comparecimento")
    public ResponseEntity<Map<String, Object>> contarComparecimento(
            @Parameter(description = "ID da escala") @PathVariable Long escalaId) {
        Long total = service.contarComparecimento(escalaId);
        return ResponseEntity.ok(Map.of(
                "escalaId", escalaId,
                "compareceram", total
        ));
    }
}
