package sptech.school.projeto_rancho.mapper;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import sptech.school.projeto_rancho.dto.EscalaDTO;
import sptech.school.projeto_rancho.model.Escala;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.*;

@DisplayName("EscalaMapper — Testes Unitários")
class EscalaMapperTest {

    private final EscalaMapper mapper = new EscalaMapper();

    // ── toDTO ──────────────────────────────────────

    @Test
    @DisplayName("toDTO() mapeia todos os campos da entidade para o DTO")
    void toDTO_entidadeCompleta_mapeiaCorretamente() {
        Escala e = new Escala();
        e.setId(1L);
        e.setDataEscala(LocalDate.of(2025, 6, 21));
        e.setHoraInicio(LocalTime.of(18, 0));
        e.setHoraFim(LocalTime.of(23, 0));
        e.setStatusEscala("Agendada");
        e.setObservacoes("Festa de final de ano");
        e.setCriadoEm(LocalDateTime.of(2025, 6, 1, 10, 0));

        EscalaDTO dto = mapper.toDTO(e);

        assertThat(dto.getId()).isEqualTo(1L);
        assertThat(dto.getDataEscala()).isEqualTo(LocalDate.of(2025, 6, 21));
        assertThat(dto.getHoraInicio()).isEqualTo(LocalTime.of(18, 0));
        assertThat(dto.getHoraFim()).isEqualTo(LocalTime.of(23, 0));
        assertThat(dto.getStatusEscala()).isEqualTo("Agendada");
        assertThat(dto.getObservacoes()).isEqualTo("Festa de final de ano");
        assertThat(dto.getCriadoEm()).isEqualTo(LocalDateTime.of(2025, 6, 1, 10, 0));
    }

    @Test
    @DisplayName("toDTO() retorna null quando entidade é null")
    void toDTO_null_retornaNull() {
        assertThat(mapper.toDTO(null)).isNull();
    }

    // ── toEntity ───────────────────────────────────

    @Test
    @DisplayName("toEntity() mapeia campos do DTO para a entidade")
    void toEntity_dtoCompleto_mapeiaCorretamente() {
        EscalaDTO dto = new EscalaDTO();
        dto.setDataEscala(LocalDate.of(2025, 7, 10));
        dto.setHoraInicio(LocalTime.of(9, 0));
        dto.setHoraFim(LocalTime.of(17, 0));
        dto.setStatusEscala("Realizada");
        dto.setObservacoes("Turno da manhã");

        Escala e = mapper.toEntity(dto);

        assertThat(e.getDataEscala()).isEqualTo(LocalDate.of(2025, 7, 10));
        assertThat(e.getHoraInicio()).isEqualTo(LocalTime.of(9, 0));
        assertThat(e.getHoraFim()).isEqualTo(LocalTime.of(17, 0));
        assertThat(e.getStatusEscala()).isEqualTo("Realizada");
        assertThat(e.getObservacoes()).isEqualTo("Turno da manhã");
        assertThat(e.getCriadoEm()).isNotNull();
    }

    @Test
    @DisplayName("toEntity() usa 'Agendada' como status padrão quando não informado")
    void toEntity_semStatus_usaAgendadaPorPadrao() {
        EscalaDTO dto = new EscalaDTO();
        dto.setDataEscala(LocalDate.now());
        dto.setHoraInicio(LocalTime.of(10, 0));
        dto.setHoraFim(LocalTime.of(18, 0));
        dto.setStatusEscala(null);

        Escala e = mapper.toEntity(dto);

        assertThat(e.getStatusEscala()).isEqualTo("Agendada");
    }

    @Test
    @DisplayName("toEntity() retorna null quando DTO é null")
    void toEntity_null_retornaNull() {
        assertThat(mapper.toEntity(null)).isNull();
    }

    // ── atualizarEntidade ──────────────────────────

    @Test
    @DisplayName("atualizarEntidade() sobrescreve campos da entidade com dados do DTO")
    void atualizarEntidade_atualizaCampos() {
        Escala e = new Escala();
        e.setDataEscala(LocalDate.of(2025, 1, 1));
        e.setStatusEscala("Agendada");

        EscalaDTO dto = new EscalaDTO();
        dto.setDataEscala(LocalDate.of(2025, 6, 21));
        dto.setHoraInicio(LocalTime.of(18, 0));
        dto.setHoraFim(LocalTime.of(23, 0));
        dto.setStatusEscala("Realizada");
        dto.setObservacoes("Atualizado");

        mapper.atualizarEntidade(e, dto);

        assertThat(e.getDataEscala()).isEqualTo(LocalDate.of(2025, 6, 21));
        assertThat(e.getHoraInicio()).isEqualTo(LocalTime.of(18, 0));
        assertThat(e.getHoraFim()).isEqualTo(LocalTime.of(23, 0));
        assertThat(e.getStatusEscala()).isEqualTo("Realizada");
        assertThat(e.getObservacoes()).isEqualTo("Atualizado");
    }

    @Test
    @DisplayName("atualizarEntidade() não altera statusEscala quando DTO.status é null")
    void atualizarEntidade_statusNull_mantemAtual() {
        Escala e = new Escala();
        e.setStatusEscala("Agendada");
        e.setDataEscala(LocalDate.now());

        EscalaDTO dto = new EscalaDTO();
        dto.setDataEscala(LocalDate.now());
        dto.setStatusEscala(null);

        mapper.atualizarEntidade(e, dto);

        assertThat(e.getStatusEscala()).isEqualTo("Agendada");
    }
}
