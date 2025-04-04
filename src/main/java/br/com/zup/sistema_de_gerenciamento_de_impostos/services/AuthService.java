package br.com.zup.sistema_de_gerenciamento_de_impostos.services;

import br.com.zup.sistema_de_gerenciamento_de_impostos.dto.AuthResponseDto;
import br.com.zup.sistema_de_gerenciamento_de_impostos.dto.LoginDto;
import br.com.zup.sistema_de_gerenciamento_de_impostos.dto.RegisterUserDto;
import br.com.zup.sistema_de_gerenciamento_de_impostos.exceptions.DuplicateResourceException;
import br.com.zup.sistema_de_gerenciamento_de_impostos.infra.jwt.JwtTokenProvider;
import br.com.zup.sistema_de_gerenciamento_de_impostos.models.User;
import br.com.zup.sistema_de_gerenciamento_de_impostos.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {
    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private AuthenticationManager authenticationManager;
    
    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    
    @Transactional
    public User register(RegisterUserDto dto) {
        if (userRepository.findByUsername(dto.username()).isPresent()) {
            throw new DuplicateResourceException("Usuário", "username", dto.username());
        }
        
        if (userRepository.findByEmail(dto.email()).isPresent()) {
            throw new DuplicateResourceException("Usuário", "email", dto.email());
        }
        
        User user = new User();
        user.setUsername(dto.username());
        user.setEmail(dto.email());
        String encodedPassword = passwordEncoder.encode(dto.password());
        user.setPassword(encodedPassword);
        user.setRole(dto.role());
        
        return userRepository.save(user);
    }
    
    public AuthResponseDto login(LoginDto dto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.username(), dto.password())
        );
        
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtTokenProvider.generateToken(authentication);
        
        return new AuthResponseDto(token);
    }
}