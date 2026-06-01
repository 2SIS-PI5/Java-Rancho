package sptech.school.projeto_rancho.dto;

import jakarta.validation.constraints.NotBlank;

public class SetorDTO {

    private Integer id;

    @NotBlank(message = "Nome do setor é obrigatório")
    private String nome;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
}
