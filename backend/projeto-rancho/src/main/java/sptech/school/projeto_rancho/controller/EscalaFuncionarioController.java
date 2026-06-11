package sptech.school.projeto_rancho.controller;

import sptech.school.projeto_rancho.dto.EscalaFuncionarioDTO;
import sptech.school.projeto_rancho.service.EscalaFuncionarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/escalas/{escalaId}/funcionarios")
@CrossOrigin(origins = "*")
public class EscalaFuncionarioController {

    @Autowired
    private EscalaFuncionarioService service;

    @GetMapping
    public ResponseEntity<List<EscalaFuncionarioDTO>> listar(
            @PathVariable Long escalaId,
            @RequestParam(required = false) Boolean compareceu,
            @RequestParam(required = false) Integer setorId) {
        return ResponseEntity.ok(service.listar(escalaId, compareceu, setorId));
    }

    @PostMapping
    public ResponseEntity<?> adicionar(
            @PathVariable Long escalaId,
            @Valid @RequestBody EscalaFuncionarioDTO dto) {
        dto.setEscalaId(escalaId);
        EscalaFuncionarioDTO criado = service.adicionar(escalaId, dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(criado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> remover(
            @PathVariable Long escalaId,
            @PathVariable Integer id) {
        service.remover(escalaId, id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/comparecimento")
    public ResponseEntity<Map<String, Object>> contarComparecimento(@PathVariable Long escalaId) {
        Long total = service.contarComparecimento(escalaId);
        return ResponseEntity.ok(Map.of(
                "escalaId", escalaId,
                "compareceram", total
        ));
    }
}
