package br.com.zup.sistema_de_gerenciamento_de_impostos.controllers;

import br.com.zup.sistema_de_gerenciamento_de_impostos.models.User;
import br.com.zup.sistema_de_gerenciamento_de_impostos.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;
    
    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<List<User>> listarTodos() {
        return ResponseEntity.ok(userService.listarTodos());
    }
    
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<User> buscarPorId(@PathVariable Long id) {
        Optional<User> user = userService.buscarPorId(id);
        return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
    
    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<User> cadastrar(@Valid @RequestBody User user) {
        return ResponseEntity.status(201).body(userService.cadastrar(user));
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<User> atualizar(@PathVariable Long id, @Valid @RequestBody User user) {
        return ResponseEntity.ok(userService.atualizar(id, user));
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        userService.excluir(id);
        return ResponseEntity.noContent().build();
    }
}