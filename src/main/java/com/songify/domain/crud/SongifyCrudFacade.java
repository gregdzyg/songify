package com.songify.domain.crud;

import com.songify.domain.crud.dto.*;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

import static com.songify.domain.crud.SongDomainMapper.mapFromSongToSOngDto;

@Log4j2
@Service
@AllArgsConstructor
public class SongifyCrudFacade {

    private final SongAdder songAdder;
    private final SongRetriever songRetriever;
    private final SongDeleter songDeleter;
    private final SongUpdater songUpdater;
    private final ArtistAdder artistAdder;
    private final GenreAdder genreAdder;
    private final AlbumAdder albumAdder;
    private final  ArtistRetriever artistRetriever;
    private final AlbumRetriever albumRetriever;
    private final ArtistDeleter artistDeleter;
    private final ArtistAssigner artistAssigner;
    private final ArtistUpdater artistUpdater;


    public AlbumDto addAlbum(AlbumRequestDto albumRequestDto) {
        return albumAdder.addAlbum(albumRequestDto.songId(), albumRequestDto.title(),
                albumRequestDto.releaseDate());
    }

    public GenreDto addGenre(GenreRequestDto genreRequestDto) {
        return genreAdder.addGenre(genreRequestDto.name());
    }

    public ArtistDto addArtist(ArtistRequestDto artistRequestDto) {
        return artistAdder.addArtist(artistRequestDto.name());
    }

    public SongDto addSong(SongRequestDto song) {

        log.info("Added new song: {}", song);
      return  songAdder.addSong(song);

    }

    public AlbumDtoWithSongAndArtist findAlbumWithArtistAndSongById(Long id) {
       return albumRetriever.findAlbumWithArtistAndSongById(id);
    }

    public Set<ArtistDto> findAllArtists(Pageable pageable) {
       return artistRetriever.findAllArtists(pageable);
    }

    public void deleteArtistByIdWithAlbumsAndSongs(Long artistId) {
        artistDeleter.deleteArtistByIdWithAlbumsAndSongs(artistId);

    }

    public void addArtistToAlbum(Long artistId, Long albumId) {
        artistAssigner.addArtistToAlbum(artistId, albumId);
    }

    public ArtistDto updateArtistNameById(Long artistId, String name) {
       return artistUpdater.updateArtistNameById(artistId, name);
    }

    public ArtistDto addArtistWithDefaultAlbumAndSong(ArtistRequestDto artistRequestDto) {
        return artistAdder.addArtistWithDefaultAlbumAndSong(artistRequestDto);
    }

    public List<SongDto> findAllSongs(Pageable pageable) {
        log.info("Retrieving all songs");
        return songRetriever.findAll(pageable).stream()
                .map(song -> SongDto.builder()
                        .id(song.getId())
                        .name(song.getName())
                        .build())
                .toList();
    }

    public SongDto findSongById(Long id) {
        return mapFromSongToSOngDto(songRetriever.findById(id));
    }

    @Transactional
    public void deleteSongAndGenreById(Long songId) {
        songDeleter.deleteSongAndGenreById(songId);
    }

    public void deleteSongById(Long id) {
        songRetriever.findById(id);
        log.info("Deleting song with id {}", id);
        songDeleter.deleteById(id);
    }

    @Transactional
    public void updateSongById(Long id, SongDto newSong) {
        //hibernate dirty checking
        // Song songById = songRetriever.findById(id);
        // songById.setName(newSong.getName());
        // songById.setArtist(newSong.getArtist());
        songRetriever.findById(id);
        songUpdater.updateById(id, Song.builder().name(newSong.name()).build());
    }

//    @Transactional
//    public void partiallyUpdateById(Long id, Song updatedSong) {
//
//
//        Song toChange = songRetriever.findById(id);
//        //hibernate dirty chacking
//        // if (updatedSong.getName() != null) {
//        //     toChange.setName(updatedSong.getName());
//        // }
//        // if (updatedSong.getArtist() != null) {
//        //     toChange.setArtist(updatedSong.getArtist());
//        // }
//
//        Song.SongBuilder builder = Song.builder();
//        if (updatedSong.getName() == null) {
//            builder.name(toChange.getName());
//        } else {
//            builder.name(updatedSong.getName());
//        }
//        if (updatedSong.getArtist() == null) {
//            builder.artist(toChange.getArtist());
//        } else {
//            builder.artist(updatedSong.getArtist());
//        }
//        Song newSong = builder.build();
//        updateById(id, newSong);
//    }

}
