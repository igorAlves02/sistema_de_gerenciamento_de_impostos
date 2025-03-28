package br.com.zup.sistema_de_gerenciamento_de_impostos.controllers;

import br.com.zup.sistema_de_gerenciamento_de_impostos.models.Role;
import br.com.zup.sistema_de_gerenciamento_de_impostos.models.User;
import br.com.zup.sistema_de_gerenciamento_de_impostos.services.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @Test
    @DisplayName("Deve retornar lista de usuários com status 200")
    void shouldReturnUserList() {
        // Arrange
        User user1 = new User();
        user1.setId(1L);
        user1.setUsername("user1");
        user1.setEmail("user1@example.com");
        user1.setPassword("hashedpassword1");
        user1.setRole(Role.USER);

        User user2 = new User();
        user2.setId(2L);
        user2.setUsername("admin");
        user2.setEmail("admin@example.com");
        user2.setPassword("hashedpassword2");
        user2.setRole(Role.ADMIN);

        List<User> users = Arrays.asList(user1, user2);
        when(userService.findAll()).thenReturn(users);

        // Act
        ResponseEntity<List<User>> response = userController.findAll();

        // Assert
        assertAll(
            () -> assertNotNull(response, "A resposta não deve ser nula"),
            () -> assertNotNull(response.getBody(), "O corpo da resposta não deve ser nulo"),
            () -> assertEquals(HttpStatus.OK, response.getStatusCode(), "Status HTTP deve ser 200 OK"),
            () -> assertEquals(2, response.getBody().size(), "Devem ser retornados 2 usuários"),
            () -> assertEquals("user1", response.getBody().get(0).getUsername()),
            () -> assertEquals("admin", response.getBody().get(1).getUsername())
        );
        
        verify(userService).findAll();
    }
    
    @Test
    @DisplayName("Deve retornar lista vazia quando não existem usuários")
    void shouldReturnEmptyListWhenNoUsersExist() {
        // Arrange
        List<User> emptyList = new ArrayList<>();
        when(userService.findAll()).thenReturn(emptyList);

        // Act
        ResponseEntity<List<User>> response = userController.findAll();

        // Assert
        assertAll(
            () -> assertNotNull(response, "A resposta não deve ser nula"),
            () -> assertNotNull(response.getBody(), "O corpo da resposta não deve ser nulo"),
            () -> assertEquals(HttpStatus.OK, response.getStatusCode(), "Status HTTP deve ser 200 OK"),
            () -> assertTrue(response.getBody().isEmpty(), "A lista deve estar vazia")
        );
        
        verify(userService).findAll();
    }

    @Test
    @DisplayName("Deve retornar usuário por ID com status 200")
    void shouldReturnUserById() {
        // Arrange
        Long userId = 1L;
        User user = new User();
        user.setId(userId);
        user.setUsername("testuser");
        user.setEmail("test@example.com");
        user.setPassword("hashedpassword");
        user.setRole(Role.USER);
        
        when(userService.findById(userId)).thenReturn(Optional.of(user));

        // Act
        ResponseEntity<User> response = userController.findById(userId);

        // Assert
        assertAll(
            () -> assertNotNull(response, "A resposta não deve ser nula"),
            () -> assertNotNull(response.getBody(), "O corpo da resposta não deve ser nulo"),
            () -> assertEquals(HttpStatus.OK, response.getStatusCode(), "Status HTTP deve ser 200 OK"),
            () -> assertEquals(userId, response.getBody().getId()),
            () -> assertEquals("testuser", response.getBody().getUsername()),
            () -> assertEquals("test@example.com", response.getBody().getEmail())
        );
        
        verify(userService).findById(userId);
    }

    @Test
    @DisplayName("Deve retornar status 404 quando usuário não é encontrado")
    void shouldReturnNotFoundWhenUserDoesNotExist() {
        // Arrange
        Long userId = 999L;
        when(userService.findById(userId)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<User> response = userController.findById(userId);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode(), "Status HTTP deve ser 404 NOT FOUND");
        assertNull(response.getBody(), "O corpo da resposta deve ser nulo");
        
        verify(userService).findById(userId);
    }

    @Test
    @DisplayName("Deve atualizar usuário e retornar status 200")
    void shouldUpdateUserAndReturn200Status() {
        // Arrange
        Long userId = 1L;
        
        User userToUpdate = new User();
        userToUpdate.setUsername("updateduser");
        userToUpdate.setEmail("updated@example.com");
        userToUpdate.setPassword("newpassword");
        
        User updatedUser = new User();
        updatedUser.setId(userId);
        updatedUser.setUsername("updateduser");
        updatedUser.setEmail("updated@example.com");
        updatedUser.setPassword("hashedpassword");
        updatedUser.setRole(Role.USER);
        
        when(userService.update(eq(userId), any(User.class))).thenReturn(updatedUser);

        // Act
        ResponseEntity<User> response = userController.update(userId, userToUpdate);

        // Assert
        assertAll(
            () -> assertNotNull(response, "A resposta não deve ser nula"),
            () -> assertNotNull(response.getBody(), "O corpo da resposta não deve ser nulo"),
            () -> assertEquals(HttpStatus.OK, response.getStatusCode(), "Status HTTP deve ser 200 OK"),
            () -> assertEquals(userId, response.getBody().getId()),
            () -> assertEquals("updateduser", response.getBody().getUsername()),
            () -> assertEquals("updated@example.com", response.getBody().getEmail())
        );
        
        verify(userService).update(eq(userId), any(User.class));
    }

    @Test
    @DisplayName("Deve lançar exceção quando atualizar usuário inexistente")
    void shouldHandleExceptionWhenUpdatingNonExistentUser() {
        // Arrange
        Long userId = 999L;
        User userToUpdate = new User();
        
        when(userService.update(eq(userId), any(User.class))).thenThrow(new RuntimeException("Usuário não encontrado."));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            userController.update(userId, userToUpdate);
        }, "Deve lançar RuntimeException para usuário inexistente");
        
        verify(userService).update(eq(userId), any(User.class));
    }

    @Test
    @DisplayName("Deve excluir usuário e retornar status 204")
    void shouldDeleteUserAndReturn204Status() {
        // Arrange
        Long userId = 1L;
        doNothing().when(userService).delete(userId);

        // Act
        ResponseEntity<Void> response = userController.delete(userId);

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode(), "Status HTTP deve ser 204 NO CONTENT");
        assertNull(response.getBody(), "O corpo da resposta deve ser nulo");
        
        verify(userService).delete(userId);
    }

    @Test
    @DisplayName("Deve lançar exceção quando excluir usuário inexistente")
    void shouldHandleExceptionWhenDeletingNonExistentUser() {
        // Arrange
        Long userId = 999L;
        doThrow(new RuntimeException("Usuário não encontrado.")).when(userService).delete(userId);

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            userController.delete(userId);
        }, "Deve lançar RuntimeException para usuário inexistente");
        
        verify(userService).delete(userId);
    }
}