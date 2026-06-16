package sptech.school.projeto_rancho.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
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

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("EscalaFuncionarioService — Testes Unitários")
class EscalaFuncionarioServiceTest {

    @Mock EscalaFuncionarioRepository repo;
    @Mock EscalaRepository escalaRepo;
    @Mock FreelancerRepository freelancerRepo;
    @Mock SetorRepository setorRepo;
    @Mock EscalaFuncionarioMapper mapper;

    @InjectMocks EscalaFuncionarioService service;

    private Escala escala;
    private Freelancer freelancer;
    private Setor setor;
    private EscalaFuncionario ef;
    private EscalaFuncionarioDTO dto;

    @BeforeEach
    void setUp() {
        escala = new Escala();
        escala.setId(1L);

        freelancer = new Freelancer();
        freelancer.setId(10L);
        freelancer.setNome("João");

        setor = new Setor();
        setor.setId(2);
        setor.setNome("Cozinha");

        ef = new EscalaFuncionario();
        ef.setId(100);
        ef.setEscala(escala);
        ef.setFreelancer(freelancer);
        ef.setSetor(setor);

        dto = new EscalaFuncionarioDTO();
        dto.setId(100);
        dto.setEscalaId(1L);
        dto.setFreelancerId(10L);
        dto.setSetorId(2);
        dto.setFreelancerNome("João");
        dto.setSetorNome("Cozinha");
    }

    // ── listar ─────────────────────────────────────

    @Test
    @DisplayName("listar() sem filtros chama findByEscalaId")
    void listar_semFiltros_usaFindByEscalaId() {
        when(repo.findByEscalaId(1L)).thenReturn(List.of(ef));
        when(mapper.toDTO(ef)).thenReturn(dto);

        List<EscalaFuncionarioDTO> result = service.listar(1L, null, null);

        assertThat(result).hasSize(1).contains(dto);
        verify(repo).findByEscalaId(1L);
    }

    @Test
    @DisplayName("listar() com compareceu filtra pela flag")
    void listar_comCompareceu_filtra() {
        ef.setCompareceu(true);
        when(repo.findByEscalaIdAndCompareceu(1L, true)).thenReturn(List.of(ef));
        when(mapper.toDTO(ef)).thenReturn(dto);

        List<EscalaFuncionarioDTO> result = service.listar(1L, true, null);

        assertThat(result).hasSize(1);
        verify(repo).findByEscalaIdAndCompareceu(1L, true);
    }

    @Test
    @DisplayName("listar() com setorId filtra por setor")
    void listar_comSetorId_filtrarPorSetor() {
        when(repo.findByEscalaIdAndSetorId(1L, 2)).thenReturn(List.of(ef));
        when(mapper.toDTO(ef)).thenReturn(dto);

        List<EscalaFuncionarioDTO> result = service.listar(1L, null, 2);

        assertThat(result).hasSize(1);
        verify(repo).findByEscalaIdAndSetorId(1L, 2);
    }

    @Test
    @DisplayName("listar() com compareceu e setorId aplica ambos os filtros")
    void listar_comAmbos_aplicaFiltroCompareceuNoStream() {
        ef.setCompareceu(true);
        when(repo.findByEscalaIdAndSetorId(1L, 2)).thenReturn(List.of(ef));
        when(mapper.toDTO(ef)).thenReturn(dto);

        List<EscalaFuncionarioDTO> result = service.listar(1L, true, 2);

        assertThat(result).hasSize(1);
    }

    @Test
    @DisplayName("listar() com ambos filtros exclui registro com compareceu false")
    void listar_comAmbos_exclueNaoCompareceu() {
        ef.setCompareceu(false);
        when(repo.findByEscalaIdAndSetorId(1L, 2)).thenReturn(List.of(ef));

        List<EscalaFuncionarioDTO> result = service.listar(1L, true, 2);

        assertThat(result).isEmpty();
    }

    // ── adicionar ──────────────────────────────────

    @Test
    @DisplayName("adicionar() cria vínculo com escala, freelancer e setor")
    void adicionar_dadosCompletos_criaSucesso() {
        when(escalaRepo.findById(1L)).thenReturn(Optional.of(escala));
        when(freelancerRepo.findById(10L)).thenReturn(Optional.of(freelancer));
        when(setorRepo.findById(2)).thenReturn(Optional.of(setor));
        when(mapper.toEntity(dto, escala, freelancer, setor)).thenReturn(ef);
        when(repo.save(ef)).thenReturn(ef);
        when(mapper.toDTO(ef)).thenReturn(dto);

        EscalaFuncionarioDTO result = service.adicionar(1L, dto);

        assertThat(result).isEqualTo(dto);
        verify(repo).save(ef);
    }

    @Test
    @DisplayName("adicionar() lança exceção quando escala não existe")
    void adicionar_escalaNaoEncontrada_lancaExcecao() {
        when(escalaRepo.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.adicionar(99L, dto))
                .isInstanceOf(RecursoNaoEncontradoException.class)
                .hasMessageContaining("99");
    }

    @Test
    @DisplayName("adicionar() lança exceção quando freelancer não existe")
    void adicionar_freelancerNaoEncontrado_lancaExcecao() {
        when(escalaRepo.findById(1L)).thenReturn(Optional.of(escala));
        when(freelancerRepo.findById(10L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.adicionar(1L, dto))
                .isInstanceOf(RecursoNaoEncontradoException.class)
                .hasMessageContaining("10");
    }

    @Test
    @DisplayName("adicionar() lança exceção quando setor não existe")
    void adicionar_setorNaoEncontrado_lancaExcecao() {
        when(escalaRepo.findById(1L)).thenReturn(Optional.of(escala));
        when(freelancerRepo.findById(10L)).thenReturn(Optional.of(freelancer));
        when(setorRepo.findById(2)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.adicionar(1L, dto))
                .isInstanceOf(RecursoNaoEncontradoException.class)
                .hasMessageContaining("2");
    }

    @Test
    @DisplayName("adicionar() sem setor cria vínculo sem setor")
    void adicionar_semSetor_salvaComSetorNull() {
        dto.setSetorId(null);
        when(escalaRepo.findById(1L)).thenReturn(Optional.of(escala));
        when(freelancerRepo.findById(10L)).thenReturn(Optional.of(freelancer));
        when(mapper.toEntity(dto, escala, freelancer, null)).thenReturn(ef);
        when(repo.save(ef)).thenReturn(ef);
        when(mapper.toDTO(ef)).thenReturn(dto);

        service.adicionar(1L, dto);

        verify(setorRepo, never()).findById(any());
        verify(repo).save(ef);
    }

    // ── remover ────────────────────────────────────

    @Test
    @DisplayName("remover() deleta vínculo quando pertence à escala correta")
    void remover_vinculoCorreto_deletaSucesso() {
        when(repo.findById(100)).thenReturn(Optional.of(ef));

        service.remover(1L, 100);

        verify(repo).delete(ef);
    }

    @Test
    @DisplayName("remover() lança exceção quando vínculo não encontrado")
    void remover_naoEncontrado_lancaExcecao() {
        when(repo.findById(999)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.remover(1L, 999))
                .isInstanceOf(RecursoNaoEncontradoException.class)
                .hasMessageContaining("999");
    }

    @Test
    @DisplayName("remover() lança IllegalArgumentException quando vínculo é de outra escala")
    void remover_vinculoDeOutraEscala_lancaIllegalArgument() {
        when(repo.findById(100)).thenReturn(Optional.of(ef)); // ef.escala.id = 1

        assertThatThrownBy(() -> service.remover(99L, 100)) // escalaId diferente
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("não pertence");
    }

    // ── contarComparecimento ───────────────────────

    @Test
    @DisplayName("contarComparecimento() conta apenas os que compareceram")
    void contarComparecimento_retornaContagem() {
        ef.setCompareceu(true);
        when(repo.findByEscalaIdAndCompareceu(1L, true)).thenReturn(List.of(ef, ef));

        Long total = service.contarComparecimento(1L);

        assertThat(total).isEqualTo(2L);
    }

    // ── contarSemanaPassada ────────────────────────

    @Test
    @DisplayName("contarSemanaPassada() conta freelancers distintos da semana passada")
    void contarSemanaPassada_retornaDistintos() {
        // Dois EscalaFuncionario com o mesmo freelancer → deve contar 1 distinto
        EscalaFuncionario ef2 = new EscalaFuncionario();
        ef2.setFreelancer(freelancer); // mesmo freelancer
        when(repo.findByPeriodo(any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(List.of(ef, ef2));

        Long total = service.contarSemanaPassada();

        assertThat(total).isEqualTo(1L);
    }

    // ── contarPorSetor ─────────────────────────────

    @Test
    @DisplayName("contarPorSetor() mapeia resultado bruto do repositório")
    @SuppressWarnings({"unchecked", "rawtypes"})
    void contarPorSetor_mapeiaResultado() {
        Object[] row = new Object[]{"Cozinha", 5L};
        List rawList = java.util.Collections.singletonList(row);
        when(repo.countFreelancersBySetor()).thenReturn(rawList);

        List<Map<String, Object>> result = service.contarPorSetor();

        assertThat(result).hasSize(1);
        assertThat(result.get(0)).containsEntry("setor", "Cozinha")
                                 .containsEntry("total", 5L);
    }

}

