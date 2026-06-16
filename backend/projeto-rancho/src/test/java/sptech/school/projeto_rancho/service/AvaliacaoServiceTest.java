package sptech.school.projeto_rancho.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
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

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("AvaliacaoService — Testes Unitários")
class AvaliacaoServiceTest {

    @Mock AvaliacaoRepository avaliacaoRepo;
    @Mock FreelancerRepository freelancerRepo;
    @Mock UsuarioRepository usuarioRepo;
    @Mock AvaliacaoMapper mapper;
    @Mock FreelancerMapper freelancerMapper;

    @InjectMocks AvaliacaoService service;

    private Freelancer freelancer;
    private FreelancerDTO freelancerDTO;
    private Avaliacao avaliacao;
    private AvaliacaoDTO avaliacaoDTO;

    @BeforeEach
    void setUp() {
        freelancer = new Freelancer();
        freelancer.setId(1L);
        freelancer.setNome("João Silva");

        freelancerDTO = new FreelancerDTO();
        freelancerDTO.setId(1L);
        freelancerDTO.setNome("João Silva");

        avaliacao = new Avaliacao();
        avaliacao.setId(10); // Integer

        avaliacaoDTO = new AvaliacaoDTO();
        avaliacaoDTO.setFreelancerId(1L);
        avaliacaoDTO.setNota(5.0f); // Float
    }

    // ── listarFreelancersComMedia ──────────────────

    @Test
    @DisplayName("listarFreelancersComMedia() retorna freelancers com média calculada")
    void listarFreelancersComMedia_retornaComMedia() {
        when(freelancerRepo.findAll()).thenReturn(List.of(freelancer));
        when(freelancerMapper.toDTO(freelancer)).thenReturn(freelancerDTO);
        when(avaliacaoRepo.mediaNotaPorFreelancer(1L)).thenReturn(4.5);

        List<FreelancerDTO> result = service.listarFreelancersComMedia();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getMediaAvaliacao()).isEqualTo(4.5);
    }

    @Test
    @DisplayName("listarFreelancersComMedia() retorna lista vazia quando não há freelancers")
    void listarFreelancersComMedia_semFreelancers_retornaVazio() {
        when(freelancerRepo.findAll()).thenReturn(List.of());

        assertThat(service.listarFreelancersComMedia()).isEmpty();
    }

    // ── buscarFreelancerPorNome ────────────────────

    @Test
    @DisplayName("buscarFreelancerPorNome() retorna freelancers que contêm o nome")
    void buscarFreelancerPorNome_encontrado_retornaComMedia() {
        when(freelancerRepo.findByNomeContainingIgnoreCase("João")).thenReturn(List.of(freelancer));
        when(freelancerMapper.toDTO(freelancer)).thenReturn(freelancerDTO);
        when(avaliacaoRepo.mediaNotaPorFreelancer(1L)).thenReturn(3.0);

        List<FreelancerDTO> result = service.buscarFreelancerPorNome("João");

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getMediaAvaliacao()).isEqualTo(3.0);
    }

    @Test
    @DisplayName("buscarFreelancerPorNome() retorna vazio quando nenhum encontrado")
    void buscarFreelancerPorNome_nenhum_retornaVazio() {
        when(freelancerRepo.findByNomeContainingIgnoreCase("Inexistente")).thenReturn(List.of());

        assertThat(service.buscarFreelancerPorNome("Inexistente")).isEmpty();
    }

    // ── listarPorFreelancer ────────────────────────

    @Test
    @DisplayName("listarPorFreelancer() retorna avaliações do freelancer")
    void listarPorFreelancer_encontradas_retornaLista() {
        when(avaliacaoRepo.findByFreelancerId(1L)).thenReturn(List.of(avaliacao));
        when(mapper.toDTO(avaliacao)).thenReturn(avaliacaoDTO);

        List<AvaliacaoDTO> result = service.listarPorFreelancer(1L);

        assertThat(result).hasSize(1).contains(avaliacaoDTO);
    }

    // ── criar ──────────────────────────────────────

    @Test
    @DisplayName("criar() salva avaliação sem usuário quando usuarioSistemaId é null")
    void criar_semUsuario_salvaSucesso() {
        avaliacaoDTO.setUsuarioSistemaId(null);
        when(freelancerRepo.findById(1L)).thenReturn(Optional.of(freelancer));
        when(mapper.toEntity(avaliacaoDTO, freelancer, null)).thenReturn(avaliacao);
        when(avaliacaoRepo.save(avaliacao)).thenReturn(avaliacao);
        when(mapper.toDTO(avaliacao)).thenReturn(avaliacaoDTO);

        AvaliacaoDTO result = service.criar(avaliacaoDTO);

        assertThat(result).isEqualTo(avaliacaoDTO);
        verify(usuarioRepo, never()).findById(any());
    }

    @Test
    @DisplayName("criar() busca usuário quando usuarioSistemaId é informado")
    void criar_comUsuario_buscaUsuario() {
        avaliacaoDTO.setUsuarioSistemaId(5L); // Long no DTO
        Usuario usuario = new Usuario();
        usuario.setId(5);

        when(freelancerRepo.findById(1L)).thenReturn(Optional.of(freelancer));
        when(usuarioRepo.findById(any())).thenReturn(Optional.of(usuario));
        when(mapper.toEntity(eq(avaliacaoDTO), eq(freelancer), any(Usuario.class))).thenReturn(avaliacao);
        when(avaliacaoRepo.save(avaliacao)).thenReturn(avaliacao);
        when(mapper.toDTO(avaliacao)).thenReturn(avaliacaoDTO);

        service.criar(avaliacaoDTO);

        verify(usuarioRepo).findById(any());
    }

    @Test
    @DisplayName("criar() lança exceção quando freelancer não existe")
    void criar_freelancerNaoEncontrado_lancaExcecao() {
        when(freelancerRepo.findById(99L)).thenReturn(Optional.empty());
        avaliacaoDTO.setFreelancerId(99L);

        assertThatThrownBy(() -> service.criar(avaliacaoDTO))
                .isInstanceOf(RecursoNaoEncontradoException.class)
                .hasMessageContaining("99");
    }

    // ── mediaPorFreelancer ─────────────────────────

    @Test
    @DisplayName("mediaPorFreelancer() retorna a média calculada")
    void mediaPorFreelancer_retornaMedia() {
        when(freelancerRepo.existsById(1L)).thenReturn(true);
        when(avaliacaoRepo.mediaNotaPorFreelancer(1L)).thenReturn(4.2);

        Double media = service.mediaPorFreelancer(1L);

        assertThat(media).isEqualTo(4.2);
    }

    @Test
    @DisplayName("mediaPorFreelancer() retorna 0.0 quando não há avaliações")
    void mediaPorFreelancer_semAvaliacoes_retornaZero() {
        when(freelancerRepo.existsById(1L)).thenReturn(true);
        when(avaliacaoRepo.mediaNotaPorFreelancer(1L)).thenReturn(null);

        assertThat(service.mediaPorFreelancer(1L)).isEqualTo(0.0);
    }

    @Test
    @DisplayName("mediaPorFreelancer() lança exceção quando freelancer não existe")
    void mediaPorFreelancer_freelancerNaoExiste_lancaExcecao() {
        when(freelancerRepo.existsById(99L)).thenReturn(false);

        assertThatThrownBy(() -> service.mediaPorFreelancer(99L))
                .isInstanceOf(RecursoNaoEncontradoException.class)
                .hasMessageContaining("99");
    }
}
