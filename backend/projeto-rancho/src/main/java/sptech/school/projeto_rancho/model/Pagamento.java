package sptech.school.projeto_rancho.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "pagamentos")
public class Pagamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_funcionario", nullable = false)
    private Freelancer freelancer;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_escala_func", nullable = false)
    private EscalaFuncionario escalaFuncionario;

    @Column(name = "data_pagamento")
    private LocalDateTime dataPagamento = LocalDateTime.now();

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal valor;

    @Column(name = "forma_pagamento_utilizada", length = 10)
    private String formaPagamentoUtilizada;

    @Column(name = "status_pagamento", length = 20)
    private String statusPagamento = "Pendente";

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Freelancer getFreelancer() { return freelancer; }
    public void setFreelancer(Freelancer freelancer) { this.freelancer = freelancer; }

    public EscalaFuncionario getEscalaFuncionario() { return escalaFuncionario; }
    public void setEscalaFuncionario(EscalaFuncionario escalaFuncionario) { this.escalaFuncionario = escalaFuncionario; }

    public LocalDateTime getDataPagamento() { return dataPagamento; }
    public void setDataPagamento(LocalDateTime dataPagamento) { this.dataPagamento = dataPagamento; }

    public BigDecimal getValor() { return valor; }
    public void setValor(BigDecimal valor) { this.valor = valor; }

    public String getFormaPagamentoUtilizada() { return formaPagamentoUtilizada; }
    public void setFormaPagamentoUtilizada(String formaPagamentoUtilizada) { this.formaPagamentoUtilizada = formaPagamentoUtilizada; }

    public String getStatusPagamento() { return statusPagamento; }
    public void setStatusPagamento(String statusPagamento) { this.statusPagamento = statusPagamento; }
}
