package sptech.school.projeto_rancho.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.Parameter;
import sptech.school.projeto_rancho.dto.funcionario.FuncionarioRequestDTO;
import sptech.school.projeto_rancho.dto.funcionario.FuncionarioUpdateDTO;
import sptech.school.projeto_rancho.dto.funcionario.FuncionarioResponseDto;
import sptech.school.projeto_rancho.model.Funcionario;
import sptech.school.projeto_rancho.mapper.FuncionarioMapper;
import sptech.school.projeto_rancho.service.FuncionarioService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/funcionarios")
@Tag(name = "Gerenciamento de Funcionários", description = "APIs para gerenciar funcionários freelancers - cadastro, consulta, atualização e exclusão")
public class FuncionarioController {

    @Autowired
    private FuncionarioService funcionarioService;

    @GetMapping
    @Operation(summary = "Listar todos os funcionários", description = "Retorna uma lista com todos os funcionários freelancers cadastrados no sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de funcionários retornada com sucesso"),
            @ApiResponse(responseCode = "204", description = "Nenhum funcionário cadastrado")
    })
    public ResponseEntity<List<FuncionarioResponseDto>> listarTodos() {
        List<Funcionario> funcionarios = funcionarioService.listarTodos();
        List<FuncionarioResponseDto> responses = funcionarios.stream()
                .map(FuncionarioMapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }

    @PostMapping
    @Operation(summary = "Cadastrar novo funcionário", description = "Cria um novo registro de funcionário freelancer no sistema com os dados fornecidos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Funcionário cadastrado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos ou incompletos"),
            @ApiResponse(responseCode = "404", description = "Área informada não encontrada")
    })
    public ResponseEntity<FuncionarioResponseDto> criar(@RequestBody @Valid FuncionarioRequestDTO dto) {
        Funcionario funcionario = funcionarioService.criar(dto);
        if (funcionario == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.status(201).body(FuncionarioMapper.toDto(funcionario));
    }

    @GetMapping("/buscar")
    @Operation(summary = "Buscar funcionários por nome", description = "Retorna uma lista de funcionários que contenham o nome especificado (busca case-insensitive)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Funcionários encontrados com sucesso"),
            @ApiResponse(responseCode = "404", description = "Nenhum funcionário encontrado com o nome fornecido")
    })
    public ResponseEntity<List<FuncionarioResponseDto>> buscarPorNome(
            @Parameter(description = "Nome ou parte do nome do funcionário a buscar", required = true)
            @RequestParam String nome) {
        List<Funcionario> funcionarios = funcionarioService.buscarPorNome(nome);
        List<FuncionarioResponseDto> responses = funcionarios.stream()
                .map(FuncionarioMapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/area/{idArea}")
    @Operation(summary = "Buscar funcionários por área", description = "Retorna uma lista de todos os funcionários que trabalham em uma área específica")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Funcionários da área retornados com sucesso"),
            @ApiResponse(responseCode = "404", description = "Nenhum funcionário encontrado para a área informada")
    })
    public ResponseEntity<List<FuncionarioResponseDto>> buscarPorArea(
            @Parameter(description = "Identificador único da área", required = true, example = "1")
            @PathVariable Integer idArea) {
        List<Funcionario> funcionarios = funcionarioService.buscarPorArea(idArea);
        List<FuncionarioResponseDto> responses = funcionarios.stream()
                .map(FuncionarioMapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obter detalhes de um funcionário", description = "Retorna os dados completos de um funcionário específico pelo seu ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Funcionário encontrado e retornado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Funcionário não encontrado")
    })
    public ResponseEntity<FuncionarioResponseDto> buscarPorId(
            @Parameter(description = "Identificador único do funcionário", required = true, example = "1")
            @PathVariable Integer id) {
        Funcionario funcionario = funcionarioService.buscarPorId(id);
        return ResponseEntity.ok(FuncionarioMapper.toDto(funcionario));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar dados do funcionário", description = "Atualiza os dados de um funcionário existente. Apenas os campos fornecidos serão atualizados, os demais serão mantidos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Funcionário atualizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos"),
            @ApiResponse(responseCode = "404", description = "Funcionário ou área informada não encontrados")
    })
    public ResponseEntity<FuncionarioResponseDto> atualizar(
            @Parameter(description = "Identificador único do funcionário a atualizar", required = true, example = "1")
            @PathVariable Integer id,
            @RequestBody FuncionarioUpdateDTO dto) {
        Funcionario funcionario = funcionarioService.atualizar(id, dto);
        return ResponseEntity.ok(FuncionarioMapper.toDto(funcionario));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar funcionário", description = "Remove um funcionário freelancer do sistema de forma permanente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Funcionário deletado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Funcionário não encontrado")
    })
    public ResponseEntity<Void> deletar(
            @Parameter(description = "Identificador único do funcionário a deletar", required = true, example = "1")
            @PathVariable Integer id) {
        Funcionario funcionario = funcionarioService.buscarPorId(id);
        if (funcionario == null) {
            return ResponseEntity.notFound().build();
        }
        funcionarioService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}