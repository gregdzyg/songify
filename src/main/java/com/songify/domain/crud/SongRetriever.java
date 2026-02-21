package com.songify.domain.crud;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import java.util.List;
import org.springframework.data.domain.Pageable;

@Log4j2
@Service
class SongRetriever {

    SongRepository songRepository;

    public SongRetriever(SongRepository songRepository) {
        this.songRepository = songRepository;
    }

    List<Song> findAll(Pageable pageable) {
        log.info("Retrieving all songs");
        return songRepository.findAll(pageable);
    }

    Song findById(Long id) {
        return songRepository.findById(id).orElseThrow(() -> new SongNotFoundException("Song with id: " + id + " not found"));
    }

}
