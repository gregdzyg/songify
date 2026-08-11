package com.songify.infrastructure.security;

class UsernameAlreadyExistsException extends RuntimeException {

    UsernameAlreadyExistsException(String username) {
        super("Username '" + username + "' is already taken");
    }
}
