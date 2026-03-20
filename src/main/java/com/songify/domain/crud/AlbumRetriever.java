package com.songify.domain.crud;

import com.songify.domain.crud.dto.AlbumDto;
import com.songify.domain.crud.dto.AlbumDtoWithSongAndArtist;
import com.songify.domain.crud.dto.ArtistDto;
import com.songify.domain.crud.dto.SongDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
class AlbumRetriever {

    private final AlbumRepository albumRepository;

    AlbumDtoWithSongAndArtist findAlbumWithArtistAndSongById(Long id) {
        Album  album = albumRepository.findById(id).orElseThrow(() ->
                new AlbumNotFoundException("Album with id " + id + "not found"));

        AlbumDto albumDto = new AlbumDto(album.getId(), album.getTitle());

        Set<Artist> artists = album.getArtists();
        Set<ArtistDto> artistDtos = artists.stream().map(artist ->
                new ArtistDto(artist.getName())).collect(Collectors.toSet());

        Set<Song> songs = album.getSongs();
        Set<SongDto> songDtos = songs.stream().map(song ->
                new SongDto(song.getId(), song.getName())).collect(Collectors.toSet());

        return new AlbumDtoWithSongAndArtist(albumDto, artistDtos, songDtos);
    }

    Set<Album> findAlbumsByArtistId(Long id) {
        return albumRepository.findByArtistId(id);
    }
}
