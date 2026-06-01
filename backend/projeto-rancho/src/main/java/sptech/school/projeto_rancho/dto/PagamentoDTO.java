package sptech.school.projeto_rancho.dto;

import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalDate;

public class PagamentoDTO {

    private Integer id;

    @NotNull(message = "ID do freelancer é obrigatório")
    private Long freelancerId;

    private String freelancerNome;
    private String freelancerPixChave;

    @NotNull(message = "ID de escala_funcionario é obrigatório")
    private Integer escalaFuncionarioId;

    private LocalDate escalaData;
    private String    setorNome;

    private LocalDateTime dataPagamento;

    @NotNull(message = "Valor é obrigatório")
    private BigDecimal valor;

    private String formaPagamentoUtilizada;

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
