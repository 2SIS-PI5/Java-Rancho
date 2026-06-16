package sptech.school.projeto_rancho.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayName("GlobalExceptionHandler — Testes Unitários")
class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    // ── RecursoNaoEncontradoException → 404 ───────

    @Test
    @DisplayName("handleNaoEncontrado() retorna 404 com a mensagem da exceção")
    void handleNaoEncontrado_retorna404() {
        RecursoNaoEncontradoException ex = new RecursoNaoEncontradoException("Freelancer não encontrado. ID: 99");

        ResponseEntity<Map<String, Object>> response = handler.handleNaoEncontrado(ex);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).containsEntry("status", 404)
                                      .containsEntry("message", "Freelancer não encontrado. ID: 99")
                                      .containsKey("timestamp");
    }

    // ── RuntimeException → 400 ────────────────────

    @Test
    @DisplayName("handleRuntime() retorna 400 com a mensagem da exceção")
    void handleRuntime_retorna400() {
        RuntimeException ex = new RuntimeException("CPF já cadastrado no sistema.");

        ResponseEntity<Map<String, Object>> response = handler.handleRuntime(ex);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).containsEntry("status", 400)
                                      .containsEntry("message", "CPF já cadastrado no sistema.");
    }

    // ── MethodArgumentNotValidException → 400 ─────

    @Test
    @DisplayName("handleValidacao() retorna 400 com mensagens de campo concatenadas")
    void handleValidacao_retorna400ComMensagens() {
        FieldError fieldError1 = new FieldError("freelancer", "nome", "não pode estar em branco");
        FieldError fieldError2 = new FieldError("freelancer", "email", "e-mail inválido");

        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.getFieldErrors()).thenReturn(List.of(fieldError1, fieldError2));

        MethodArgumentNotValidException ex = mock(MethodArgumentNotValidException.class);
        when(ex.getBindingResult()).thenReturn(bindingResult);

        ResponseEntity<Map<String, Object>> response = handler.handleValidacao(ex);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        String message = (String) response.getBody().get("message");
        assertThat(message).contains("nome").contains("email").contains("não pode estar em branco");
    }

    // ── Exception genérica → 500 ──────────────────

    @Test
    @DisplayName("handleGeral() retorna 500 com mensagem genérica")
    void handleGeral_retorna500() {
        Exception ex = new NullPointerException("NullPointer interno");

        ResponseEntity<Map<String, Object>> response = handler.handleGeral(ex);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(response.getBody()).containsEntry("status", 500);
        assertThat((String) response.getBody().get("message"))
                .contains("Erro interno");
    }

    // ── timestamp ─────────────────────────────────

    @Test
    @DisplayName("Todas as respostas contêm o campo timestamp")
    void todasRespostas_contemTimestamp() {
        RecursoNaoEncontradoException ex = new RecursoNaoEncontradoException("teste");

        ResponseEntity<Map<String, Object>> response = handler.handleNaoEncontrado(ex);

        assertThat(response.getBody()).containsKey("timestamp");
        assertThat(response.getBody().get("timestamp").toString()).isNotBlank();
    }
}
