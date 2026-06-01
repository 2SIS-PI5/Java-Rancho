package sptech.school.projeto_rancho.mapper;

import sptech.school.projeto_rancho.dto.EscalaFuncionarioDTO;
import sptech.school.projeto_rancho.model.EscalaFuncionario;
import sptech.school.projeto_rancho.model.Escala;
import sptech.school.projeto_rancho.model.Freelancer;
import sptech.school.projeto_rancho.model.Setor;
import org.springframework.stereotype.Component;

@Component
public class EscalaFuncionarioMapper {

    public EscalaFuncionarioDTO toDTO(EscalaFuncionario ef) {
        if (ef == null) return null;
        EscalaFuncionarioDTO dto = new EscalaFuncionarioDTO();
        dto.setId(ef.getId());
        dto.setCompareceu(ef.getCompareceu());
        if (ef.getEscala() != null)      dto.setEscalaId(ef.getEscala().getId());
        if (ef.getFreelancer() != null) {
            dto.setFreelancerId(ef.getFreelancer().getId());
            dto.setFreelancerNome(ef.getFreelancer().getNome());
            dto.setFreelancerCep(ef.getFreelancer().getCep());
            dto.setFreelancerDistancia(ef.getFreelancer().getDistanciaKm());
        }
        if (ef.getSetor() != null) {
            dto.setSetorId(ef.getSetor().getId());
            dto.setSetorNome(ef.getSetor().getNome());
        }
        dto.setValorTotal(ef.getValorTotal());
        return dto;
    }

    public EscalaFuncionario toEntity(EscalaFuncionarioDTO dto,
                                      Escala escala,
                                      Freelancer freelancer,
                                      Setor setor) {
        if (dto == null) return null;
        EscalaFuncionario ef = new EscalaFuncionario();
        ef.setEscala(escala);
        ef.setFreelancer(freelancer);
        ef.setSetor(setor);
        ef.setValorTotal(dto.getValorTotal());
        ef.setCompareceu(dto.getCompareceu());
        return ef;
    }
}
