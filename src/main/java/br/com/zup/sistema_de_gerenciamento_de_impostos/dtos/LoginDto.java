package br.com.zup.authenticator.controllers.dtos;

import lombok.Data;

@Data
public class LoginDto {
    private String username;
    private String password;
}
