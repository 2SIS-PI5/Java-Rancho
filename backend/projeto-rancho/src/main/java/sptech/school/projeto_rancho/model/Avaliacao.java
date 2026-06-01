package sptech.school.projeto_rancho.model;

import jakarta.persistence.*;

@Entity
@Table(name = "avaliacoes")
public class Avaliacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_funcionario", nullable = false)
    private Freelancer freelancer;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_usuario_sistema")
    private Usuario usuarioSistema;

    private Float nota;

    @Column(length = 255)
    private String descricao;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Freelancer getFreelancer() { return freelancer; }
    public void setFreelancer(Freelancer freelancer) { this.freelancer = freelancer; }

    public Usuario getUsuarioSistema() { return usuarioSistema; }
    public void setUsuarioSistema(Usuario usuarioSistema) { this.usuarioSistema = usuarioSistema; }

    public Float getNota() { return nota; }
    public void setNota(Float nota) { this.nota = nota; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
}
