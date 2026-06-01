package sptech.school.projeto_rancho.mapper;

import sptech.school.projeto_rancho.dto.SetorDTO;
import sptech.school.projeto_rancho.model.Setor;
import org.springframework.stereotype.Component;

@Component
public class SetorMapper {

    public SetorDTO toDTO(Setor s) {
        if (s == null) return null;
        SetorDTO dto = new SetorDTO();
        dto.setId(s.getId());
        dto.setNome(s.getNome());
        return dto;
    }

    public Setor toEntity(SetorDTO dto) {
        if (dto == null) return null;
        Setor s = new Setor();
        s.setNome(dto.getNome());
        return s;
    }

    public void atualizarEntidade(Setor s, SetorDTO dto) {
        s.setNome(dto.getNome());
    }
}
