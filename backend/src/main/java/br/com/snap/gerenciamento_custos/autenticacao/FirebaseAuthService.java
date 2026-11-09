package br.com.snap.gerenciamento_custos.autenticacao;

import br.com.snap.gerenciamento_custos.administrador.Administrador;
import br.com.snap.gerenciamento_custos.administrador.AdministradorService;
import org.springframework.stereotype.Service;

@Service
public class FirebaseAuthService {

    private final AdministradorService administradorService;

    public FirebaseAuthService(AdministradorService administradorService) {
        this.administradorService = administradorService;
    }

    public Administrador validarToken(String tokenId) {
        return administradorService.login(tokenId);
    }

    public void encerrarSessao(String tokenId) {
        administradorService.logout(tokenId);
    }
}
