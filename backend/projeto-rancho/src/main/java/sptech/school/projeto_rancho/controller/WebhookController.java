package sptech.school.projeto_rancho.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sptech.school.projeto_rancho.model.CarregarWebhook;
import sptech.school.projeto_rancho.service.WhatsAppService;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/webhook")
public class WebhookController {

    private final WhatsAppService whatsAppService;

    public WebhookController(WhatsAppService whatsAppService) {
        this.whatsAppService = whatsAppService;
    }

    @PostMapping
    public ResponseEntity<String> receiveMessage(
            @RequestParam(value = "From", required = false) String from,
            @RequestParam(value = "Body", required = false) String body,
            @RequestParam(value = "MessageStatus", required = false) String status) {

        // ignora notificações de status (sent, delivered, read)
        if (body == null || from == null) {
            return ResponseEntity.ok().build();
        }

        String numero = from.replace("whatsapp:", "");
        whatsAppService.processarMensagemTwilio(numero, body);
        return ResponseEntity.ok().build();
    }
}
