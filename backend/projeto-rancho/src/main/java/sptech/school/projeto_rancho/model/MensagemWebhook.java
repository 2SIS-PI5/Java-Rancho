package sptech.school.projeto_rancho.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MensagemWebhook {

    @JsonProperty("from")
    private String numero;

    @JsonProperty("text")
    private TextoWebhook textoWebhook;

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public TextoWebhook getTextoWebhook() {
        return textoWebhook;
    }

    public void setTextoWebhook(TextoWebhook textoWebhook) {
        this.textoWebhook = textoWebhook;
    }
}
