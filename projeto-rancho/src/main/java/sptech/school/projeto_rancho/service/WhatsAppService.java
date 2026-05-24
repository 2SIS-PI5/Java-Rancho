package sptech.school.projeto_rancho.service;

import org.springframework.stereotype.Service;
import sptech.school.projeto_rancho.client.WhatsAppClient;
import sptech.school.projeto_rancho.model.*;

@Service
public class WhatsAppService {

    private final WhatsAppClient whatsAppClient;

    public WhatsAppService(WhatsAppClient whatsAppClient) {
        this.whatsAppClient = whatsAppClient;
    }

    public void processarMensagemTwilio(String numero, String texto) {
        String resposta = gerarResposta(texto.toLowerCase());
        whatsAppClient.enviarMensagem(numero, resposta);
    }

    private String gerarResposta(String texto) {
        
        if (texto.equals("1")) {

            return "Comidas:\n" +
                    "- Porco no rolete\n" +
                    "- Churrasco\n" +
                    "- Variação de Queijos \n" +
                    "- Molhos variados \n" +
                    "- Churrasco\n" +
                    "- Saladas \n" +
                    "- Pratos Mineiros \n" +
                    "\n";
        } else if (texto.equals("2")) {

            return "Rua Antônio Vertamatti, 83 - Zanzala, Riacho Grande";
        } else if (texto.equals("3")) {

            return "Valores:\n" +
                    "- R$116,00 por pessoa\n" +
                    "- Crianças de até 6 anos não paga \n" +
                    "- Crianças de 7 a 11 anos paga R$58,00\n" +
                    "- Aniversariante e Bariátrica possui 30% de desconto \n";
        } else if (texto.equals("4")) {

            return "Atividades:\n" +
                    "- Passeio a cavalo \n" +
                    "- Tirolesa\n" +
                    "- Vara de Pesca\n" +
                    "- Galinheiro\n";
        } else if (texto.equals("5")) {

            return "- Contate o telefone: 11999999999, " +
                    "\n Informe seu nome, telefone, data da reserva, quantidade de pessoas e o local de sua preferência na casa.\n";

        } else if (texto.equals("6")) {

            return "Horários de funcionamento:\n" +
                    "- Espaço e atividades: das 09h às 17h \n" +
                    "- Restaurante (comida mineira/caipira): das 12h às 16h30 \n" +
                    "- Música ao vivo: das 13h às 17h \n";
        } else {
            return "Olá! Bem-vindo ao atendimento do Rancho do Comanche. " +
                    "\nComo posso te ajudar?\n\n1 - Cardápio de comidas \n2 - Endereço do restaurante \n3 - Valores \n4 - Atividades para o lazer \n5 - Como reservar uma mesa? \n6 - Horários de funcionamento";
        }
    }
}
