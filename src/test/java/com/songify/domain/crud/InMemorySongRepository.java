package com.songify.domain.crud;

import org.springframework.data.domain.Pageable;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

class InMemorySongRepository implements SongRepository {

    private final Map<Long, Song> db = new LinkedHashMap<>();
    private final AtomicLong index = new AtomicLong(0);

    @Override
    public Song save(Song song) {
        long id = song.getId() == null ? index.getAndIncrement() : song.getId();
        song.setId(id);
        db.put(id, song);
        return song;
    }

    @Override
    public List<Song> findAll(Pageable pageable) {
        return db.values().stream().toList();
    }

    @Override
    public Optional<Song> findById(Long id) {
        return Optional.ofNullable(db.get(id));
    }
    @Override
    public void deleteById(Long id) {
        db.remove(id);
    }

    @Override
    public void updateById(Long id, Song newSong) {
        Song song = db.get(id);
        if (song != null) {
            song.setName(newSong.getName());
            song.setArtist(newSong.getArtist());
        }
    }

    @Override
    public void deleteByIdIn(Collection<Long> ids) {
        ids.forEach(db::remove);
    }
}
