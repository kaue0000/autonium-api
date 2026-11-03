package br.com.snap.gerenciamento_custos.autenticacao;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTests {

	@Autowired
	private MockMvc mockMvc;

	@Test
	void deveInformarQuandoFirebaseNaoEstaConfigurado() throws Exception {
		mockMvc.perform(post("/api/auth/login")
						.contentType(MediaType.APPLICATION_JSON)
						.content("""
								{"tokenId":"token-invalido"}
								"""))
				.andExpect(status().isServiceUnavailable())
				.andExpect(jsonPath("$.mensagem").value("Firebase Auth não está configurado no servidor."));
	}

	@Test
	void deveExigirAutenticacaoNasRotasProtegidas() throws Exception {
		mockMvc.perform(get("/api/privado"))
				.andExpect(status().isUnauthorized())
				.andExpect(jsonPath("$.mensagem").value("Autenticação obrigatória."));
	}

	@Test
	void deveRejeitarCadastroComDadosInvalidos() throws Exception {
		mockMvc.perform(post("/api/auth/cadastro")
						.contentType(MediaType.APPLICATION_JSON)
						.content("""
								{
								  "nome": "",
								  "sobrenome": "SILVA",
								  "dataNascimento": "01/01/1990",
								  "telefone": "11999999999",
								  "cpf": "12345678901",
								  "cep": "12345678",
								  "uf": "SP",
								  "cidade": "Sao Paulo",
								  "bairro": "Centro",
								  "rua": "Rua Um",
								  "numero": "10",
								  "email": "admin@exemplo.com",
								  "senha": "senha-segura"
								}
								"""))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.mensagem").value("O nome é obrigatório e deve ter até 50 caracteres."));
	}
}
