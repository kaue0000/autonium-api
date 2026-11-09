package br.com.snap.gerenciamento_custos.administrador;

import jakarta.validation.constraints.NotBlank;

public record TokenAutenticacaoRequest(
        @NotBlank(message = "O token do Firebase é obrigatório.")
        String tokenId
) {
}
