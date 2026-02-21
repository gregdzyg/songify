package com.songify.domain.crud;

import com.songify.domain.crud.dto.ArtistDto;
import com.songify.domain.crud.dto.ArtistRequestDto;
import com.songify.domain.crud.dto.SongDto;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

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




    public ArtistDto addArtist(ArtistRequestDto artistRequestDto) {
        return artistAdder.addArtist(artistRequestDto.name());
    }

    public SongDto addSong(SongDto song) {
        log.info("Added new song: {}", song);
       Song added = songAdder.addSong(Song.builder().name(song.name()).build());
       return SongDto.builder().id(added.getId()).name(added.getName()).build();
    }

    public List<SongDto> findAll(Pageable pageable) {
        log.info("Retrieving all songs");
        return songRetriever.findAll(pageable).stream()
                .map(song -> SongDto.builder()
                        .id(song.getId())
                        .name(song.getName())
                        .build())
                .toList();
    }

    public SongDto findById(Long id) {
        return mapFromSongToSOngDto(songRetriever.findById(id));
    }

    public void deleteById(Long id) {
        songRetriever.findById(id);
        log.info("Deleting song with id {}", id);
        songDeleter.deleteById(id);
    }

    @Transactional
    public void updateById(Long id, SongDto newSong) {
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
