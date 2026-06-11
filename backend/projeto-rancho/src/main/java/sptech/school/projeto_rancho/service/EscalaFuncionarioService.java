package sptech.school.projeto_rancho.service;

import sptech.school.projeto_rancho.dto.EscalaFuncionarioDTO;
import sptech.school.projeto_rancho.exception.RecursoNaoEncontradoException;
import sptech.school.projeto_rancho.mapper.EscalaFuncionarioMapper;
import sptech.school.projeto_rancho.model.Escala;
import sptech.school.projeto_rancho.model.EscalaFuncionario;
import sptech.school.projeto_rancho.model.Freelancer;
import sptech.school.projeto_rancho.model.Setor;
import sptech.school.projeto_rancho.repository.EscalaFuncionarioRepository;
import sptech.school.projeto_rancho.repository.EscalaRepository;
import sptech.school.projeto_rancho.repository.FreelancerRepository;
import sptech.school.projeto_rancho.repository.SetorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class EscalaFuncionarioService {

    @Autowired
    private EscalaFuncionarioRepository repo;

    @Autowired
    private EscalaRepository escalaRepo;

    @Autowired
    private FreelancerRepository freelancerRepo;

    @Autowired
    private SetorRepository setorRepo;

    @Autowired
    private EscalaFuncionarioMapper mapper;

    public List<EscalaFuncionarioDTO> listar(Long escalaId, Boolean compareceu, Integer setorId) {
        if (setorId != null && compareceu != null) {
            return repo.findByEscalaIdAndSetorId(escalaId, setorId).stream()
                    .filter(ef -> compareceu.equals(ef.getCompareceu()))
                    .map(mapper::toDTO)
                    .collect(Collectors.toList());
        }
        if (setorId != null) {
            return repo.findByEscalaIdAndSetorId(escalaId, setorId).stream()
                    .map(mapper::toDTO)
                    .collect(Collectors.toList());
        }
        if (compareceu != null) {
            return repo.findByEscalaIdAndCompareceu(escalaId, compareceu).stream()
                    .map(mapper::toDTO)
                    .collect(Collectors.toList());
        }
        return repo.findByEscalaId(escalaId).stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }

    public EscalaFuncionarioDTO adicionar(Long escalaId, EscalaFuncionarioDTO dto) {
        Escala escala = escalaRepo.findById(escalaId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Escala não encontrada. ID: " + escalaId));

        Freelancer freelancer = freelancerRepo.findById(dto.getFreelancerId())
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Freelancer não encontrado. ID: " + dto.getFreelancerId()));

        Setor setor = null;
        if (dto.getSetorId() != null) {
            setor = setorRepo.findById(dto.getSetorId())
                    .orElseThrow(() -> new RecursoNaoEncontradoException(
                            "Setor não encontrado. ID: " + dto.getSetorId()));
        }

        EscalaFuncionario ef = mapper.toEntity(dto, escala, freelancer, setor);
        return mapper.toDTO(repo.save(ef));
    }

    public void remover(Long escalaId, Integer id) {
        EscalaFuncionario ef = repo.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Vínculo escala-funcionário não encontrado. ID: " + id));

        if (!ef.getEscala().getId().equals(escalaId)) {
            throw new IllegalArgumentException("O vínculo não pertence à escala informada.");
        }

        repo.delete(ef);
    }

    public Long contarComparecimento(Long escalaId) {
        return repo.findByEscalaIdAndCompareceu(escalaId, true)
                .stream()
                .count();
    }

    public Long contarSemanaPassada() {
        LocalDate hoje = LocalDate.now();
        LocalDate inicioSemanaPassada = hoje.minusWeeks(1).with(java.time.DayOfWeek.MONDAY);
        LocalDate fimSemanaPassada = inicioSemanaPassada.plusDays(6);
        return repo.findByPeriodo(inicioSemanaPassada, fimSemanaPassada).stream()
                .map(ef -> ef.getFreelancer().getId())
                .distinct()
                .count();
    }

    public List<Map<String, Object>> contarPorSetor() {
        return repo.countFreelancersBySetor().stream()
                .map(obj -> {
                    Map<String, Object> item = new java.util.HashMap<>();
                    item.put("setor", (String) obj[0]);
                    item.put("total", (Long) obj[1]);
                    return item;
                })
                .collect(Collectors.toList());
    }
}
