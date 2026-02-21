ALTER TABLE genre
    DROP CONSTRAINT fk_genre_on_song;

ALTER TABLE genre
    DROP COLUMN song_id;