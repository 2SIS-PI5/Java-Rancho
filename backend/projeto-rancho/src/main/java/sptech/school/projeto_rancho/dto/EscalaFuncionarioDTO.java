package sptech.school.projeto_rancho.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

@Schema(description = "Vínculo de um freelancer a uma escala")
public class EscalaFuncionarioDTO {

    @Schema(description = "ID do vínculo", accessMode = Schema.AccessMode.READ_ONLY)
    private Integer id;

    @Schema(description = "ID da escala", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "ID da escala é obrigatório")
    private Long escalaId;

    @Schema(description = "ID do freelancer", example = "5", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "ID do freelancer é obrigatório")
    private Long freelancerId;

    @Schema(description = "Nome do freelancer", accessMode = Schema.AccessMode.READ_ONLY)
    private String freelancerNome;

    @Schema(description = "CEP do freelancer", accessMode = Schema.AccessMode.READ_ONLY)
    private String freelancerCep;

    @Schema(description = "Distância do freelancer até o rancho (km)", accessMode = Schema.AccessMode.READ_ONLY)
    private Double freelancerDistancia;

    @Schema(description = "ID do setor atribuído ao freelancer nesta escala", example = "2")
    private Integer setorId;

    @Schema(description = "Nome do setor", accessMode = Schema.AccessMode.READ_ONLY)
    private String setorNome;

    @Schema(description = "Valor total calculado para o freelancer nesta escala", accessMode = Schema.AccessMode.READ_ONLY)
    private BigDecimal valorTotal;

    @Schema(description = "Indica se o freelancer compareceu à escala", example = "true")
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
