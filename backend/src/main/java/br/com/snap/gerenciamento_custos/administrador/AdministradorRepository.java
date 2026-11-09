package br.com.snap.gerenciamento_custos.administrador;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdministradorRepository extends JpaRepository<Administrador, Integer> {
    Optional<Administrador> findByFirebaseUid(String firebaseUid);

    boolean existsByFuncionario_Id(Integer funcionarioId);
}
