package sptech.school.projeto_rancho.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Avaliação de um freelancer")
public class AvaliacaoDTO {

    @Schema(description = "ID da avaliação", accessMode = Schema.AccessMode.READ_ONLY)
    private Integer id;

    @Schema(description = "ID do freelancer avaliado", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "ID do freelancer é obrigatório")
    private Long freelancerId;

    @Schema(description = "Nome do freelancer", accessMode = Schema.AccessMode.READ_ONLY)
    private String freelancerNome;

    @Schema(description = "ID do usuário que realizou a avaliação (opcional)", example = "2")
    private Long usuarioSistemaId;

    @Schema(description = "Nome do usuário que realizou a avaliação", accessMode = Schema.AccessMode.READ_ONLY)
    private String usuarioSistemaNome;

    @Schema(description = "Nota de 0 a 10", example = "8.5")
    private Float nota;

    @Schema(description = "Comentário sobre o freelancer", example = "Ótimo profissional, pontual e dedicado.")
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
