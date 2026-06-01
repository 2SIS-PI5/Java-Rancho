package sptech.school.projeto_rancho.model;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "escala_funcionarios")
public class EscalaFuncionario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_escala")
    private Escala escala;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_funcionario")
    private Freelancer freelancer;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_setor")
    private Setor setor;

    @Column(name = "valor_total", precision = 10, scale = 2)
    private BigDecimal valorTotal;

    @Column(name = "compareceu")
    private Boolean compareceu = null;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Escala getEscala() { return escala; }
    public void setEscala(Escala escala) { this.escala = escala; }

    public Freelancer getFreelancer() { return freelancer; }
    public void setFreelancer(Freelancer freelancer) { this.freelancer = freelancer; }

    public Setor getSetor() { return setor; }
    public void setSetor(Setor setor) { this.setor = setor; }

    public BigDecimal getValorTotal() { return valorTotal; }
    public void setValorTotal(BigDecimal valorTotal) { this.valorTotal = valorTotal; }

    public Boolean getCompareceu() { return compareceu; }
    public void setCompareceu(Boolean compareceu) { this.compareceu = compareceu; }
}
