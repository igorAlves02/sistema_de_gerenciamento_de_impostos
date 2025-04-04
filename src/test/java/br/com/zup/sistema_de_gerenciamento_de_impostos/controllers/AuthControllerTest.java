package br.com.zup.sistema_de_gerenciamento_de_impostos.controllers;

import br.com.zup.sistema_de_gerenciamento_de_impostos.dto.AuthResponseDto;
import br.com.zup.sistema_de_gerenciamento_de_impostos.dto.LoginDto;
import br.com.zup.sistema_de_gerenciamento_de_impostos.dto.RegisterUserDto;
import br.com.zup.sistema_de_gerenciamento_de_impostos.exceptions.DuplicateResourceException;
import br.com.zup.sistema_de_gerenciamento_de_impostos.exceptions.UnauthorizedException;
import br.com.zup.sistema_de_gerenciamento_de_impostos.models.Role;
import br.com.zup.sistema_de_gerenciamento_de_impostos.models.User;
import br.com.zup.sistema_de_gerenciamento_de_impostos.services.AuthService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock
    private AuthService authService;

    @InjectMocks
    private AuthController authController;

    @Test
    @DisplayName("Deve registrar usuário e retornar status 201")
    void shouldRegisterUserAndReturn201Status() {
        // Arrange
        RegisterUserDto registerDto = new RegisterUserDto(
                "newuser",
                "new@example.com",
                "password123",
                Role.USER
        );

        User registeredUser = new User();
        registeredUser.setId(1L);
        registeredUser.setUsername("newuser");
        registeredUser.setEmail("new@example.com");
        registeredUser.setPassword("hashedpassword");
        registeredUser.setRole(Role.USER);

        when(authService.register(any(RegisterUserDto.class))).thenReturn(registeredUser);

        // Act
        ResponseEntity<User> response = authController.register(registerDto);

        // Assert
        assertAll(
                () -> assertNotNull(response, "A resposta não deve ser nula"),
                () -> assertNotNull(response.getBody(), "O corpo da resposta não deve ser nulo"),
                () -> assertEquals(HttpStatus.CREATED, response.getStatusCode(), "Status HTTP deve ser 201 CREATED"),
                () -> assertEquals(1L, response.getBody().getId()),
                () -> assertEquals("newuser", response.getBody().getUsername()),
                () -> assertEquals("new@example.com", response.getBody().getEmail()),
                () -> assertEquals(Role.USER, response.getBody().getRole())
        );

        verify(authService).register(registerDto);
    }

    @Test
    @DisplayName("Deve lançar DuplicateResourceException quando registrar usuário com nome duplicado")
    void shouldHandleExceptionWhenRegisteringDuplicateUser() {
        // Arrange
        RegisterUserDto registerDto = new RegisterUserDto(
                "existinguser",
                "new@example.com",
                "password123",
                Role.USER
        );

        when(authService.register(any(RegisterUserDto.class)))
            .thenThrow(new DuplicateResourceException("Usuário", "username", "existinguser"));

        // Act & Assert
        DuplicateResourceException exception = assertThrows(DuplicateResourceException.class, () -> {
            authController.register(registerDto);
        }, "Deve lançar DuplicateResourceException para username duplicado");

        assertTrue(exception.getMessage().contains("existinguser"));
        verify(authService).register(registerDto);
    }

    @Test
    @DisplayName("Deve lançar DuplicateResourceException quando registrar usuário com email duplicado")
    void shouldHandleExceptionWhenRegisteringDuplicateEmail() {
        // Arrange
        RegisterUserDto registerDto = new RegisterUserDto(
                "newuser",
                "existing@example.com",
                "password123",
                Role.USER
        );

        when(authService.register(any(RegisterUserDto.class)))
            .thenThrow(new DuplicateResourceException("Usuário", "email", "existing@example.com"));

        // Act & Assert
        DuplicateResourceException exception = assertThrows(DuplicateResourceException.class, () -> {
            authController.register(registerDto);
        }, "Deve lançar DuplicateResourceException para email duplicado");

        assertTrue(exception.getMessage().contains("existing@example.com"));
        verify(authService).register(registerDto);
    }

    @Test
    @DisplayName("Deve fazer login com sucesso e retornar token JWT")
    void shouldLoginSuccessfullyAndReturnToken() {
        // Arrange
        LoginDto loginDto = new LoginDto("user", "password");
        String jwtToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...";
        AuthResponseDto authResponse = new AuthResponseDto(jwtToken);

        when(authService.login(any(LoginDto.class))).thenReturn(authResponse);

        // Act
        ResponseEntity<AuthResponseDto> response = authController.login(loginDto);

        // Assert
        assertAll(
                () -> assertNotNull(response, "A resposta não deve ser nula"),
                () -> assertNotNull(response.getBody(), "O corpo da resposta não deve ser nulo"),
                () -> assertEquals(HttpStatus.OK, response.getStatusCode(), "Status HTTP deve ser 200 OK"),
                () -> assertEquals(jwtToken, response.getBody().token(), "O token retornado deve ser igual ao esperado")
        );

        verify(authService).login(loginDto);
    }

    @Test
    @DisplayName("Deve lançar UnauthorizedException quando credenciais de login são inválidas")
    void shouldHandleExceptionWhenLoginCredentialsAreInvalid() {
        // Arrange
        LoginDto loginDto = new LoginDto("user", "wrongpassword");

        when(authService.login(any(LoginDto.class)))
            .thenThrow(new UnauthorizedException("Credenciais inválidas"));

        // Act & Assert
        UnauthorizedException exception = assertThrows(UnauthorizedException.class, () -> {
            authController.login(loginDto);
        }, "Deve lançar UnauthorizedException para credenciais inválidas");

        assertTrue(exception.getMessage().contains("Credenciais inválidas"));
        verify(authService).login(loginDto);
    }
}