package sptech.school.projeto_rancho.exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class FuncionarioNaoEncontradoException extends RuntimeException {

    public FuncionarioNaoEncontradoException() {
        super("Funcionario não encontrado");
    }
}