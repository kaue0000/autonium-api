package br.com.snap.gerenciamento_custos.funcionario;

import br.com.snap.gerenciamento_custos.administrador.AdministradorRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Locale;

@Service
public class FuncionarioService {

    private final FuncionarioRepository funcionarioRepository;
    private final AdministradorRepository administradorRepository;

    public FuncionarioService(
            FuncionarioRepository funcionarioRepository,
            AdministradorRepository administradorRepository
    ) {
        this.funcionarioRepository = funcionarioRepository;
        this.administradorRepository = administradorRepository;
    }

    @Transactional
    public FuncionarioResponse register(FuncionarioRequest request) {
        validarDados(request);
        if (funcionarioRepository.existsByCpf(request.cpf())) {
            throw new ErroFuncionario(HttpStatus.CONFLICT, "O CPF informado já está cadastrado.");
        }
        Funcionario salvo = funcionarioRepository.save(mapear(request, new Funcionario()));
        return FuncionarioResponse.from(salvo);
    }

    @Transactional
    public FuncionarioResponse update(Integer id, FuncionarioRequest request) {
        validarDados(request);
        Funcionario funcionario = get(id);
        if (funcionarioRepository.existsByCpfAndIdNot(request.cpf(), id)) {
            throw new ErroFuncionario(HttpStatus.CONFLICT, "O CPF informado já está cadastrado.");
        }
        return FuncionarioResponse.from(funcionarioRepository.save(mapear(request, funcionario)));
    }

    // Interno: devolve a entidade (usado por update e delete)
    @Transactional(readOnly = true)
    public Funcionario get(Integer id) {
        return funcionarioRepository.findById(id)
                .orElseThrow(() -> new ErroFuncionario(HttpStatus.NOT_FOUND, "Funcionário não encontrado."));
    }

    // Para o controller: devolve o DTO
    @Transactional(readOnly = true)
    public FuncionarioResponse getResponse(Integer id) {
        return FuncionarioResponse.from(get(id));
    }

    @Transactional(readOnly = true)
    public List<FuncionarioResponse> getAll() {
        return funcionarioRepository.findAll().stream()
                .map(FuncionarioResponse::from)
                .toList();
    }

    @Transactional
    public void delete(Integer id) {
        Funcionario funcionario = get(id);
        if (administradorRepository.existsByFuncionario_Id(id)) {
            throw new ErroFuncionario(
                    HttpStatus.CONFLICT,
                    "Não é possível excluir um funcionário vinculado a um administrador."
            );
        }
        funcionarioRepository.delete(funcionario);
    }

    private void validarDados(FuncionarioRequest request) {
        if (request.dataNascimento().plusYears(18).isAfter(LocalDate.now())) {
            throw new ErroFuncionario(HttpStatus.BAD_REQUEST, "O funcionário deve ter pelo menos 18 anos.");
        }
    }

    private Funcionario mapear(FuncionarioRequest request, Funcionario funcionario) {
        funcionario.setNome(request.nome().trim().toUpperCase(Locale.ROOT));
        funcionario.setSobrenome(request.sobrenome().trim().toUpperCase(Locale.ROOT));
        funcionario.setDataNascimento(request.dataNascimento());
        funcionario.setTelefone(request.telefone());
        funcionario.setCpf(request.cpf());
        funcionario.setCep(request.cep());
        funcionario.setUf(request.uf());
        funcionario.setCidade(request.cidade().trim());
        funcionario.setBairro(request.bairro().trim());
        funcionario.setRua(request.rua().trim());
        funcionario.setNumero(request.numero().trim());
        funcionario.setComplemento(request.complemento() == null || request.complemento().isBlank()
                ? null
                : request.complemento().trim());
        funcionario.setPorcentagemVenda(request.porcentagemVenda());
        return funcionario;
    }
}
