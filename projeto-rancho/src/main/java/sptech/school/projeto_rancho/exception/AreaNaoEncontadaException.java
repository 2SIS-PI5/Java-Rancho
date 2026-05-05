package sptech.school.projeto_rancho.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class AreaNaoEncontadaException extends RuntimeException {
    public AreaNaoEncontadaException() {
        super("Area não encontrada");
    }

}