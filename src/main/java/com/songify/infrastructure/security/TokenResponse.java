package com.songify.infrastructure.security;

record TokenResponse(String accessToken, String tokenType, long expiresIn) {
}
