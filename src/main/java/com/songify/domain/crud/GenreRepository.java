package com.songify.domain.crud;

import org.springframework.data.repository.Repository;

import java.util.Set;

interface GenreRepository extends Repository<Genre, Long> {

    Genre getGenreById(Long id);

    Genre save(Genre genre);

    void deleteById(Long id);
}
