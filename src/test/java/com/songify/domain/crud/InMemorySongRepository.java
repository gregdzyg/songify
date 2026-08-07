package com.songify.domain.crud;

import org.springframework.data.domain.Pageable;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

class InMemorySongRepository implements SongRepository {
    @Override
    public Song save(Song song) {
        return null;
    }

    @Override
    public List<Song> findAll(Pageable pageable) {
        return List.of();
    }

    @Override
    public Optional<Song> findById(Long id) {
        return Optional.empty();
    }

    @Override
    public void deleteById(Long id) {

    }

    @Override
    public void updateById(Long id, Song newSong) {

    }

    @Override
    public void deleteByIdIn(Collection<Long> ids) {

    }
}
