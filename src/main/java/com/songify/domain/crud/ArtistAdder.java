package com.songify.domain.crud;

import com.songify.domain.crud.dto.ArtistDto;
import com.songify.domain.crud.dto.ArtistRequestDto;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.Set;
import java.util.UUID;

@AllArgsConstructor
@Service
class ArtistAdder {

    ArtistRepository artistRepository;

    public ArtistDto addArtist(String name) {
        Artist artist = new Artist(name);
        artistRepository.save(artist);
        return new ArtistDto(artist.getName());
    }

    public ArtistDto addArtistWithDefaultAlbumAndSong(ArtistRequestDto artistRequestDto) {
        Artist saved = saveArtistWithDefaultAlbumAndSong(artistRequestDto.name());
        return new ArtistDto(saved.getName());
    }
 //uzywa cascade = persist, merge przy relacji artist-album
    //mozna uzyc serwisow do dodawania albomow i piosenek
    //eliminuje to koniecznosc uzywania kaskady i jest czystsze
    private Artist saveArtistWithDefaultAlbumAndSong(String name) {
        Artist artist = new Artist(name);
        Album album = new Album();
        album.setTitle("default-album " + UUID.randomUUID());
        album.setReleaseDate(LocalDateTime.now().toInstant(ZoneOffset.UTC));
        Song song = new Song("default-song-name " + UUID.randomUUID());
        Genre genre = new Genre("default-genre");
        song.setGenre(genre);
        album.addSong(song);
        artist.setAlbums(Set.of(album));
        return artistRepository.save(artist);
    }
}
