package sptech.school.projeto_rancho.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalDate;

@Schema(description = "Pagamento a um freelancer")
public class PagamentoDTO {

    @Schema(description = "ID do pagamento", accessMode = Schema.AccessMode.READ_ONLY)
    private Integer id;

    @Schema(description = "ID do freelancer", example = "5", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "ID do freelancer é obrigatório")
    private Long freelancerId;

    @Schema(description = "Nome do freelancer", accessMode = Schema.AccessMode.READ_ONLY)
    private String freelancerNome;

    @Schema(description = "Chave Pix do freelancer", accessMode = Schema.AccessMode.READ_ONLY)
    private String freelancerPixChave;

    @Schema(description = "ID do vínculo escala-funcionário", example = "10", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "ID de escala_funcionario é obrigatório")
    private Integer escalaFuncionarioId;

    @Schema(description = "Data da escala vinculada", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDate escalaData;

    @Schema(description = "Nome do setor da escala", accessMode = Schema.AccessMode.READ_ONLY)
    private String setorNome;

    @Schema(description = "Data/hora em que o pagamento foi efetuado", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime dataPagamento;

    @Schema(description = "Valor do pagamento", example = "150.00", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "Valor é obrigatório")
    private BigDecimal valor;

    @Schema(description = "Forma de pagamento utilizada", example = "Pix",
            allowableValues = {"Pix", "Dinheiro"})
    private String formaPagamentoUtilizada;

    @Schema(description = "Status do pagamento", example = "Pendente",
            allowableValues = {"Pendente", "Pago"})
    private String statusPagamento;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Long getFreelancerId() { return freelancerId; }
    public void setFreelancerId(Long freelancerId) { this.freelancerId = freelancerId; }

    public String getFreelancerNome() { return freelancerNome; }
    public void setFreelancerNome(String freelancerNome) { this.freelancerNome = freelancerNome; }

    public String getFreelancerPixChave() { return freelancerPixChave; }
    public void setFreelancerPixChave(String freelancerPixChave) { this.freelancerPixChave = freelancerPixChave; }

    public Integer getEscalaFuncionarioId() { return escalaFuncionarioId; }
    public void setEscalaFuncionarioId(Integer escalaFuncionarioId) { this.escalaFuncionarioId = escalaFuncionarioId; }

    public LocalDate getEscalaData() { return escalaData; }
    public void setEscalaData(LocalDate escalaData) { this.escalaData = escalaData; }

    public String getSetorNome() { return setorNome; }
    public void setSetorNome(String setorNome) { this.setorNome = setorNome; }

    public LocalDateTime getDataPagamento() { return dataPagamento; }
    public void setDataPagamento(LocalDateTime dataPagamento) { this.dataPagamento = dataPagamento; }

    public BigDecimal getValor() { return valor; }
    public void setValor(BigDecimal valor) { this.valor = valor; }

    public String getFormaPagamentoUtilizada() { return formaPagamentoUtilizada; }
    public void setFormaPagamentoUtilizada(String formaPagamentoUtilizada) { this.formaPagamentoUtilizada = formaPagamentoUtilizada; }

    public String getStatusPagamento() { return statusPagamento; }
    public void setStatusPagamento(String statusPagamento) { this.statusPagamento = statusPagamento; }
}
