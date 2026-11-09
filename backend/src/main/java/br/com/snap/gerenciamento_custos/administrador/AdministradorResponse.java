package br.com.snap.gerenciamento_custos.administrador;

public record AdministradorResponse(int id, String email) {
    public static AdministradorResponse from(Administrador admin){
        return new AdministradorResponse(
                admin.getId(),
                admin.getEmail()
        );
    }
}
