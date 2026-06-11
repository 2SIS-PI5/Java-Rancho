package sptech.school.projeto_rancho.service;

import sptech.school.projeto_rancho.dto.AvaliacaoDTO;
import sptech.school.projeto_rancho.dto.FreelancerDTO;
import sptech.school.projeto_rancho.exception.RecursoNaoEncontradoException;
import sptech.school.projeto_rancho.mapper.AvaliacaoMapper;
import sptech.school.projeto_rancho.mapper.FreelancerMapper;
import sptech.school.projeto_rancho.model.Avaliacao;
import sptech.school.projeto_rancho.model.Freelancer;
import sptech.school.projeto_rancho.model.Usuario;
import sptech.school.projeto_rancho.repository.AvaliacaoRepository;
import sptech.school.projeto_rancho.repository.FreelancerRepository;
import sptech.school.projeto_rancho.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AvaliacaoService {

    @Autowired
    private AvaliacaoRepository avaliacaoRepo;

    @Autowired
    private FreelancerRepository freelancerRepo;

    @Autowired
    private UsuarioRepository usuarioRepo;

    @Autowired
    private AvaliacaoMapper mapper;

    @Autowired
    private FreelancerMapper freelancerMapper;

    public List<FreelancerDTO> listarFreelancersComMedia() {
        List<Freelancer> todos = freelancerRepo.findAll();
        return todos.stream().map(f -> {
            FreelancerDTO dto = freelancerMapper.toDTO(f);
            Double media = avaliacaoRepo.mediaNotaPorFreelancer(f.getId());
            dto.setMediaAvaliacao(media);
            return dto;
        }).collect(Collectors.toList());
    }

    public List<FreelancerDTO> buscarFreelancerPorNome(String nome) {
        List<Freelancer> encontrados = freelancerRepo.findByNomeContainingIgnoreCase(nome);
        return encontrados.stream().map(f -> {
            FreelancerDTO dto = freelancerMapper.toDTO(f);
            Double media = avaliacaoRepo.mediaNotaPorFreelancer(f.getId());
            dto.setMediaAvaliacao(media);
            return dto;
        }).collect(Collectors.toList());
    }

    public List<AvaliacaoDTO> listarPorFreelancer(Long freelancerId) {
        return avaliacaoRepo.findByFreelancerId(freelancerId)
                .stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }

    public AvaliacaoDTO criar(AvaliacaoDTO dto) {
        Freelancer freelancer = freelancerRepo.findById(dto.getFreelancerId())
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Freelancer não encontrado. ID: " + dto.getFreelancerId()));

        Usuario usuario = null;
        if (dto.getUsuarioSistemaId() != null) {
            usuario = usuarioRepo.findById(dto.getUsuarioSistemaId()).orElse(null);
        }

        Avaliacao avaliacao = mapper.toEntity(dto, freelancer, usuario);
        return mapper.toDTO(avaliacaoRepo.save(avaliacao));
    }

    public Double mediaPorFreelancer(Long freelancerId) {
        if (!freelancerRepo.existsById(freelancerId)) {
            throw new RecursoNaoEncontradoException("Freelancer não encontrado. ID: " + freelancerId);
        }
        Double media = avaliacaoRepo.mediaNotaPorFreelancer(freelancerId);
        return media != null ? media : 0.0;
    }
}
