package com.songify.song.domain.service;


import com.songify.song.domain.model.Song;
import com.songify.song.domain.repository.SongRepository;
import jakarta.transaction.Transactional;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class SongUpdater {

    private final SongRepository songRepository;
    private final SongRetriever songRetriever;

    public SongUpdater(SongRepository songRepository, SongRetriever songRetriever) {
        this.songRepository = songRepository;
        this.songRetriever = songRetriever;
    }
    @Transactional
    public void updateById(Long id, Song newSong) {
        //hibernate dirty checking
       // Song songById = songRetriever.findById(id);
       // songById.setName(newSong.getName());
       // songById.setArtist(newSong.getArtist());
        songRetriever.findById(id);
        songRepository.updateById(id, newSong);
    }
    @Transactional
    public void partiallyUpdateById(Long id, Song updatedSong) {


        Song toChange = songRetriever.findById(id);
        //hibernate dirty chacking
       // if (updatedSong.getName() != null) {
       //     toChange.setName(updatedSong.getName());
       // }
       // if (updatedSong.getArtist() != null) {
       //     toChange.setArtist(updatedSong.getArtist());
       // }

        Song.SongBuilder builder = Song.builder();
        if (updatedSong.getName() == null) {
            builder.name(toChange.getName());
        } else {
            builder.name(updatedSong.getName());
        }
        if (updatedSong.getArtist() == null) {
            builder.artist(toChange.getArtist());
        } else {
            builder.artist(updatedSong.getArtist());
        }
        Song newSong = builder.build();
        updateById(id, newSong);
    }
}
