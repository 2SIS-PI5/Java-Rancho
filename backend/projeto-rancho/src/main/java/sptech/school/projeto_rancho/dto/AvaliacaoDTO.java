package sptech.school.projeto_rancho.dto;

import jakarta.validation.constraints.NotNull;

public class AvaliacaoDTO {

    private Integer id;

    @NotNull(message = "ID do freelancer é obrigatório")
    private Long freelancerId;

    private String freelancerNome;

    private Long usuarioSistemaId;
    private String usuarioSistemaNome;

    private Float nota;

    private String descricao;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Long getFreelancerId() { return freelancerId; }
    public void setFreelancerId(Long freelancerId) { this.freelancerId = freelancerId; }

    public String getFreelancerNome() { return freelancerNome; }
    public void setFreelancerNome(String freelancerNome) { this.freelancerNome = freelancerNome; }

    public Long getUsuarioSistemaId() { return usuarioSistemaId; }
    public void setUsuarioSistemaId(Long usuarioSistemaId) { this.usuarioSistemaId = usuarioSistemaId; }

    public String getUsuarioSistemaNome() { return usuarioSistemaNome; }
    public void setUsuarioSistemaNome(String usuarioSistemaNome) { this.usuarioSistemaNome = usuarioSistemaNome; }

    public Float getNota() { return nota; }
    public void setNota(Float nota) { this.nota = nota; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
}
