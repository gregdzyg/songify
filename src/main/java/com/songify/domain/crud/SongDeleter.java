package com.songify.domain.crud;

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
    private final PersistentContextCleaner persistentContextCleaner;


    SongDeleter(SongRepository songRepository, SongRetriever songRetriever, GenreDeleter genreDeleter,
                PersistentContextCleaner persistentContextCleaner) {
        this.songRepository = songRepository;
        this.songRetriever = songRetriever;
        this.genreDeleter = genreDeleter;
        this.persistentContextCleaner = persistentContextCleaner;
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
        persistentContextCleaner.clear();
        genreDeleter.deleteById(genreId);

    }

    public void deleteAllSongsByIds(Set<Long> ids) {
        songRepository.deleteByIdIn(ids);
    }
}
