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

@Configuration
@ConditionalOnProperty(name = "firebase.enabled", havingValue = "true")
public class FirebaseConfig {

	@Bean
	FirebaseApp configurarAplicacaoFirebase(Environment ambiente) throws IOException {
		FirebaseOptions.Builder opcoes = FirebaseOptions.builder()
				.setCredentials(GoogleCredentials.getApplicationDefault());
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
