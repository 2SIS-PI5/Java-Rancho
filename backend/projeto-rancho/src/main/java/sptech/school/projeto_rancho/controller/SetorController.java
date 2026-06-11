package sptech.school.projeto_rancho.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import sptech.school.projeto_rancho.dto.SetorDTO;
import sptech.school.projeto_rancho.service.SetorService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Setores", description = "Gestão de setores de trabalho")
@RestController
@RequestMapping("/api/setores")
@CrossOrigin(origins = "*")
public class SetorController {

    @Autowired
    private SetorService setorService;

    @Operation(summary = "Listar setores")
    @GetMapping
    public ResponseEntity<List<SetorDTO>> listar() {
        return ResponseEntity.ok(setorService.listar());
    }

    @Operation(summary = "Buscar setor por ID")
    @ApiResponse(responseCode = "200", description = "Setor encontrado")
    @ApiResponse(responseCode = "404", description = "Setor não encontrado")
    @GetMapping("/{id}")
    public ResponseEntity<?> buscar(
            @Parameter(description = "ID do setor") @PathVariable Integer id) {
        return setorService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Criar setor")
    @ApiResponse(responseCode = "201", description = "Setor criado com sucesso")
    @PostMapping
    public ResponseEntity<?> criar(@Valid @RequestBody SetorDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(setorService.criar(dto));
    }

    @Operation(summary = "Atualizar setor")
    @ApiResponse(responseCode = "200", description = "Setor atualizado")
    @ApiResponse(responseCode = "404", description = "Setor não encontrado")
    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(
            @Parameter(description = "ID do setor") @PathVariable Integer id,
            @Valid @RequestBody SetorDTO dto) {
        return ResponseEntity.ok(setorService.atualizar(id, dto));
    }

    @Operation(summary = "Excluir setor")
    @ApiResponse(responseCode = "200", description = "Setor excluído")
    @ApiResponse(responseCode = "404", description = "Setor não encontrado")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> excluir(
            @Parameter(description = "ID do setor") @PathVariable Integer id) {
        setorService.excluir(id);
        return ResponseEntity.ok().build();
    }
}
