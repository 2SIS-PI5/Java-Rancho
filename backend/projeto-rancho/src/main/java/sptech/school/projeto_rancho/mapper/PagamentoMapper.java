package sptech.school.projeto_rancho.mapper;

import sptech.school.projeto_rancho.dto.PagamentoDTO;
import sptech.school.projeto_rancho.model.EscalaFuncionario;
import sptech.school.projeto_rancho.model.Freelancer;
import sptech.school.projeto_rancho.model.Pagamento;
import org.springframework.stereotype.Component;

@Component
public class PagamentoMapper {

    public PagamentoDTO toDTO(Pagamento p) {
        if (p == null) return null;
        PagamentoDTO dto = new PagamentoDTO();
        dto.setId(p.getId());
        dto.setValor(p.getValor());
        dto.setDataPagamento(p.getDataPagamento());
        dto.setFormaPagamentoUtilizada(p.getFormaPagamentoUtilizada());
        dto.setStatusPagamento(p.getStatusPagamento());

        if (p.getFreelancer() != null) {
            dto.setFreelancerId(p.getFreelancer().getId());
            dto.setFreelancerNome(p.getFreelancer().getNome());
            dto.setFreelancerPixChave(p.getFreelancer().getPixChave());
        }
        if (p.getEscalaFuncionario() != null) {
            dto.setEscalaFuncionarioId(p.getEscalaFuncionario().getId());
            if (p.getEscalaFuncionario().getEscala() != null) {
                dto.setEscalaData(p.getEscalaFuncionario().getEscala().getDataEscala());
            }
            if (p.getEscalaFuncionario().getSetor() != null) {
                dto.setSetorNome(p.getEscalaFuncionario().getSetor().getNome());
            }
        }
        return dto;
    }

    public Pagamento toEntity(PagamentoDTO dto,
                               Freelancer freelancer,
                               EscalaFuncionario escalaFuncionario) {
        if (dto == null) return null;
        Pagamento p = new Pagamento();
        p.setFreelancer(freelancer);
        p.setEscalaFuncionario(escalaFuncionario);
        p.setValor(dto.getValor());
        p.setFormaPagamentoUtilizada(dto.getFormaPagamentoUtilizada());
        p.setStatusPagamento(dto.getStatusPagamento() != null ? dto.getStatusPagamento() : "Pendente");
        return p;
    }
}
