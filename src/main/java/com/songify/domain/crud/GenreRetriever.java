package com.songify.domain.crud;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
class GenreRetriever {

    private final GenreRepository genreRepository;


    public Genre getGenreById(Long id) {
        return genreRepository.getGenreById(id);
    }
}
