package br.com.zup.sistema_de_gerenciamento_de_impostos.services;

import br.com.zup.sistema_de_gerenciamento_de_impostos.models.User;
import br.com.zup.sistema_de_gerenciamento_de_impostos.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public List<User> listarTodos() {
        return userRepository.findAll();
    }

    public Optional<User> buscarPorId(Long id) {
        return userRepository.findById(id);
    }

    public User cadastrar(User user) {
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new RuntimeException("Nome de usuário já existe.");
        }
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new RuntimeException("E-mail já está em uso.");
        }
        return userRepository.save(user);
    }

    public User atualizar(Long id, User userAtualizado) {
        User userExistente = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado."));

        userExistente.setUsername(userAtualizado.getUsername());
        userExistente.setEmail(userAtualizado.getEmail());
        userExistente.setPassword(userAtualizado.getPassword());

        return userRepository.save(userExistente);
    }

    public void excluir(Long id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("Usuário não encontrado.");
        }
        userRepository.deleteById(id);
    }
}
