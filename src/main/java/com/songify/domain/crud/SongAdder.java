package com.songify.domain.crud;

import com.songify.domain.crud.dto.SongDto;
import com.songify.domain.crud.dto.SongRequestDto;
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

    SongDto addSong(SongRequestDto songRequestDto) {
        Song song = new Song(songRequestDto.name(), songRequestDto.releaseDate(), songRequestDto.duration(),
                SongLanguage.valueOf(songRequestDto.language().name()));
        log.info("Added new song: {}", song);
        Song savedSong = songRepository.save(song);
        return new SongDto(savedSong.getId(), savedSong.getName());
    }

}