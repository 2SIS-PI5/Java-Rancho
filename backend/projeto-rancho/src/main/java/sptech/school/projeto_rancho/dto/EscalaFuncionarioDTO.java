package sptech.school.projeto_rancho.dto;

import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public class EscalaFuncionarioDTO {

    private Integer id;

    @NotNull(message = "ID da escala é obrigatório")
    private Long escalaId;

    @NotNull(message = "ID do freelancer é obrigatório")
    private Long freelancerId;

    private String freelancerNome;
    private String freelancerCep;
    private Double freelancerDistancia;

    private Integer setorId;
    private String  setorNome;

    private BigDecimal valorTotal;

    private Boolean compareceu;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Long getEscalaId() { return escalaId; }
    public void setEscalaId(Long escalaId) { this.escalaId = escalaId; }

    public Long getFreelancerId() { return freelancerId; }
    public void setFreelancerId(Long freelancerId) { this.freelancerId = freelancerId; }

    public String getFreelancerNome() { return freelancerNome; }
    public void setFreelancerNome(String freelancerNome) { this.freelancerNome = freelancerNome; }

    public String getFreelancerCep() { return freelancerCep; }
    public void setFreelancerCep(String freelancerCep) { this.freelancerCep = freelancerCep; }

    public Double getFreelancerDistancia() { return freelancerDistancia; }
    public void setFreelancerDistancia(Double freelancerDistancia) { this.freelancerDistancia = freelancerDistancia; }

    public Integer getSetorId() { return setorId; }
    public void setSetorId(Integer setorId) { this.setorId = setorId; }

    public String getSetorNome() { return setorNome; }
    public void setSetorNome(String setorNome) { this.setorNome = setorNome; }

    public BigDecimal getValorTotal() { return valorTotal; }
    public void setValorTotal(BigDecimal valorTotal) { this.valorTotal = valorTotal; }

    public Boolean getCompareceu() { return compareceu; }
    public void setCompareceu(Boolean compareceu) { this.compareceu = compareceu; }
}
