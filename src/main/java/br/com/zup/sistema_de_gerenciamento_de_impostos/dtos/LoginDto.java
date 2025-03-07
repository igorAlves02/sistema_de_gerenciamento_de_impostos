package br.com.zup.sistema_de_gerenciamento_de_impostos.dto;

import jakarta.validation.constraints.NotBlank;

public record LoginDto(
        @NotBlank(message = "O nome de usuário é obrigatório")
        String username,

        @NotBlank(message = "A senha é obrigatória")
        String password
) {}
