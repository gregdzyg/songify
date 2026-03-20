package com.songify.domain.crud;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
@AllArgsConstructor
class AlbumDeleter {

    AlbumRepository albumRepository;

    void deleteAlbumsById(Collection<Long> ids) {
        albumRepository.deleteByIdIn(ids);
    }
}
