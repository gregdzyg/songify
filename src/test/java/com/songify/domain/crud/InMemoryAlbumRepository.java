package com.songify.domain.crud;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

class InMemoryAlbumRepository implements AlbumRepository {

    private final Map<Long, Album> db = new LinkedHashMap<>();
    private final AtomicLong index = new AtomicLong(0);

    @Override
    public Album save(Album album) {
        long id = album.getId() == null ? index.getAndIncrement() : album.getId();
        album.setId(id);
        db.put(id, album);
        return album;
    }

    @Override
    public Optional<Album> findById(Long id) {
        return Optional.ofNullable(db.get(id));
    }

    @Override
    public Set<Album> findByArtistId(Long id) {
        Set<Album> albums = new HashSet<>();
        db.values().stream()
                .filter(album -> album.getArtists().stream()
                        .anyMatch(artist -> Objects.equals(artist.getId(), id)))
                .forEach(albums::add);
        return albums;
    }
    @Override
    public void deleteByIdIn(Collection<Long> ids) {
        ids.forEach(db::remove);
    }
}
