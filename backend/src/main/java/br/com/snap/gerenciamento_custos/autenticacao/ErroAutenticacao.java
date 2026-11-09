package br.com.snap.gerenciamento_custos.autenticacao;

import org.springframework.http.HttpStatus;

public class ErroAutenticacao extends RuntimeException {
    private final HttpStatus status;

    public ErroAutenticacao(HttpStatus status, String mensagem) {
        super(mensagem);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
