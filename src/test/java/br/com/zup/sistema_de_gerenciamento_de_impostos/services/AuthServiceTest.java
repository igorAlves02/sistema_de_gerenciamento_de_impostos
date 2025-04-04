package br.com.zup.sistema_de_gerenciamento_de_impostos.services;

import br.com.zup.sistema_de_gerenciamento_de_impostos.dto.AuthResponseDto;
import br.com.zup.sistema_de_gerenciamento_de_impostos.dto.LoginDto;
import br.com.zup.sistema_de_gerenciamento_de_impostos.dto.RegisterUserDto;
import br.com.zup.sistema_de_gerenciamento_de_impostos.exceptions.DuplicateResourceException;
import br.com.zup.sistema_de_gerenciamento_de_impostos.infra.jwt.JwtTokenProvider;
import br.com.zup.sistema_de_gerenciamento_de_impostos.models.Role;
import br.com.zup.sistema_de_gerenciamento_de_impostos.models.User;
import br.com.zup.sistema_de_gerenciamento_de_impostos.repositories.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @InjectMocks
    private AuthService authService;

    @Test
    @DisplayName("Deve registrar um usuário com sucesso")
    void shouldRegisterUserSuccessfully() {
        // Arrange
        RegisterUserDto registerDto = new RegisterUserDto(
                "newuser",
                "new@example.com",
                "password123",
                Role.USER
        );

        String encodedPassword = "encodedPassword123";
        
        when(userRepository.findByUsername("newuser")).thenReturn(Optional.empty());
        when(userRepository.findByEmail("new@example.com")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("password123")).thenReturn(encodedPassword);
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User savedUser = invocation.getArgument(0);
            savedUser.setId(1L);
            return savedUser;
        });

        // Act
        User result = authService.register(registerDto);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("newuser", result.getUsername());
        assertEquals("new@example.com", result.getEmail());
        assertEquals(encodedPassword, result.getPassword());
        assertEquals(Role.USER, result.getRole());
        
        verify(passwordEncoder).encode("password123");
        verify(userRepository).save(any(User.class));
    }

    @Test
    @DisplayName("Deve lançar DuplicateResourceException quando tentar registrar usuário com nome já existente")
    void shouldThrowDuplicateResourceExceptionWhenUsernameAlreadyExists() {
        // Arrange
        RegisterUserDto registerDto = new RegisterUserDto(
                "existinguser",
                "new@example.com",
                "password123",
                Role.USER
        );
        
        User existingUser = new User();
        existingUser.setId(1L);
        existingUser.setUsername("existinguser");
        
        when(userRepository.findByUsername("existinguser")).thenReturn(Optional.of(existingUser));

        // Act & Assert
        DuplicateResourceException exception = assertThrows(DuplicateResourceException.class, () -> {
            authService.register(registerDto);
        });
        
        assertTrue(exception.getMessage().contains("já existe"));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("Deve lançar DuplicateResourceException quando tentar registrar usuário com email já existente")
    void shouldThrowDuplicateResourceExceptionWhenEmailAlreadyExists() {
        // Arrange
        RegisterUserDto registerDto = new RegisterUserDto(
                "newuser",
                "existing@example.com",
                "password123",
                Role.USER
        );
        
        User existingUser = new User();
        existingUser.setId(1L);
        existingUser.setEmail("existing@example.com");
        
        when(userRepository.findByUsername("newuser")).thenReturn(Optional.empty());
        when(userRepository.findByEmail("existing@example.com")).thenReturn(Optional.of(existingUser));

        // Act & Assert
        DuplicateResourceException exception = assertThrows(DuplicateResourceException.class, () -> {
            authService.register(registerDto);
        });
        
        assertTrue(exception.getMessage().contains("já existe"));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("Deve fazer login com sucesso e retornar token JWT")
    void shouldLoginSuccessfullyAndReturnToken() {
        // Arrange
        LoginDto loginDto = new LoginDto("user", "password");
        Authentication authentication = mock(Authentication.class);
        String token = "jwt.token.string";
        
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(jwtTokenProvider.generateToken(authentication)).thenReturn(token);

        // Act
        AuthResponseDto response = authService.login(loginDto);

        // Assert
        assertNotNull(response);
        assertEquals(token, response.token());
        
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtTokenProvider).generateToken(authentication);
    }
}