package br.com.snap.gerenciamento_custos.autenticacao;

public record RespostaAutenticacao(
        String uidFirebase,
        String email,
        String nome,
        String sobrenome
) {
}
