package br.com.snap.gerenciamento_custos.funcionario;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Table(name = "funcionario")
@Entity
@Getter
@Setter
public class Funcionario {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(nullable = false, length = 50)
    private String nome;

    @Column(nullable = false, length = 50)
    private String sobrenome;

    @Column(nullable = false, name = "data_nascimento")
    private LocalDate dataNascimento;

    @Column(nullable = false) // length = 11
    private String telefone;

    @Column(nullable = false)
    private String cpf;

    @Column(nullable = false)
    private UF uf;

    @Column(nullable = false, length = 40)
    private String cidade;

    @Column(nullable = false, length = 40)
    private String bairro;

    @Column(nullable = false, length = 40)
    private String rua;

    @Column(nullable = false, length = 5)
    private String numero;

    @Column(length = 40)
    private String complemento;

    @Column(name = "porcentagem_venda")
    private double porcentagemVenda;
}
