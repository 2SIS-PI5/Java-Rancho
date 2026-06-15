package sptech.school.projeto_rancho.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CarregarWebhook {

    @JsonProperty("entry")
    private List<EntradaWebhook> entrada;

    public List<EntradaWebhook> getEntrada() {
        return entrada;
    }

    public void setEntrada(List<EntradaWebhook> entrada) {
        this.entrada = entrada;
    }
}
