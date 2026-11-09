package br.com.snap.gerenciamento_custos.autenticacao;

import br.com.snap.gerenciamento_custos.administrador.Administrador;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

public class FirebaseTokenFilter extends OncePerRequestFilter {

	private final FirebaseAuthService servicoAutenticacaoFirebase;

	public FirebaseTokenFilter(FirebaseAuthService servicoAutenticacaoFirebase) {
		this.servicoAutenticacaoFirebase = servicoAutenticacaoFirebase;
	}

	@Override
	protected boolean shouldNotFilter(HttpServletRequest requisicao) {
		String caminho = requisicao.getServletPath();
		return caminho.equals("/api/auth/login")
				|| caminho.equals("/api/auth/cadastro")
				|| caminho.equals("/api/auth/logout");
	}

	@Override
	protected void doFilterInternal(
			HttpServletRequest requisicao,
			HttpServletResponse resposta,
			FilterChain cadeiaFiltros
	) throws ServletException, IOException {
		String cabecalho = requisicao.getHeader(HttpHeaders.AUTHORIZATION);
		if (!StringUtils.hasText(cabecalho)) {
			cadeiaFiltros.doFilter(requisicao, resposta);
			return;
		}
		if (!cabecalho.startsWith("Bearer ") || cabecalho.length() <= 7) {
			responderErro(resposta, HttpStatus.UNAUTHORIZED, "Token inválido ou sessão encerrada.");
			return;
		}

		try {
			Administrador administrador = servicoAutenticacaoFirebase.validarToken(cabecalho.substring(7));
			UsernamePasswordAuthenticationToken autenticacao = new UsernamePasswordAuthenticationToken(
					administrador,
					null,
					List.of(new SimpleGrantedAuthority("ROLE_ADMIN"))
			);
			SecurityContextHolder.getContext().setAuthentication(autenticacao);
		} catch (ErroAutenticacao excecao) {
			responderErro(resposta, excecao.getStatus(), excecao.getMessage());
			return;
		}

		cadeiaFiltros.doFilter(requisicao, resposta);
	}

	private void responderErro(HttpServletResponse resposta, HttpStatus status, String mensagem) throws IOException {
		SecurityContextHolder.clearContext();
		resposta.setStatus(status.value());
		resposta.setContentType("application/json");
		resposta.getWriter().write("{\"mensagem\":\"" + mensagem + "\"}");
	}
}
