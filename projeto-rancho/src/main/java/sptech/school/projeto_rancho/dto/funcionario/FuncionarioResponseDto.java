package sptech.school.projeto_rancho.dto.funcionario;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;

@Schema(description = "Dados de resposta de um funcionário freelancer")
public class FuncionarioResponseDto {
    @Schema(description = "Nome completo do funcionário", example = "João Silva Santos")
    private String nome;
    
    @Schema(description = "Número de telefone completo (DDD + número)", example = "11987654321")
    private String numeroCompleto;
    
    @Schema(description = "CEP do endereço do funcionário", example = "01310100")
    private String cep;
    
    @Schema(description = "Chave PIX do funcionário (pode ser email, CPF, CNPJ ou chave aleatória)", example = "joao@email.com")
    private String pixChave;
    
    @Schema(description = "Nome da área de atuação do funcionário", example = "Limpeza")
    private String nomeArea;
    
    @Schema(description = "Valor da diária em reais", example = "150.00")
    private Double valorDiaria;
    
    @Schema(description = "Indica se há custo de transporte", example = "true")
    private Boolean custoTransporte;
    
    @Schema(description = "Distância em quilômetros até o local de trabalho", example = "12.5")
    private Float distancia;
    
    @Schema(description = "Data de cadastro do funcionário no sistema", example = "2026-01-10")
    private LocalDate dataCadastro;

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getNumeroCompleto() { return numeroCompleto; }
    public void setNumeroCompleto(String numeroCompleto) { this.numeroCompleto = numeroCompleto; }

    public String getCep() { return cep; }
    public void setCep(String cep) { this.cep = cep; }

    public String getPixChave() { return pixChave; }
    public void setPixChave(String pixChave) { this.pixChave = pixChave; }

    public String getNomeArea() { return nomeArea; }
    public void setNomeArea(String nomeArea) { this.nomeArea = nomeArea; }

    public Double getValorDiaria() { return valorDiaria; }
    public void setValorDiaria(Double valorDiaria) { this.valorDiaria = valorDiaria; }

    public Boolean getCustoTransporte() { return custoTransporte; }
    public void setCustoTransporte(Boolean custoTransporte) { this.custoTransporte = custoTransporte; }

    public Float getDistancia() { return distancia; }
    public void setDistancia(Float distancia) { this.distancia = distancia; }

    public LocalDate getDataCadastro() { return dataCadastro; }
    public void setDataCadastro(LocalDate dataCadastro) { this.dataCadastro = dataCadastro; }
}
