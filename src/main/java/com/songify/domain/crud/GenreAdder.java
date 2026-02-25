package com.songify.domain.crud;

import com.songify.domain.crud.dto.GenreDto;
import com.songify.domain.crud.dto.GenreRequestDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
class GenreAdder {

    private final GenreRepository genreRepository;

    public GenreDto addGenre(String name){

        Genre genre = new Genre(name);
        genreRepository.save(genre);
        return new GenreDto(genre.getName());
    }
}
