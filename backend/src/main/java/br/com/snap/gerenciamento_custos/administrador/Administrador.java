package br.com.snap.gerenciamento_custos.administrador;

import br.com.snap.gerenciamento_custos.funcionario.Funcionario;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Table(name = "admin")
@Entity
@Getter
@Setter
public class Administrador {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @OneToOne
    @JoinColumn(name = "funcionario_id")
    private Funcionario funcionario;

    @Column
    private String email;

    @Column
    private String senha;
}
