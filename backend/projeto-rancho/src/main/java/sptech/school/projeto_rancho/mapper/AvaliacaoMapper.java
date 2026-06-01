package sptech.school.projeto_rancho.mapper;

import sptech.school.projeto_rancho.dto.AvaliacaoDTO;
import sptech.school.projeto_rancho.model.Avaliacao;
import sptech.school.projeto_rancho.model.Freelancer;
import sptech.school.projeto_rancho.model.Usuario;
import org.springframework.stereotype.Component;

@Component
public class AvaliacaoMapper {

    public AvaliacaoDTO toDTO(Avaliacao a) {
        if (a == null) return null;
        AvaliacaoDTO dto = new AvaliacaoDTO();
        dto.setId(a.getId());
        dto.setNota(a.getNota());
        dto.setDescricao(a.getDescricao());
        if (a.getFreelancer() != null) {
            dto.setFreelancerId(a.getFreelancer().getId());
            dto.setFreelancerNome(a.getFreelancer().getNome());
        }
        if (a.getUsuarioSistema() != null) {
            dto.setUsuarioSistemaId(a.getUsuarioSistema().getId().longValue());
            dto.setUsuarioSistemaNome(a.getUsuarioSistema().getNome());
        }
        return dto;
    }

    public Avaliacao toEntity(AvaliacaoDTO dto, Freelancer freelancer, Usuario usuario) {
        if (dto == null) return null;
        Avaliacao a = new Avaliacao();
        a.setFreelancer(freelancer);
        a.setUsuarioSistema(usuario);
        a.setNota(dto.getNota());
        a.setDescricao(dto.getDescricao());
        return a;
    }
}
