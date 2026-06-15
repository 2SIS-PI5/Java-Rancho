package sptech.school.projeto_rancho.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ValorWebhook {

    @JsonProperty("messages")
    private List<MensagemWebhook> mensagens;

    public List<MensagemWebhook> getMensagens() {
        return mensagens;
    }

    public void setMensagens(List<MensagemWebhook> mensagens) {
        this.mensagens = mensagens;
    }
}
