package br.com.snap.gerenciamento_custos.funcionario;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record FuncionarioRequest(
        @NotBlank(message = "O nome é obrigatório.")
        @Size(max = 50, message = "O nome deve ter até 50 caracteres.")
        String nome,

        @NotBlank(message = "O sobrenome é obrigatório.")
        @Size(max = 50, message = "O sobrenome deve ter até 50 caracteres.")
        String sobrenome,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
        @NotNull(message = "A data de nascimento é obrigatória.")
        LocalDate dataNascimento,

        @NotBlank(message = "O telefone é obrigatório.")
        @Pattern(regexp = "\\d{10,11}", message = "O telefone deve conter 10 ou 11 dígitos.")
        String telefone,

        @NotBlank(message = "O CPF é obrigatório.")
        @Pattern(regexp = "\\d{11}", message = "O CPF deve conter 11 dígitos.")
        String cpf,

        @NotBlank(message = "O CEP é obrigatório.")
        @Pattern(regexp = "\\d{8}", message = "O CEP deve conter 8 dígitos.")
        String cep,

        @NotNull(message = "A UF é obrigatória.")
        UF uf,

        @NotBlank(message = "A cidade é obrigatória.")
        @Size(max = 40, message = "A cidade deve ter até 40 caracteres.")
        String cidade,

        @NotBlank(message = "O bairro é obrigatório.")
        @Size(max = 40, message = "O bairro deve ter até 40 caracteres.")
        String bairro,

        @NotBlank(message = "A rua é obrigatória.")
        @Size(max = 40, message = "A rua deve ter até 40 caracteres.")
        String rua,

        @NotBlank(message = "O número é obrigatório.")
        @Size(max = 5, message = "O número deve ter até 5 caracteres.")
        String numero,

        @Size(max = 40, message = "O complemento deve ter até 40 caracteres.")
        String complemento,

        @NotNull(message = "A porcentagem de venda é obrigatória.")
        @Min(value = 1, message = "A porcentagem de venda deve ser maior que 0.")
        @Max(value = 99, message = "A porcentagem de venda deve ser menor que 100.")
        Integer porcentagemVenda
) {
}
