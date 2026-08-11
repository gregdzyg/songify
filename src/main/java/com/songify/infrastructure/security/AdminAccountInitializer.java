package com.songify.infrastructure.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
class AdminAccountInitializer implements ApplicationRunner {

    private final ApplicationUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final String adminUsername;
    private final String adminPassword;

    AdminAccountInitializer(ApplicationUserRepository userRepository,
                            PasswordEncoder passwordEncoder,
                            @Value("${songify.security.admin-username:}") String adminUsername,
                            @Value("${songify.security.admin-password:}") String adminPassword) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.adminUsername = adminUsername;
        this.adminPassword = adminPassword;
    }

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        if (adminUsername.isBlank() && adminPassword.isBlank()) {
            return;
        }
        if (adminUsername.isBlank() || adminPassword.isBlank()) {
            throw new IllegalStateException("Both admin username and password must be configured");
        }
        if (adminPassword.length() < 8 || adminPassword.length() > 72) {
            throw new IllegalStateException("Admin password must contain between 8 and 72 characters");
        }

        String normalizedUsername = DatabaseUserDetailsService.normalize(adminUsername);
        if (normalizedUsername.length() < 3 || normalizedUsername.length() > 50) {
            throw new IllegalStateException("Admin username must contain between 3 and 50 characters");
        }
        if (!userRepository.existsByUsername(normalizedUsername)) {
            userRepository.save(new ApplicationUser(
                    normalizedUsername,
                    passwordEncoder.encode(adminPassword),
                    UserRole.ADMIN
            ));
        }
    }
}
