package br.com.zup.sistema_de_gerenciamento_de_impostos.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import br.com.zup.sistema_de_gerenciamento_de_impostos.models.Role;

public record RegisterUserDto(
        @NotBlank(message = "O nome de usuário é obrigatório")
        String username,

        @NotBlank(message = "O e-mail é obrigatório")
        @Email(message = "E-mail inválido")
        String email,

        @NotBlank(message = "A senha é obrigatória")
        @Size(min = 6, message = "A senha deve ter no mínimo 6 caracteres")
        String password,

        Role role
) {}
