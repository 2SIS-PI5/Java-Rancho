package sptech.school.projeto_rancho.dto.funcionario;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

@Schema(description = "Dados de requisição para atualização parcial de um funcionário freelancer - todos os campos são opcionais")
public class FuncionarioUpdateDTO {

    @Size(max = 100)
    @Schema(description = "Nome completo do funcionário", example = "João Silva Santos")
    private String nome;

    @Pattern(regexp = "^\\d+$", message = "Número deve conter apenas dígitos")
    @Size(min = 10, max = 11, message = "Número completo deve ter 10 ou 11 dígitos (com DDD)")
    @Schema(description = "Número de telefone completo (DDD + número, apenas dígitos)", example = "11987654321")
    private String numeroCompleto;

    @Size(min = 8, max = 8, message = "CEP deve ter 8 dígitos")
    @Schema(description = "CEP do endereço (8 dígitos sem hífen)", example = "01310100")
    private String cep;

    @Size(max = 250)
    @Schema(description = "Chave PIX para recebimento (email, CPF, CNPJ ou chave aleatória)", example = "joao@email.com")
    private String pixChave;

    @Schema(description = "Identificador da área de atuação do funcionário", example = "1")
    private Integer idArea;


    @Positive(message = "Valor da diária deve ser positivo")
    @Schema(description = "Valor da diária em reais", example = "200.00")
    private Double valorDiaria;

    @Schema(description = "Indica se há custo de transporte", example = "false")
    private Boolean custoTransporte;

    @Positive(message = "Distância deve ser positiva")
    @Schema(description = "Distância em quilômetros até o local de trabalho", example = "15.0")
    private Float distancia;

    @Schema(description = "Data de cadastro do funcionário")
    private LocalDate dataCadastro;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getNumeroCompleto() {
        return numeroCompleto;
    }

    public void setNumeroCompleto(String numeroCompleto) {
        this.numeroCompleto = numeroCompleto;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getPixChave() {
        return pixChave;
    }

    public void setPixChave(String pixChave) {
        this.pixChave = pixChave;
    }

    public Integer getIdArea() {
        return idArea;
    }

    public void setIdArea(Integer idArea) {
        this.idArea = idArea;
    }


    public Double getValorDiaria() {
        return valorDiaria;
    }

    public void setValorDiaria(Double valorDiaria) {
        this.valorDiaria = valorDiaria;
    }

    public Boolean getCustoTransporte() {
        return custoTransporte;
    }

    public void setCustoTransporte(Boolean custoTransporte) {
        this.custoTransporte = custoTransporte;
    }

    public Float getDistancia() {
        return distancia;
    }

    public void setDistancia(Float distancia) {
        this.distancia = distancia;
    }

    public LocalDate getDataCadastro() {
        return dataCadastro;
    }

    public void setDataCadastro(LocalDate dataCadastro) {
        this.dataCadastro = dataCadastro;
    }
}