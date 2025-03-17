package br.com.zup.sistema_de_gerenciamento_de_impostos.services;

import br.com.zup.sistema_de_gerenciamento_de_impostos.models.Role;
import br.com.zup.sistema_de_gerenciamento_de_impostos.models.User;
import br.com.zup.sistema_de_gerenciamento_de_impostos.repositories.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    @DisplayName("Deve listar todos os usuários")
    void shouldListAllUsers() {
        // Arrange
        User user1 = new User();
        user1.setId(1L);
        user1.setUsername("user1");
        user1.setEmail("user1@example.com");
        user1.setRole(Role.USER);

        User user2 = new User();
        user2.setId(2L);
        user2.setUsername("admin");
        user2.setEmail("admin@example.com");
        user2.setRole(Role.ADMIN);

        List<User> expectedUsers = Arrays.asList(user1, user2);
        when(userRepository.findAll()).thenReturn(expectedUsers);

        // Act
        List<User> result = userService.findAll();

        // Assert
        assertEquals(expectedUsers.size(), result.size());
        assertEquals(expectedUsers, result);
        verify(userRepository).findAll();
    }

    @Test
    @DisplayName("Deve buscar usuário por ID quando existir")
    void shouldFindUserByIdWhenExists() {
        // Arrange
        Long userId = 1L;
        User user = new User();
        user.setId(userId);
        user.setUsername("testuser");
        user.setEmail("test@example.com");
        user.setRole(Role.USER);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        // Act
        Optional<User> result = userService.findById(userId);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(userId, result.get().getId());
        assertEquals("testuser", result.get().getUsername());
        verify(userRepository).findById(userId);
    }

    @Test
    @DisplayName("Deve retornar Optional vazio quando buscar por ID inexistente")
    void shouldReturnEmptyOptionalWhenUserIdDoesNotExist() {
        // Arrange
        Long userId = 999L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Act
        Optional<User> result = userService.findById(userId);

        // Assert
        assertFalse(result.isPresent());
        verify(userRepository).findById(userId);
    }

    @Test
    @DisplayName("Deve cadastrar usuário com sucesso")
    void shouldRegisterUserSuccessfully() {
        // Arrange
        User newUser = new User();
        newUser.setUsername("newuser");
        newUser.setEmail("new@example.com");
        newUser.setPassword("password123");
        newUser.setRole(Role.USER);

        User savedUser = new User();
        savedUser.setId(1L);
        savedUser.setUsername("newuser");
        savedUser.setEmail("new@example.com");
        savedUser.setPassword("password123");
        savedUser.setRole(Role.USER);

        when(userRepository.findByUsername("newuser")).thenReturn(Optional.empty());
        when(userRepository.findByEmail("new@example.com")).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        // Act
        User result = userService.save(newUser);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("newuser", result.getUsername());
        assertEquals("new@example.com", result.getEmail());
        verify(userRepository).save(newUser);
    }

    @Test
    @DisplayName("Deve lançar exceção quando cadastrar usuário com nome duplicado")
    void shouldThrowExceptionWhenRegisteringUserWithDuplicateUsername() {
        // Arrange
        User existingUser = new User();
        existingUser.setId(1L);
        existingUser.setUsername("existinguser");

        User newUser = new User();
        newUser.setUsername("existinguser");
        newUser.setEmail("new@example.com");

        when(userRepository.findByUsername("existinguser")).thenReturn(Optional.of(existingUser));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userService.save(newUser);
        });

        assertEquals("Nome de usuário já existe.", exception.getMessage());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("Deve lançar exceção quando cadastrar usuário com email duplicado")
    void shouldThrowExceptionWhenRegisteringUserWithDuplicateEmail() {
        // Arrange
        User existingUser = new User();
        existingUser.setId(1L);
        existingUser.setEmail("existing@example.com");

        User newUser = new User();
        newUser.setUsername("newuser");
        newUser.setEmail("existing@example.com");

        when(userRepository.findByUsername("newuser")).thenReturn(Optional.empty());
        when(userRepository.findByEmail("existing@example.com")).thenReturn(Optional.of(existingUser));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userService.save(newUser);
        });

        assertEquals("E-mail já está em uso.", exception.getMessage());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("Deve atualizar usuário com sucesso")
    void shouldUpdateUserSuccessfully() {
        // Arrange
        Long userId = 1L;
        
        User existingUser = new User();
        existingUser.setId(userId);
        existingUser.setUsername("oldusername");
        existingUser.setEmail("old@example.com");
        existingUser.setPassword("oldpassword");
        
        User updatedUser = new User();
        updatedUser.setUsername("newusername");
        updatedUser.setEmail("new@example.com");
        updatedUser.setPassword("newpassword");
        
        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));
        
        // Act
        User result = userService.update(userId, updatedUser);
        
        // Assert
        assertNotNull(result);
        assertEquals(userId, result.getId());
        assertEquals("newusername", result.getUsername());
        assertEquals("new@example.com", result.getEmail());
        assertEquals("newpassword", result.getPassword());
        verify(userRepository).save(existingUser);
    }

    @Test
    @DisplayName("Deve lançar exceção quando atualizar usuário inexistente")
    void shouldThrowExceptionWhenUpdatingNonExistentUser() {
        // Arrange
        Long userId = 999L;
        User updatedUser = new User();
        
        when(userRepository.findById(userId)).thenReturn(Optional.empty());
        
        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userService.update(userId, updatedUser);
        });
        
        assertEquals("Usuário não encontrado.", exception.getMessage());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("Deve excluir usuário com sucesso")
    void shouldDeleteUserSuccessfully() {
        // Arrange
        Long userId = 1L;
        when(userRepository.existsById(userId)).thenReturn(true);
        doNothing().when(userRepository).deleteById(userId);
        
        // Act
        assertDoesNotThrow(() -> userService.delete(userId));
        
        // Assert
        verify(userRepository).deleteById(userId);
    }

    @Test
    @DisplayName("Deve lançar exceção quando excluir usuário inexistente")
    void shouldThrowExceptionWhenDeletingNonExistentUser() {
        // Arrange
        Long userId = 999L;
        when(userRepository.existsById(userId)).thenReturn(false);
        
        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userService.delete(userId);
        });
        
        assertEquals("Usuário não encontrado.", exception.getMessage());
        verify(userRepository, never()).deleteById(anyLong());
    }
}