package br.com.snap.gerenciamento_custos.funcionario;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Table(name = "funcionario", uniqueConstraints = @UniqueConstraint(name = "uk_funcionario_cpf", columnNames = "cpf"))
@Entity
@Getter
@Setter
public class Funcionario {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(name = "nome", nullable = false, length = 50)
    private String nome;

    @Column(name = "sobrenome", nullable = false, length = 50)
    private String sobrenome;

    @Column(nullable = false, name = "data_nascimento")
    private LocalDate dataNascimento;

    @Column(name = "telefone", nullable = false, length = 11)
    private String telefone;

    @Column(name = "cpf", nullable = false, length = 11)
    private String cpf;

    @Column(name = "cep", nullable = false, length = 8)
    private String cep;

    @Enumerated(EnumType.STRING)
    @Column(name = "uf", nullable = false, length = 2)
    private UF uf;

    @Column(name = "cidade", nullable = false, length = 40)
    private String cidade;

    @Column(name = "bairro", nullable = false, length = 40)
    private String bairro;

    @Column(name = "rua", nullable = false, length = 40)
    private String rua;

    @Column(name = "numero", nullable = false, length = 5)
    private String numero;

    @Column(name = "complemento", length = 40)
    private String complemento;

    @Column(name = "porcentagem_venda", nullable = false)
    private Integer porcentagemVenda;
}
