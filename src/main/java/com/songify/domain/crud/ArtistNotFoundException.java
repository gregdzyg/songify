package com.songify.domain.crud;

class ArtistNotFoundException extends RuntimeException {
    ArtistNotFoundException(String message) {
        super("Artist with id " + message + " not found");
    }
}
