package com.songify.domain.crud;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
@Component
interface SongRepository extends Repository<Song, Long> {
    Song save(Song song);
    @Query("SELECT s FROM Song s")
    List<Song> findAll(Pageable pageable);
    @Query("SELECT s FROM Song s WHERE s.id = :id")
    Optional<Song> findById(Long id);
    @Modifying
    @Query("DELETE FROM Song s WHERE s.id = :id")
    void deleteById(Long id);
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("UPDATE Song s SET s.name = :#{#newSong.name}, s.artist = :#{#newSong.artist} WHERE s.id = :id")
    void updateById(Long id, Song newSong);
}
