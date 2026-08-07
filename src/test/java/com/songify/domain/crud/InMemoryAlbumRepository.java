package com.songify.domain.crud;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;

class InMemoryAlbumRepository implements AlbumRepository {
    @Override
    public Album save(Album album) {
        return null;
    }

    @Override
    public Optional<Album> findById(Long id) {
        return Optional.empty();
    }

    @Override
    public Set<Album> findByArtistId(Long id) {
        return Set.of();
    }

    @Override
    public void deleteByIdIn(Collection<Long> ids) {

    }
}
