package sptech.school.projeto_rancho.controller;

import sptech.school.projeto_rancho.dto.AvaliacaoDTO;
import sptech.school.projeto_rancho.dto.FreelancerDTO;
import sptech.school.projeto_rancho.service.AvaliacaoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/avaliacoes")
@CrossOrigin(origins = "*")
public class AvaliacaoController {

    @Autowired
    private AvaliacaoService avaliacaoService;

    @GetMapping("/freelancers")
    public ResponseEntity<List<FreelancerDTO>> listarFreelancers(
            @RequestParam(required = false) String nome) {
        if (nome != null && !nome.isBlank()) {
            return ResponseEntity.ok(avaliacaoService.buscarFreelancerPorNome(nome));
        }
        return ResponseEntity.ok(avaliacaoService.listarFreelancersComMedia());
    }

    @GetMapping("/freelancer/{id}")
    public ResponseEntity<List<AvaliacaoDTO>> listarPorFreelancer(@PathVariable Long id) {
        return ResponseEntity.ok(avaliacaoService.listarPorFreelancer(id));
    }

    @GetMapping("/media/{freelancerId}")
    public ResponseEntity<?> media(@PathVariable Long freelancerId) {
        try {
            Double media = avaliacaoService.mediaPorFreelancer(freelancerId);
            return ResponseEntity.ok(Map.of(
                "freelancerId", freelancerId,
                "media",         media
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    @PostMapping
    public ResponseEntity<?> criar(@Valid @RequestBody AvaliacaoDTO dto) {
        try {
            AvaliacaoDTO criada = avaliacaoService.criar(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(criada);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }
}
