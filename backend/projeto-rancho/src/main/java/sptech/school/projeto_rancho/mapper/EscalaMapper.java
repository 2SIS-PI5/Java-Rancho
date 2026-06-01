package sptech.school.projeto_rancho.mapper;

import sptech.school.projeto_rancho.dto.EscalaDTO;
import sptech.school.projeto_rancho.model.Escala;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class EscalaMapper {

    public EscalaDTO toDTO(Escala e) {
        if (e == null) return null;

        EscalaDTO dto = new EscalaDTO();
        dto.setId(e.getId());
        dto.setDataEscala(e.getDataEscala());
        dto.setHoraInicio(e.getHoraInicio());
        dto.setHoraFim(e.getHoraFim());
        dto.setStatusEscala(e.getStatusEscala());
        dto.setObservacoes(e.getObservacoes());
        dto.setCriadoEm(e.getCriadoEm());
        return dto;
    }

    public Escala toEntity(EscalaDTO dto) {
        if (dto == null) return null;

        Escala e = new Escala();
        e.setDataEscala(dto.getDataEscala());
        e.setHoraInicio(dto.getHoraInicio());
        e.setHoraFim(dto.getHoraFim());
        e.setStatusEscala(dto.getStatusEscala() != null ? dto.getStatusEscala() : "Agendada");
        e.setObservacoes(dto.getObservacoes());
        e.setCriadoEm(LocalDateTime.now());
        return e;
    }

    public void atualizarEntidade(Escala e, EscalaDTO dto) {
        e.setDataEscala(dto.getDataEscala());
        e.setHoraInicio(dto.getHoraInicio());
        e.setHoraFim(dto.getHoraFim());
        if (dto.getStatusEscala() != null) e.setStatusEscala(dto.getStatusEscala());
        e.setObservacoes(dto.getObservacoes());
    }
}
