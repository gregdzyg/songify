package com.songify.infrastructure.security;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
class RegistrationService {

    private final ApplicationUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    RegistrationService(ApplicationUserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    RegisterResponse register(RegisterRequest request) {
        String username = DatabaseUserDetailsService.normalize(request.username());
        if (userRepository.existsByUsername(username)) {
            throw new UsernameAlreadyExistsException(username);
        }

        ApplicationUser user = new ApplicationUser(
                username,
                passwordEncoder.encode(request.password()),
                UserRole.USER
        );
        ApplicationUser savedUser = userRepository.save(user);
        return new RegisterResponse(savedUser.getId(), savedUser.getUsername(), savedUser.getRole());
    }
}
