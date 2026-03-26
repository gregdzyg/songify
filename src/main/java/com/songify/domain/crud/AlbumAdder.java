package com.songify.domain.crud;


import com.songify.domain.crud.dto.AlbumDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;

@AllArgsConstructor
@Service
class AlbumAdder {
    private final SongRetriever songRetriever;
    private final AlbumRepository albumRepository;

    public AlbumDto addAlbum(Long songId, String title, Instant releaseDate) {

        Song song = songRetriever.findById(songId);

        Album album = new Album();
        album.setTitle(title);
        album.addSong(song);
        album.setReleaseDate(releaseDate);

        albumRepository.save(album);
        return new AlbumDto(album.getId(), album.getTitle());
    }
}
