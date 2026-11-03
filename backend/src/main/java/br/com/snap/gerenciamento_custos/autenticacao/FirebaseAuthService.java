package br.com.snap.gerenciamento_custos.autenticacao;

import br.com.snap.gerenciamento_custos.funcionario.CadastroFuncionarioRequest;
import br.com.snap.gerenciamento_custos.funcionario.Funcionario;
import br.com.snap.gerenciamento_custos.funcionario.FuncionarioRepository;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import com.google.firebase.auth.UserRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Locale;

@Service
public class FirebaseAuthService {

	private static final Logger logger = LoggerFactory.getLogger(FirebaseAuthService.class);

	private final ObjectProvider<FirebaseAuth> provedorAutenticacaoFirebase;
	private final FuncionarioRepository funcionarioRepository;

	public FirebaseAuthService(
			ObjectProvider<FirebaseAuth> provedorAutenticacaoFirebase,
			FuncionarioRepository funcionarioRepository
	) {
		this.provedorAutenticacaoFirebase = provedorAutenticacaoFirebase;
		this.funcionarioRepository = funcionarioRepository;
	}

	@Transactional
	public RespostaAutenticacao cadastrarAdministrador(CadastroFuncionarioRequest cadastro) {
		validarMaioridade(cadastro.dataNascimento());
		if (funcionarioRepository.count() > 0) {
			throw new ErroAutenticacao(HttpStatus.CONFLICT, "O administrador já foi cadastrado.");
		}
		if (funcionarioRepository.existsByCpf(cadastro.cpf())) {
			throw new ErroAutenticacao(HttpStatus.CONFLICT, "O CPF informado já está cadastrado.");
		}
		if (funcionarioRepository.existsByEmail(cadastro.email().trim().toLowerCase(Locale.ROOT))) {
			throw new ErroAutenticacao(HttpStatus.CONFLICT, "O e-mail informado já está cadastrado.");
		}

		FirebaseAuth autenticacaoFirebase = obterFirebaseAuth();
		UserRecord usuarioFirebase;
		try {
			usuarioFirebase = autenticacaoFirebase.createUser(new UserRecord.CreateRequest()
					.setEmail(cadastro.email().trim().toLowerCase(Locale.ROOT))
					.setPassword(cadastro.senha())
					.setDisplayName(cadastro.nome().trim() + " " + cadastro.sobrenome().trim()));
		} catch (FirebaseAuthException excecao) {
			logger.warn("Falha ao criar a conta do administrador no Firebase.", excecao);
			throw new ErroAutenticacao(HttpStatus.BAD_GATEWAY, "Não foi possível criar a conta no Firebase.");
		}

		try {
			Funcionario funcionario = funcionarioRepository.saveAndFlush(
					new Funcionario(cadastro, usuarioFirebase.getUid()));
			return criarResposta(funcionario);
		} catch (RuntimeException falhaCadastroLocal) {
			removerUsuarioFirebase(autenticacaoFirebase, usuarioFirebase.getUid(), falhaCadastroLocal);
			throw falhaCadastroLocal;
		}
	}

	@Transactional
	public RespostaAutenticacao autenticar(String tokenId) {
		return criarResposta(validarToken(tokenId));
	}

	@Transactional
	public Funcionario validarToken(String tokenId) {
		FirebaseAuth autenticacaoFirebase = obterFirebaseAuth();
		FirebaseToken tokenFirebase;
		try {
			tokenFirebase = autenticacaoFirebase.verifyIdToken(tokenId, true);
		} catch (FirebaseAuthException | IllegalArgumentException excecao) {
			throw new ErroAutenticacao(HttpStatus.UNAUTHORIZED, "Credenciais inválidas ou sessão encerrada.");
		}

		Funcionario funcionario = funcionarioRepository.findByUidFirebase(tokenFirebase.getUid())
				.orElseThrow(() -> new ErroAutenticacao(
						HttpStatus.UNAUTHORIZED,
						"Esta conta não possui acesso ao sistema."
				));
		Instant agora = Instant.now();
		Instant ultimaAtividade = funcionario.getUltimaAtividade();
		if (ultimaAtividade != null && !ultimaAtividade.plus(Duration.ofMinutes(30)).isAfter(agora)) {
			revogarTokens(autenticacaoFirebase, funcionario.getUidFirebase());
			throw new ErroAutenticacao(HttpStatus.UNAUTHORIZED, "Sessão encerrada por inatividade. Faça login novamente.");
		}
		funcionarioRepository.atualizarUltimaAtividade(funcionario.getUidFirebase(), agora);
		return funcionario;
	}

	public void encerrarSessao(String uidFirebase) {
		revogarTokens(obterFirebaseAuth(), uidFirebase);
	}

	private void revogarTokens(FirebaseAuth autenticacaoFirebase, String uidFirebase) {
		try {
			autenticacaoFirebase.revokeRefreshTokens(uidFirebase);
		} catch (FirebaseAuthException excecao) {
			logger.error("Falha ao revogar os tokens do usuário no Firebase.", excecao);
			throw new ErroAutenticacao(HttpStatus.BAD_GATEWAY, "Não foi possível encerrar a sessão no Firebase.");
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

	private void validarMaioridade(LocalDate dataNascimento) {
		if (dataNascimento.plusYears(18).isAfter(LocalDate.now())) {
			throw new ErroAutenticacao(HttpStatus.BAD_REQUEST, "O administrador deve ter pelo menos 18 anos.");
		}
	}

	private RespostaAutenticacao criarResposta(Funcionario funcionario) {
		return new RespostaAutenticacao(
				funcionario.getUidFirebase(),
				funcionario.getEmail(),
				funcionario.getNome(),
				funcionario.getSobrenome()
		);
	}

	private void removerUsuarioFirebase(
			FirebaseAuth autenticacaoFirebase,
			String uidFirebase,
			RuntimeException falhaOriginal
	) {
		try {
			autenticacaoFirebase.deleteUser(uidFirebase);
		} catch (FirebaseAuthException falhaRemocao) {
			logger.error("Falha ao remover usuário Firebase após erro ao salvar o funcionário.", falhaRemocao);
			falhaOriginal.addSuppressed(falhaRemocao);
		}
	}
}
