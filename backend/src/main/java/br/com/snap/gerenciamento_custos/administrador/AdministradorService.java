package br.com.snap.gerenciamento_custos.administrador;

import br.com.snap.gerenciamento_custos.autenticacao.ErroAutenticacao;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AdministradorService {

    @Autowired
    private AdministradorRepository administradorRepository;

    private final ObjectProvider<FirebaseAuth> provedorAutenticacaoFirebase;

    public AdministradorService(ObjectProvider<FirebaseAuth> provedorAutenticacaoFirebase) {
        this.provedorAutenticacaoFirebase = provedorAutenticacaoFirebase;
    }

    @Transactional
    public Administrador register(String tokenId) {
        FirebaseToken tokenFirebase = verificarToken(tokenId);
        if (administradorRepository.count() > 0) {
            throw new ErroAutenticacao(HttpStatus.CONFLICT, "O administrador já foi cadastrado.");
        }

        Administrador administrador = new Administrador();
        administrador.setFirebaseUid(tokenFirebase.getUid());
        administrador.setEmail(tokenFirebase.getEmail());
        return administradorRepository.save(administrador);
    }

    @Transactional(readOnly = true)
    public Administrador login(String tokenId) {
        FirebaseToken tokenFirebase = verificarToken(tokenId);
        return administradorRepository.findByFirebaseUid(tokenFirebase.getUid())
                .orElseThrow(() -> new ErroAutenticacao(
                        HttpStatus.UNAUTHORIZED,
                        "Esta conta não possui acesso ao sistema."
                ));
    }

    public void logout(String tokenId) {
        FirebaseToken tokenFirebase = verificarToken(tokenId);
        administradorRepository.findByFirebaseUid(tokenFirebase.getUid())
                .orElseThrow(() -> new ErroAutenticacao(
                        HttpStatus.UNAUTHORIZED,
                        "Esta conta não possui acesso ao sistema."
                ));
        try {
            obterFirebaseAuth().revokeRefreshTokens(tokenFirebase.getUid());
        } catch (FirebaseAuthException excecao) {
            throw new ErroAutenticacao(HttpStatus.BAD_GATEWAY, "Não foi possível encerrar a sessão no Firebase.");
        }
    }

    @Transactional(readOnly = true)
    public AdministradorResponse get(Integer id) {
        Administrador administrador = administradorRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Administrador não encontrado."));
        return AdministradorResponse.from(administrador);
    }

    @Transactional(readOnly = true)
    public List<AdministradorResponse> getAll() {
        return administradorRepository.findAll().stream()
                .map(AdministradorResponse::from)
                .toList();
    }

    private FirebaseToken verificarToken(String tokenId) {
        try {
            return obterFirebaseAuth().verifyIdToken(tokenId, true);
        } catch (FirebaseAuthException | IllegalArgumentException excecao) {
            throw new ErroAutenticacao(HttpStatus.UNAUTHORIZED, "Credenciais inválidas ou sessão encerrada.");
        }
    }

    private FirebaseAuth obterFirebaseAuth() {
        FirebaseAuth autenticacaoFirebase = provedorAutenticacaoFirebase.getIfAvailable();
        if (autenticacaoFirebase == null) {
            throw new ErroAutenticacao(
                    HttpStatus.SERVICE_UNAVAILABLE,
                    "Firebase Auth não está configurado no servidor."
            );
        }
        return autenticacaoFirebase;
    }
}
