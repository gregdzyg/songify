package com.songify.domain.crud;

import jakarta.transaction.Transactional;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Log4j2
@Service
@Transactional
class SongAdder {

    SongRepository songRepository;

    public SongAdder(SongRepository songRepository) {
        this.songRepository = songRepository;
    }

    Song addSong(Song song) {
        log.info("Added new song: {}", song);
        return songRepository.save(song);
    }

}