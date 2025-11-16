-- schema.sql

CREATE TABLE IF NOT EXISTS song (
    id BIGINT PRIMARY KEY DEFAULT nextval('song_id_seq'),
    artist VARCHAR(255) NOT NULL,
    name VARCHAR(255) NOT NULL
);
