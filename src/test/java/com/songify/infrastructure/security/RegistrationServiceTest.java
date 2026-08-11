package com.songify.infrastructure.security;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class RegistrationServiceTest {

    private final InMemoryApplicationUserRepository userRepository = new InMemoryApplicationUserRepository();
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final RegistrationService registrationService =
            new RegistrationService(userRepository, passwordEncoder);

    @Test
    void should_register_user_with_normalized_username_and_hashed_password() {
        RegisterResponse response = registrationService.register(
                new RegisterRequest("  NewUser  ", "secure-password")
        );

        ApplicationUser savedUser = userRepository.findByUsername("newuser").orElseThrow();
        assertThat(response.username()).isEqualTo("newuser");
        assertThat(response.role()).isEqualTo(UserRole.USER);
        assertThat(savedUser.getPasswordHash()).isNotEqualTo("secure-password");
        assertThat(passwordEncoder.matches("secure-password", savedUser.getPasswordHash())).isTrue();
    }

    @Test
    void should_reject_duplicate_username_ignoring_case_and_spaces() {
        registrationService.register(new RegisterRequest("existing", "secure-password"));

        assertThatThrownBy(() -> registrationService.register(
                new RegisterRequest(" Existing ", "another-password")
        ))
                .isInstanceOf(UsernameAlreadyExistsException.class)
                .hasMessage("Username 'existing' is already taken");
    }

    private static class InMemoryApplicationUserRepository implements ApplicationUserRepository {

        private final List<ApplicationUser> users = new ArrayList<>();

        @Override
        public ApplicationUser save(ApplicationUser user) {
            users.add(user);
            return user;
        }

        @Override
        public Optional<ApplicationUser> findByUsername(String username) {
            return users.stream().filter(user -> user.getUsername().equals(username)).findFirst();
        }

        @Override
        public boolean existsByUsername(String username) {
            return findByUsername(username).isPresent();
        }
    }
}
