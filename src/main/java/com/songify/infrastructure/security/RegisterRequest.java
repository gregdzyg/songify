package com.songify.infrastructure.security;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

record RegisterRequest(
        @NotBlank @Size(min = 3, max = 50) String username,
        @NotBlank @Size(min = 8, max = 72) String password
) {
    RegisterRequest {
        username = username == null ? null : DatabaseUserDetailsService.normalize(username);
    }
}
