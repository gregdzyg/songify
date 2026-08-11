package com.songify.infrastructure.security;

import java.util.Optional;

interface ApplicationUserRepository {

    ApplicationUser save(ApplicationUser user);

    Optional<ApplicationUser> findByUsername(String username);

    boolean existsByUsername(String username);
}
