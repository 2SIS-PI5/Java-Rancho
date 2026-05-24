package sptech.school.projeto_rancho.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AlteracaoWebhook {

    @JsonProperty("value")
    private ValorWebhook valor;

    public ValorWebhook getValor() {
        return valor;
    }

    public void setValor(ValorWebhook valor) {
        this.valor = valor;
    }
}
