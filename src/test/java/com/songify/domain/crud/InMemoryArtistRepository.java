package com.songify.domain.crud;

import org.springframework.data.domain.Pageable;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

class InMemoryArtistRepository implements ArtistRepository{

    private final Map<Long, Artist> db = new LinkedHashMap<>();
    private final AtomicLong index = new AtomicLong(0);

    @Override
    public Artist save(Artist artist) {
        long id = artist.getId() == null ? index.getAndIncrement() : artist.getId();
        db.put(id, artist);
        artist.setId(id);
        return artist;
    }

    @Override
    public Set<Artist> findAll(Pageable pageable) {
        return new HashSet<>(db.values());
    }

    @Override
    public Optional<Artist> findById(Long id) {
        return Optional.ofNullable(db.get(id));
    }
    @Override
    public void deleteById(Long id) {
        db.remove(id);
    }
}
