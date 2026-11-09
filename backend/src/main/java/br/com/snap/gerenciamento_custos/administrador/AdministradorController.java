package br.com.snap.gerenciamento_custos.administrador;

import br.com.snap.gerenciamento_custos.autenticacao.ErroAutenticacao;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping
public class AdministradorController {
    private final AdministradorService administradorService;

    public AdministradorController(AdministradorService administradorService) {
        this.administradorService = administradorService;
    }

    @PostMapping("/api/auth/cadastro")
    public ResponseEntity<Administrador> register(@Valid @RequestBody TokenAutenticacaoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(administradorService.register(request.tokenId()));
    }

    @PostMapping("/api/auth/login")
    public Administrador login(@Valid @RequestBody TokenAutenticacaoRequest request) {
        return administradorService.login(request.tokenId());
    }

    @PostMapping("/api/auth/logout")
    public ResponseEntity<Void> logout(@Valid @RequestBody TokenAutenticacaoRequest request) {
        administradorService.logout(request.tokenId());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public AdministradorResponse get(@PathVariable Integer id) {
        return administradorService.get(id);
    }

    @GetMapping("/api/administrador")
    public List<AdministradorResponse> getAll() {
        return administradorService.getAll();
    }

    @ExceptionHandler(ErroAutenticacao.class)
    public ResponseEntity<Map<String, String>> tratarErroAutenticacao(ErroAutenticacao erro) {
        return ResponseEntity.status(erro.getStatus()).body(Map.of("mensagem", erro.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> tratarErroValidacao(MethodArgumentNotValidException erro) {
        String mensagem = erro.getBindingResult().getFieldErrors().get(0).getDefaultMessage();
        return ResponseEntity.badRequest().body(Map.of("mensagem", mensagem));
    }
}
