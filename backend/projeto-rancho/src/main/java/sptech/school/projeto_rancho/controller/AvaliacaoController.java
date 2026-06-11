package sptech.school.projeto_rancho.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
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

@Tag(name = "Avaliações", description = "Avaliações de freelancers")
@RestController
@RequestMapping("/api/avaliacoes")
@CrossOrigin(origins = "*")
public class AvaliacaoController {

    @Autowired
    private AvaliacaoService avaliacaoService;

    @Operation(summary = "Listar freelancers com média de avaliação",
               description = "Retorna todos os freelancers com sua média de notas. Filtra por nome se informado.")
    @GetMapping("/freelancers")
    public ResponseEntity<List<FreelancerDTO>> listarFreelancers(
            @Parameter(description = "Filtrar por nome do freelancer")
            @RequestParam(required = false) String nome) {
        if (nome != null && !nome.isBlank()) {
            return ResponseEntity.ok(avaliacaoService.buscarFreelancerPorNome(nome));
        }
        return ResponseEntity.ok(avaliacaoService.listarFreelancersComMedia());
    }

    @Operation(summary = "Listar avaliações de um freelancer")
    @GetMapping("/freelancer/{id}")
    public ResponseEntity<List<AvaliacaoDTO>> listarPorFreelancer(
            @Parameter(description = "ID do freelancer") @PathVariable Long id) {
        return ResponseEntity.ok(avaliacaoService.listarPorFreelancer(id));
    }

    @Operation(summary = "Média de avaliação de um freelancer")
    @ApiResponse(responseCode = "200", description = "Média calculada com sucesso")
    @ApiResponse(responseCode = "404", description = "Freelancer não encontrado")
    @GetMapping("/media/{freelancerId}")
    public ResponseEntity<?> media(
            @Parameter(description = "ID do freelancer") @PathVariable Long freelancerId) {
        Double media = avaliacaoService.mediaPorFreelancer(freelancerId);
        return ResponseEntity.ok(Map.of(
            "freelancerId", freelancerId,
            "media", media
        ));
    }

    @Operation(summary = "Criar avaliação")
    @ApiResponse(responseCode = "201", description = "Avaliação criada com sucesso")
    @PostMapping
    public ResponseEntity<?> criar(@Valid @RequestBody AvaliacaoDTO dto) {
        AvaliacaoDTO criada = avaliacaoService.criar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(criada);
    }
}
