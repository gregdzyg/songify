package com.songify.domain.crud;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Log4j2
@AllArgsConstructor
@Service
class ArtistDeleter {
    private final AlbumDeleter albumDeleter;
    private final SongDeleter songDeleter;
    private final ArtistRepository artistRepository;
    private final ArtistRetriever artistRetriever;
    private final AlbumRetriever albumRetriever;
    @Transactional
    void deleteArtistByIdWithAlbumsAndSongs(Long artistId) {
       Artist artist = artistRetriever.findById(artistId);
        Set<Album> albums = albumRetriever.findAlbumsByArtistId(artist.getId());
        if(albums.isEmpty()) {
            log.info("Artist with id {} does not have any albums. Deleting artist...", artistId);
            artistRepository.deleteById(artistId);
            return;
        }

        albums.stream()
                .filter(album -> album.getArtists().size() >= 2)
                .forEach(album -> album.deleteArtist(artist));

        Set<Album> albumsWithOnlyOneArtist = albums.stream()
                .filter(album -> album.getArtists().size() == 1)
                .collect(Collectors.toSet());

        Set<Long> allSongsIdsFromAllAlbumsWithOnlyThisArtist = albumsWithOnlyOneArtist.stream()
                .flatMap(album -> album.getSongs().stream())
                .map(Song::getId)
                .collect(Collectors.toSet());

        Set<Long> albumsIds = albumsWithOnlyOneArtist.stream()
                .map(Album::getId)
                .collect(Collectors.toSet());

        songDeleter.deleteAllSongsByIds(allSongsIdsFromAllAlbumsWithOnlyThisArtist);
        albumDeleter.deleteAlbumsById(albumsIds);
        artistRepository.deleteById(artistId);
    }
}
