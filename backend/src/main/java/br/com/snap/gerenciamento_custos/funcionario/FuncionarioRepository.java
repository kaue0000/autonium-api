package br.com.snap.gerenciamento_custos.funcionario;

import org.springframework.data.jpa.repository.JpaRepository;

public interface FuncionarioRepository extends JpaRepository<Funcionario, Integer> {
}
