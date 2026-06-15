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
            return "🍽️ *Cardápio do Rancho Comanche*\n\n" +
                    "Prepare o estômago para o melhor da culinária caipira! 🤠\n\n" +
                    "🐷 Porco no rolete\n" +
                    "🥩 Churrasco\n" +
                    "🧀 Variação de Queijos\n" +
                    "🫙 Molhos variados\n" +
                    "🥗 Saladas frescas\n" +
                    "🍲 Pratos Mineiros\n\n" +
                    "_Tudo feito com muito carinho e sabor do interior!_ 💚\n\n" +
                    "Digite 0️⃣ para voltar ao menu principal.";

        } else if (texto.equals("2")) {
            return "📍 *Onde nos encontrar?*\n\n" +
                    "Estamos te esperando aqui:\n\n" +
                    "🗺️ Rua Antônio Vertamatti, 83\n" +
                    "📌 Zanzala, Riacho Grande\n\n" +
                    "_Venha nos visitar e aproveite um dia inesquecível no campo!_ 🌿🐴\n\n" +
                    "Digite 0️⃣ para voltar ao menu principal.";

        } else if (texto.equals("3")) {
            return "💰 *Valores e Preços*\n\n" +
                    "Confira nossa tabela de preços:\n\n" +
                    "👤 Adulto: *R$ 116,00* por pessoa\n" +
                    "👶 Crianças até 6 anos: *Gratuito* \n" +
                    "🧒 Crianças de 7 a 11 anos: *R$ 58,00*\n" +
                    "🎂 Aniversariante: *30% de desconto*\n" +
                    "⚕️ Bariátrica: *30% de desconto*\n\n" +
                    "_Venha celebrar momentos especiais com a gente!_ 🥳\n\n" +
                    "Digite 0️⃣ para voltar ao menu principal.";

        } else if (texto.equals("4")) {
            return "🎉 *Atividades e Lazer*\n\n" +
                    "Aqui a diversão não para! Confira o que temos:\n\n" +
                    "🐴 Passeio a cavalo\n" +
                    "🪂 Tirolesa\n" +
                    "🎣 Vara de Pesca\n" +
                    "🐔 Visita ao Galinheiro\n\n" +
                    "_Uma experiência rural completa para toda a família!_ 👨‍👩‍👧‍👦\n\n" +
                    "Digite 0️⃣ para voltar ao menu principal.";

        } else if (texto.equals("5")) {
            return "📅 *Reserva de Mesa*\n\n" +
                    "Que ótima escolha! Para garantir seu lugar, entre em contato pelo telefone:\n\n" +
                    "📞 *(11) 99999-9999*\n\n" +
                    "Ao ligar, tenha em mãos as seguintes informações:\n\n" +
                    "✏️ Nome completo\n" +
                    "📱 Telefone para contato\n" +
                    "📆 Data da reserva\n" +
                    "👥 Quantidade de pessoas\n" +
                    "🪑 Local de preferência na casa\n\n" +
                    "_Nossa equipe terá o prazer de te atender!_ 😊\n\n" +
                    "Digite 0️⃣ para voltar ao menu principal.";

        } else if (texto.equals("6")) {
            return "🕐 *Horários de Funcionamento*\n\n" +
                    "Planeje sua visita com antecedência:\n\n" +
                    "🌿 Espaço e atividades:\n" +
                    "    das *09h às 17h*\n\n" +
                    "🍽️ Restaurante (comida mineira/caipira):\n" +
                    "    das *12h às 16h30*\n\n" +
                    "🎵 Música ao vivo:\n" +
                    "    das *13h às 17h*\n\n" +
                    "_Te esperamos de braços abertos!_ 🤗\n\n" +
                    "Digite 0️⃣ para voltar ao menu principal.";

        } else if (texto.equals("0")) {
            return "🏡 *Menu Principal*\n\n" +
                    "Como posso te ajudar?\n\n" +
                    "1️⃣ - Cardápio de comidas\n" +
                    "2️⃣ - Endereço do restaurante\n" +
                    "3️⃣ - Valores\n" +
                    "4️⃣ - Atividades para o lazer\n" +
                    "5️⃣ - Como reservar uma mesa?\n" +
                    "6️⃣ - Horários de funcionamento\n\n" +
                    "_Digite o número da opção desejada!_ 😊";

        } else {
            return "🤠 *Olá! Bem-vindo ao Rancho Comanche!*\n\n" +
                    "Que bom ter você por aqui! Sou o assistente virtual do rancho e estou aqui pra te ajudar. 🌿\n\n" +
                    "Como posso te ajudar hoje?\n\n" +
                    "1️⃣ - Cardápio de comidas\n" +
                    "2️⃣ - Endereço do restaurante\n" +
                    "3️⃣ - Valores\n" +
                    "4️⃣ - Atividades para o lazer\n" +
                    "5️⃣ - Como reservar uma mesa?\n" +
                    "6️⃣ - Horários de funcionamento\n\n" +
                    "_Digite o número da opção desejada!_ 😊";
        }
    }
}