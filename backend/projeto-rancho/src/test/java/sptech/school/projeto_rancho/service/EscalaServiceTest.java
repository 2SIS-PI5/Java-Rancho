package sptech.school.projeto_rancho.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import sptech.school.projeto_rancho.dto.EscalaDTO;
import sptech.school.projeto_rancho.exception.RecursoNaoEncontradoException;
import sptech.school.projeto_rancho.mapper.EscalaMapper;
import sptech.school.projeto_rancho.model.Escala;
import sptech.school.projeto_rancho.repository.EscalaRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("EscalaService — Testes Unitários")
class EscalaServiceTest {

    @Mock EscalaRepository escalaRepo;
    @Mock EscalaMapper mapper;

    @InjectMocks EscalaService service;

    private Escala escala;
    private EscalaDTO dto;

    @BeforeEach
    void setUp() {
        escala = new Escala();
        escala.setId(1L);
        escala.setDataEscala(LocalDate.of(2025, 6, 21));
        escala.setHoraInicio(LocalTime.of(18, 0));
        escala.setHoraFim(LocalTime.of(23, 0));
        escala.setStatusEscala("Agendada");

        dto = new EscalaDTO();
        dto.setId(1L);
        dto.setDataEscala(LocalDate.of(2025, 6, 21));
        dto.setHoraInicio(LocalTime.of(18, 0));
        dto.setHoraFim(LocalTime.of(23, 0));
        dto.setStatusEscala("Agendada");
    }

    // ── listar ─────────────────────────────────────

    @Test
    @DisplayName("listar() sem datas retorna todas as escalas via findAll")
    void listar_semDatas_retornaTodasViaFindAll() {
        when(escalaRepo.findAll()).thenReturn(List.of(escala));
        when(mapper.toDTO(escala)).thenReturn(dto);

        List<EscalaDTO> result = service.listar(null, null);

        assertThat(result).hasSize(1).contains(dto);
        verify(escalaRepo).findAll();
        verify(escalaRepo, never()).findByDataEscalaBetween(any(), any());
    }

    @Test
    @DisplayName("listar() com datas usa findByDataEscalaBetween")
    void listar_comDatas_usaFiltroDeData() {
        LocalDate inicio = LocalDate.of(2025, 6, 1);
        LocalDate fim    = LocalDate.of(2025, 6, 30);
        when(escalaRepo.findByDataEscalaBetween(inicio, fim)).thenReturn(List.of(escala));
        when(mapper.toDTO(escala)).thenReturn(dto);

        List<EscalaDTO> result = service.listar(inicio, fim);

        assertThat(result).hasSize(1);
        verify(escalaRepo).findByDataEscalaBetween(inicio, fim);
        verify(escalaRepo, never()).findAll();
    }

    @Test
    @DisplayName("listar() com apenas dataInicio nula cai no findAll")
    void listar_dataInicioNula_usaFindAll() {
        when(escalaRepo.findAll()).thenReturn(List.of());

        service.listar(null, LocalDate.now());

        verify(escalaRepo).findAll();
    }

    // ── buscarPorId ────────────────────────────────

    @Test
    @DisplayName("buscarPorId() retorna DTO quando encontrado")
    void buscarPorId_encontrado_retornaDTO() {
        when(escalaRepo.findById(1L)).thenReturn(Optional.of(escala));
        when(mapper.toDTO(escala)).thenReturn(dto);

        Optional<EscalaDTO> result = service.buscarPorId(1L);

        assertThat(result).isPresent().contains(dto);
    }

    @Test
    @DisplayName("buscarPorId() retorna vazio quando não encontrado")
    void buscarPorId_naoEncontrado_retornaVazio() {
        when(escalaRepo.findById(99L)).thenReturn(Optional.empty());

        assertThat(service.buscarPorId(99L)).isEmpty();
    }

    // ── criar ──────────────────────────────────────

    @Test
    @DisplayName("criar() converte DTO para entity, salva e retorna DTO")
    void criar_dadosValidos_salvaSucesso() {
        when(mapper.toEntity(dto)).thenReturn(escala);
        when(escalaRepo.save(escala)).thenReturn(escala);
        when(mapper.toDTO(escala)).thenReturn(dto);

        EscalaDTO result = service.criar(dto);

        assertThat(result).isEqualTo(dto);
        verify(escalaRepo).save(escala);
    }

    // ── atualizar ──────────────────────────────────

    @Test
    @DisplayName("atualizar() encontra, atualiza e salva")
    void atualizar_encontrado_atualizaSucesso() {
        when(escalaRepo.findById(1L)).thenReturn(Optional.of(escala));
        when(escalaRepo.save(escala)).thenReturn(escala);
        when(mapper.toDTO(escala)).thenReturn(dto);

        EscalaDTO result = service.atualizar(1L, dto);

        assertThat(result).isEqualTo(dto);
        verify(mapper).atualizarEntidade(escala, dto);
        verify(escalaRepo).save(escala);
    }

    @Test
    @DisplayName("atualizar() lança exceção quando não encontrada")
    void atualizar_naoEncontrada_lancaExcecao() {
        when(escalaRepo.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.atualizar(99L, dto))
                .isInstanceOf(RecursoNaoEncontradoException.class)
                .hasMessageContaining("99");
    }

    // ── excluir ────────────────────────────────────

    @Test
    @DisplayName("excluir() chama deleteById quando ID existe")
    void excluir_encontrada_deleta() {
        when(escalaRepo.existsById(1L)).thenReturn(true);

        service.excluir(1L);

        verify(escalaRepo).deleteById(1L);
    }

    @Test
    @DisplayName("excluir() lança exceção quando ID não existe")
    void excluir_naoEncontrada_lancaExcecao() {
        when(escalaRepo.existsById(99L)).thenReturn(false);

        assertThatThrownBy(() -> service.excluir(99L))
                .isInstanceOf(RecursoNaoEncontradoException.class)
                .hasMessageContaining("99");

        verify(escalaRepo, never()).deleteById(any());
    }
}
