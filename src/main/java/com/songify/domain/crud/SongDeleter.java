package com.songify.domain.crud;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.Set;

@Log4j2
@Service
@Transactional
class SongDeleter {

    private final SongRepository songRepository;
    private final SongRetriever songRetriever;
    private final GenreDeleter genreDeleter;
    private final EntityManager entityManager;


    SongDeleter(SongRepository songRepository, SongRetriever songRetriever, GenreDeleter genreDeleter, EntityManager entityManager) {
        this.songRepository = songRepository;
        this.songRetriever = songRetriever;
        this.genreDeleter = genreDeleter;
        this.entityManager = entityManager;
    }
    void deleteById(Long id) {
        songRetriever.findById(id);
        log.info("Deleting song with id {}", id);
        songRepository.deleteById(id);
    }

    void deleteSongAndGenreById(Long songId) {
        Song song = songRetriever.findById(songId);
        Long genreId = song.getGenre().getId();
        songRepository.deleteById(songId);
        entityManager.clear();
        genreDeleter.deleteById(genreId);

    }

    public void deleteAllSongsByIds(Set<Long> ids) {
        songRepository.deleteByIdIn(ids);
    }
}
