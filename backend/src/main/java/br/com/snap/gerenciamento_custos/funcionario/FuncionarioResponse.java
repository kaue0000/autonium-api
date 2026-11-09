package br.com.snap.gerenciamento_custos.funcionario;

import java.time.LocalDate;

public record FuncionarioResponse(
        Integer id,
        String nome,
        String sobrenome,
        LocalDate dataNascimento,
        String telefone,
        String cpf,
        String cep,
        UF uf,
        String cidade,
        String bairro,
        String rua,
        String numero,
        String complemento,
        double porcentagemVenda
) {
    public static FuncionarioResponse from(Funcionario f) {
        return new FuncionarioResponse(
                f.getId(),
                f.getNome(),
                f.getSobrenome(),
                f.getDataNascimento(),
                f.getTelefone(),
                f.getCpf(),
                f.getCep(),
                f.getUf(),
                f.getCidade(),
                f.getBairro(),
                f.getRua(),
                f.getNumero(),
                f.getComplemento(),
                f.getPorcentagemVenda()
        );
    }
}
