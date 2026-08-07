package com.songify.domain.crud;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

class InMemoryGenreRepository implements GenreRepository{

    private final Map<Long, Genre> db = new LinkedHashMap<>();
    private final AtomicLong index = new AtomicLong(0);

    @Override
    public Genre getGenreById(Long id) {
        return db.get(id);
    }

    @Override
    public Genre save(Genre genre) {
        long id = genre.getId() == null ? index.getAndIncrement() : genre.getId();
        genre.setId(id);
        db.put(id, genre);
        return genre;
    }
    @Override
    public void deleteById(Long id) {
        db.remove(id);
    }
}
