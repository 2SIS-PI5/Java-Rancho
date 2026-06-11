package sptech.school.projeto_rancho.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Setor de trabalho")
public class SetorDTO {

    @Schema(description = "ID do setor", accessMode = Schema.AccessMode.READ_ONLY)
    private Integer id;

    @Schema(description = "Nome do setor", example = "Cozinha", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "Nome do setor é obrigatório")
    private String nome;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
}
