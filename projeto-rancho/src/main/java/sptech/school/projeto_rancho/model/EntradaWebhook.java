package sptech.school.projeto_rancho.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class EntradaWebhook {

    @JsonProperty("changes")
    private List<AlteracaoWebhook> alteracoes;

    public List<AlteracaoWebhook> getAlteracoes() {
        return alteracoes;
    }

    public void setAlteracoes(List<AlteracaoWebhook> alteracoes) {
        this.alteracoes = alteracoes;
    }
}
