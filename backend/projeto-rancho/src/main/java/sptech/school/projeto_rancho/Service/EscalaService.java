package sptech.school.projeto_rancho.service;

import org.springframework.transaction.annotation.Transactional;
import sptech.school.projeto_rancho.dto.EscalaDTO;
import sptech.school.projeto_rancho.exception.RecursoNaoEncontradoException;
import sptech.school.projeto_rancho.mapper.EscalaMapper;
import sptech.school.projeto_rancho.model.Escala;
import sptech.school.projeto_rancho.repository.EscalaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EscalaService {

    @Autowired
    private EscalaRepository escalaRepo;

    @Autowired
    private EscalaMapper mapper;

    @Transactional(readOnly = true)
    public List<EscalaDTO> listar(LocalDate dataInicio, LocalDate dataFim) {
        if (dataInicio != null && dataFim != null) {
            return escalaRepo.findByDataEscalaBetween(dataInicio, dataFim)
                    .stream()
                    .map(mapper::toDTO)
                    .collect(Collectors.toList());
        }
        return escalaRepo.findAll().stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Optional<EscalaDTO> buscarPorId(Long id) {
        return escalaRepo.findById(id).map(mapper::toDTO);
    }

    public EscalaDTO criar(EscalaDTO dto) {
        Escala escala = mapper.toEntity(dto);
        return mapper.toDTO(escalaRepo.save(escala));
    }

    public EscalaDTO atualizar(Long id, EscalaDTO dto) {
        Escala escala = escalaRepo.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Escala não encontrada. ID: " + id));

        mapper.atualizarEntidade(escala, dto);
        return mapper.toDTO(escalaRepo.save(escala));
    }

    public void excluir(Long id) {
        if (!escalaRepo.existsById(id)) {
            throw new RecursoNaoEncontradoException("Escala não encontrada. ID: " + id);
        }
        escalaRepo.deleteById(id);
    }
}
