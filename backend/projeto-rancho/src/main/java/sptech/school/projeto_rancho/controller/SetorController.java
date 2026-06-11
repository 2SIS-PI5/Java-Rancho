package sptech.school.projeto_rancho.controller;

import sptech.school.projeto_rancho.dto.SetorDTO;
import sptech.school.projeto_rancho.service.SetorService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/setores")
@CrossOrigin(origins = "*")
public class SetorController {

    @Autowired
    private SetorService setorService;

    @GetMapping
    public ResponseEntity<List<SetorDTO>> listar() {
        return ResponseEntity.ok(setorService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscar(@PathVariable Integer id) {
        return setorService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> criar(@Valid @RequestBody SetorDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(setorService.criar(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(@PathVariable Integer id, @Valid @RequestBody SetorDTO dto) {
        return ResponseEntity.ok(setorService.atualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> excluir(@PathVariable Integer id) {
        setorService.excluir(id);
        return ResponseEntity.ok().build();
    }
}
