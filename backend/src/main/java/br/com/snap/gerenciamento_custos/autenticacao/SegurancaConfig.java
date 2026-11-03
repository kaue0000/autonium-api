package br.com.snap.gerenciamento_custos.autenticacao;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SegurancaConfig {

	@Bean
	UserDetailsService negarLoginPorSenha() {
		return usuario -> {
			throw new UsernameNotFoundException("A autenticação deve ser feita pelo Firebase.");
		};
	}

	@Bean
	FirebaseTokenFilter filtroTokenFirebase(FirebaseAuthService servicoAutenticacaoFirebase) {
		return new FirebaseTokenFilter(servicoAutenticacaoFirebase);
	}

	@Bean
	SecurityFilterChain configurarSeguranca(HttpSecurity segurancaHttp, FirebaseTokenFilter filtroTokenFirebase)
			throws Exception {
		return segurancaHttp
				.csrf(csrf -> csrf.disable())
				.sessionManagement(sessao -> sessao.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.authorizeHttpRequests(autorizacao -> autorizacao
						.requestMatchers("/api/auth/login", "/api/auth/cadastro").permitAll()
						.anyRequest().authenticated())
				.exceptionHandling(erros -> erros.authenticationEntryPoint((requisicao, resposta, excecao) -> {
					resposta.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
					resposta.setContentType("application/json;charset=UTF-8");
					resposta.getWriter().write("{\"mensagem\":\"Autenticação obrigatória.\"}");
				}))
				.httpBasic(basico -> basico.disable())
				.formLogin(formulario -> formulario.disable())
				.addFilterBefore(filtroTokenFirebase, UsernamePasswordAuthenticationFilter.class)
				.build();
	}
}
