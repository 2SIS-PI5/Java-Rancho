package sptech.school.projeto_rancho.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "funcionarios")
@Schema(description = "Entidade que representa um funcionário freelancer do sistema")
public class Funcionario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Identificador único do funcionário", example = "1")
    private Integer id;

    @Column(nullable = false, length = 100)
    @Schema(description = "Nome completo do funcionário", example = "João Silva Santos")
    private String nome;

    @Column(nullable = false, length = 2)
    @Schema(description = "DDD da região (2 dígitos)", example = "11")
    private String ddd;

    @Column(nullable = false, length = 11)
    @Schema(description = "Número de telefone sem DDD (8 ou 9 dígitos)", example = "987654321")
    private String numero;

    @Column(nullable = false, length = 8)
    @Schema(description = "CEP do endereço", example = "01310100")
    private String cep;

    @Column(name = "pix_chave", nullable = false, length = 250)
    @Schema(description = "Chave PIX para recebimento", example = "joao@email.com")
    private String pixChave;

    @ManyToOne
    @JoinColumn(name = "id_area")
    @Schema(description = "Área de atuação do funcionário")
    private Area area;

    @Column(name = "valor_diaria")
    @Schema(description = "Valor da diária em reais", example = "150.00")
    private Double valorDiaria;

    @Column(name = "custo_transporte")
    @Schema(description = "Se há custo de transporte", example = "true")
    private Boolean custoTransporte = false;

    @Column(nullable = false)
    @Schema(description = "Distância até o local de trabalho em km", example = "12.5")
    private Float distancia;

    @Column(name = "data_cadastro", nullable = false)
    @Schema(description = "Data de cadastro no sistema", example = "2026-01-10")
    private LocalDate dataCadastro;

    public Funcionario() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDdd() {
        return ddd;
    }

    public void setDdd(String ddd) {
        this.ddd = ddd;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
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

    public Area getArea() {
        return area;
    }

    public void setArea(Area area) {
        this.area = area;
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