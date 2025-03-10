package br.com.zup.sistema_de_gerenciamento_de_impostos.controllers;

import br.com.zup.sistema_de_gerenciamento_de_impostos.dto.AuthResponseDto;
import br.com.zup.sistema_de_gerenciamento_de_impostos.dto.LoginDto;
import br.com.zup.sistema_de_gerenciamento_de_impostos.dto.RegisterUserDto;
import br.com.zup.sistema_de_gerenciamento_de_impostos.models.User;
import br.com.zup.sistema_de_gerenciamento_de_impostos.services.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/user") // Alterado de "/api/auth" para "/user"
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<User> register(@Valid @RequestBody RegisterUserDto dto) {
        User user = authService.register(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(@Valid @RequestBody LoginDto dto) {
        AuthResponseDto response = authService.login(dto);
        return ResponseEntity.ok(response);
    }
}