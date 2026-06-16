package sptech.school.projeto_rancho.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import sptech.school.projeto_rancho.dto.FreelancerDTO;
import sptech.school.projeto_rancho.exception.RecursoNaoEncontradoException;
import sptech.school.projeto_rancho.mapper.FreelancerMapper;
import sptech.school.projeto_rancho.model.Freelancer;
import sptech.school.projeto_rancho.repository.FreelancerRepository;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("FreelancerService — Testes Unitários")
class FreelancerServiceTest {

    @Mock FreelancerRepository repo;
    @Mock FreelancerMapper mapper;

    @InjectMocks FreelancerService service;

    private Freelancer freelancer;
    private FreelancerDTO dto;

    @BeforeEach
    void setUp() {
        freelancer = new Freelancer();
        freelancer.setId(1L);
        freelancer.setNome("João Silva");
        freelancer.setCpf("111.222.333-44");
        freelancer.setStatus("ativo");

        dto = new FreelancerDTO();
        dto.setId(1L);
        dto.setNome("João Silva");
        dto.setCpf("111.222.333-44");
        dto.setStatus("ativo");
    }

    // ── listar ─────────────────────────────────────

    @Test
    @DisplayName("listar() sem filtros retorna todos os freelancers")
    void listar_semFiltros_retornaTodos() {
        when(repo.buscarComFiltros(null, null, null)).thenReturn(List.of(freelancer));
        when(mapper.toDTO(freelancer)).thenReturn(dto);

        List<FreelancerDTO> result = service.listar(null, null, null);

        assertThat(result).hasSize(1).contains(dto);
        verify(repo).buscarComFiltros(null, null, null);
    }

    @Test
    @DisplayName("listar() com filtro de status repassa status correto")
    void listar_comStatus_repassaFiltro() {
        when(repo.buscarComFiltros("ativo", null, null)).thenReturn(List.of(freelancer));
        when(mapper.toDTO(freelancer)).thenReturn(dto);

        List<FreelancerDTO> result = service.listar("ativo", null, null);

        assertThat(result).hasSize(1);
        verify(repo).buscarComFiltros("ativo", null, null);
    }

    @Test
    @DisplayName("listar() com string em branco trata como null")
    void listar_stringEmBranco_trataComoNull() {
        when(repo.buscarComFiltros(null, null, null)).thenReturn(List.of());

        service.listar("  ", "  ", "  ");

        verify(repo).buscarComFiltros(null, null, null);
    }

    // ── buscarPorId ────────────────────────────────

    @Test
    @DisplayName("buscarPorId() retorna DTO quando encontrado")
    void buscarPorId_encontrado_retornaDTO() {
        when(repo.findById(1L)).thenReturn(Optional.of(freelancer));
        when(mapper.toDTO(freelancer)).thenReturn(dto);

        Optional<FreelancerDTO> result = service.buscarPorId(1L);

        assertThat(result).isPresent().contains(dto);
    }

    @Test
    @DisplayName("buscarPorId() retorna vazio quando não encontrado")
    void buscarPorId_naoEncontrado_retornaVazio() {
        when(repo.findById(99L)).thenReturn(Optional.empty());

        Optional<FreelancerDTO> result = service.buscarPorId(99L);

        assertThat(result).isEmpty();
    }

    // ── criar ──────────────────────────────────────

    @Test
    @DisplayName("criar() salva e retorna DTO quando CPF não existe")
    void criar_cpfNovo_salvaSucesso() {
        when(repo.existsByCpf(dto.getCpf())).thenReturn(false);
        when(mapper.toEntity(dto)).thenReturn(freelancer);
        when(repo.save(freelancer)).thenReturn(freelancer);
        when(mapper.toDTO(freelancer)).thenReturn(dto);

        FreelancerDTO result = service.criar(dto);

        assertThat(result).isEqualTo(dto);
        verify(repo).save(freelancer);
    }

    @Test
    @DisplayName("criar() lança RuntimeException quando CPF já existe")
    void criar_cpfDuplicado_lancaExcecao() {
        when(repo.existsByCpf(dto.getCpf())).thenReturn(true);

        assertThatThrownBy(() -> service.criar(dto))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("CPF já cadastrado");

        verify(repo, never()).save(any());
    }

    @Test
    @DisplayName("criar() não verifica CPF quando ele é nulo")
    void criar_cpfNulo_naoVerificaDuplicidade() {
        dto.setCpf(null);
        when(mapper.toEntity(dto)).thenReturn(freelancer);
        when(repo.save(freelancer)).thenReturn(freelancer);
        when(mapper.toDTO(freelancer)).thenReturn(dto);

        service.criar(dto);

        verify(repo, never()).existsByCpf(any());
        verify(repo).save(freelancer);
    }

    // ── atualizar ──────────────────────────────────

    @Test
    @DisplayName("atualizar() atualiza entidade e retorna DTO")
    void atualizar_encontrado_atualizaSucesso() {
        // CPF do DTO e da entidade são iguais → existsByCpf NÃO é chamado
        when(repo.findById(1L)).thenReturn(Optional.of(freelancer));
        when(repo.save(freelancer)).thenReturn(freelancer);
        when(mapper.toDTO(freelancer)).thenReturn(dto);

        FreelancerDTO result = service.atualizar(1L, dto);

        assertThat(result).isEqualTo(dto);
        verify(mapper).atualizarEntidade(freelancer, dto);
        verify(repo).save(freelancer);
    }


    @Test
    @DisplayName("atualizar() lança exceção quando freelancer não encontrado")
    void atualizar_naoEncontrado_lancaExcecao() {
        when(repo.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.atualizar(99L, dto))
                .isInstanceOf(RecursoNaoEncontradoException.class)
                .hasMessageContaining("99");
    }

    @Test
    @DisplayName("atualizar() lança exceção quando CPF pertence a outro freelancer")
    void atualizar_cpfDeOutro_lancaExcecao() {
        freelancer.setCpf("000.000.000-00"); // CPF diferente do DTO
        when(repo.findById(1L)).thenReturn(Optional.of(freelancer));
        when(repo.existsByCpf(dto.getCpf())).thenReturn(true);

        assertThatThrownBy(() -> service.atualizar(1L, dto))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("CPF já cadastrado para outro");
    }

    // ── excluir ────────────────────────────────────

    @Test
    @DisplayName("excluir() chama deleteById quando ID existe")
    void excluir_encontrado_deleta() {
        when(repo.existsById(1L)).thenReturn(true);

        service.excluir(1L);

        verify(repo).deleteById(1L);
    }

    @Test
    @DisplayName("excluir() lança exceção quando ID não existe")
    void excluir_naoEncontrado_lancaExcecao() {
        when(repo.existsById(99L)).thenReturn(false);

        assertThatThrownBy(() -> service.excluir(99L))
                .isInstanceOf(RecursoNaoEncontradoException.class)
                .hasMessageContaining("99");

        verify(repo, never()).deleteById(any());
    }

    // ── listarPorDistancia ─────────────────────────

    @Test
    @DisplayName("listarPorDistancia() filtra por km e status ativo")
    void listarPorDistancia_retornaFiltrados() {
        when(repo.findByDistanciaKmLessThanEqualAndStatus(10.0, "ativo"))
                .thenReturn(List.of(freelancer));
        when(mapper.toDTO(freelancer)).thenReturn(dto);

        List<FreelancerDTO> result = service.listarPorDistancia(10.0);

        assertThat(result).hasSize(1).contains(dto);
        verify(repo).findByDistanciaKmLessThanEqualAndStatus(10.0, "ativo");
    }
}
