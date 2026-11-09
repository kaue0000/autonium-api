package br.com.snap.gerenciamento_custos.autenticacao;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Configuration
@ConditionalOnProperty(name = "firebase.enabled", havingValue = "true")
public class FirebaseConfig {

	@Bean
	FirebaseApp configurarAplicacaoFirebase(Environment ambiente) throws IOException {
		String caminhoCredenciais = ambiente.getProperty("firebase.credentials-path");
		GoogleCredentials credenciais;
		if (caminhoCredenciais == null || caminhoCredenciais.isBlank()) {
			credenciais = GoogleCredentials.getApplicationDefault();
		} else {
			try (var fluxoCredenciais = Files.newInputStream(Path.of(caminhoCredenciais))) {
				credenciais = GoogleCredentials.fromStream(fluxoCredenciais);
			}
		}
		FirebaseOptions.Builder opcoes = FirebaseOptions.builder().setCredentials(credenciais);
		String idProjeto = ambiente.getProperty("FIREBASE_PROJECT_ID");
		if (idProjeto != null && !idProjeto.isBlank()) {
			opcoes.setProjectId(idProjeto);
		}
		return FirebaseApp.initializeApp(opcoes.build());
	}

	@Bean
	FirebaseAuth autenticacaoFirebase(FirebaseApp aplicacaoFirebase) {
		return FirebaseAuth.getInstance(aplicacaoFirebase);
	}
}
