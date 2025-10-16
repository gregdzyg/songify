package com.songify.song.domain.repository;
import com.songify.song.domain.model.Song;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
@Repository
public class SongRepository {

    Map<Integer, Song> database = new HashMap<>(Map.of(
            1, new Song("Rock song", "System of a dawn"),
            2, new Song("Metal song", "Metallica"),
            3, new Song("Jazz song", "Saxlove"),
            4, new Song("Rap song", "Cypress Hill"),
            5, new Song("Pop song", "Shawn Mendes")
    ));

    public Song saveToDatabase(Song song) {
        database.put(database.size() + 1, song);
        return song;
    }

    public Map<Integer, Song> findAll() {
        return database;
    }
}
