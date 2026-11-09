package br.com.snap.gerenciamento_custos.funcionario;

import org.springframework.http.HttpStatus;

public class ErroFuncionario extends RuntimeException {
    private final HttpStatus status;

    public ErroFuncionario(HttpStatus status, String mensagem) {
        super(mensagem);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
