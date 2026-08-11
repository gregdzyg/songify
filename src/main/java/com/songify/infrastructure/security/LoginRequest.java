package com.songify.infrastructure.security;

import jakarta.validation.constraints.NotBlank;

record LoginRequest(@NotBlank String username, @NotBlank String password) {
    LoginRequest {
        username = username == null ? null : DatabaseUserDetailsService.normalize(username);
    }
}
