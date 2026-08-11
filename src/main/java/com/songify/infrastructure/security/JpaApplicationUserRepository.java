package com.songify.infrastructure.security;

import org.springframework.data.jpa.repository.JpaRepository;

interface JpaApplicationUserRepository extends ApplicationUserRepository, JpaRepository<ApplicationUser, Long> {
}
