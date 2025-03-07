package br.com.zup.sistema_de_gerenciamento_de_impostos.repositories;

import br.com.zup.sistema_de_gerenciamento_de_impostos.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
}
