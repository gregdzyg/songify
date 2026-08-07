package com.songify.domain.crud;

import com.songify.domain.crud.dto.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Pageable;

import java.time.Instant;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

class SongifyCrudFacadeTest {

    private InMemorySongRepository songRepository;
    private InMemoryGenreRepository genreRepository;
    private InMemoryArtistRepository artistRepository;
    private InMemoryAlbumRepository albumRepository;
    private TestJpaPersistentContextCleaner persistentContextCleaner;
    private SongifyCrudFacade songifyCrudFacade;

    @BeforeEach
    void setUp() {
        songRepository = new InMemorySongRepository();
        genreRepository = new InMemoryGenreRepository();
        artistRepository = new InMemoryArtistRepository();
        albumRepository = new InMemoryAlbumRepository();
        persistentContextCleaner = new TestJpaPersistentContextCleaner();
        songifyCrudFacade = SongifyCrudFacadeConfiguration.createSongifyCrudFacade(
                songRepository,
                genreRepository,
                artistRepository,
                albumRepository,
                persistentContextCleaner
        );
    }

    @Test
    public void should_add_artist() {
        //given
        ArtistRequestDto requestDto = ArtistRequestDto.builder()
                .name("Shawn Mendes")
                .build();
        Set<ArtistDto> artists = songifyCrudFacade.findAllArtists(Pageable.unpaged());
        assertThat(artists).isEmpty();
        //when
        ArtistDto artistDto = songifyCrudFacade.addArtist(requestDto);
        //then
        assertThat(artistDto.name()).isEqualTo("Shawn Mendes");
        assertThat(songifyCrudFacade.findAllArtists(Pageable.unpaged()))
                .containsExactly(new ArtistDto("Shawn Mendes"));
    }

    @Test
    public void should_throw_exception_artist_not_found_when_artist_id_was_zero() {
        //given
        assertThat(songifyCrudFacade.findAllArtists(Pageable.unpaged())).isEmpty();
        //when
        Throwable throwable = catchThrowable(() -> songifyCrudFacade.deleteArtistByIdWithAlbumsAndSongs(0L));
        //then
        assertThat(throwable)
                .isInstanceOf(ArtistNotFoundException.class)
                .hasMessage("Artist with id 0 not found");
    }

    @Test
    public void should_add_and_find_song() {
        //given
        SongRequestDto requestDto = new SongRequestDto(
                "Stitches", Instant.parse("2015-04-14T00:00:00Z"), 206L, SongLanguageDto.ENGLISH);
        //when
        SongDto savedSong = songifyCrudFacade.addSong(requestDto);
        //then
        assertThat(savedSong).isEqualTo(new SongDto(0L, "Stitches"));
        assertThat(songifyCrudFacade.findSongById(savedSong.id())).isEqualTo(savedSong);
        assertThat(songifyCrudFacade.findAllSongs(Pageable.unpaged())).containsExactly(savedSong);
        assertThat(songRepository.findById(savedSong.id()).orElseThrow().getGenre().getName())
                .isEqualTo("default-genre");
    }

    @Test
    public void should_throw_exception_when_song_was_not_found() {
        //given
        Long missingSongId = 100L;
        //when
        Throwable throwable = catchThrowable(() -> songifyCrudFacade.findSongById(missingSongId));
        //then
        assertThat(throwable)
                .isInstanceOf(SongNotFoundException.class)
                .hasMessage("Song with id: 100 not found");
    }

    @Test
    public void should_delete_song() {
        //given
        Song savedSong = songRepository.save(new Song("Stitches", "Shawn Mendes"));
        //when
        songifyCrudFacade.deleteSongById(savedSong.getId());
        //then
        assertThat(songRepository.findById(savedSong.getId())).isEmpty();
    }

    @Test
    public void should_update_song_name_without_removing_artist() {
        //given
        Song savedSong = songRepository.save(new Song("Old title", "Shawn Mendes"));
        //when
        songifyCrudFacade.updateSongById(savedSong.getId(), new SongDto(null, "New title"));
        //then
        Song updatedSong = songRepository.findById(savedSong.getId()).orElseThrow();
        assertThat(updatedSong.getName()).isEqualTo("New title");
        assertThat(updatedSong.getArtist()).isEqualTo("Shawn Mendes");
    }

    @Test
    public void should_delete_song_with_its_genre_and_clear_persistent_context() {
        //given
        Genre genre = genreRepository.save(new Genre("Pop"));
        Song song = new Song("Stitches", "Shawn Mendes");
        song.setGenre(genre);
        Song savedSong = songRepository.save(song);
        //when
        songifyCrudFacade.deleteSongAndGenreById(savedSong.getId());
        //then
        assertThat(songRepository.findById(savedSong.getId())).isEmpty();
        assertThat(genreRepository.getGenreById(genre.getId())).isNull();
        assertThat(persistentContextCleaner.wasCleared()).isTrue();
    }

    @Test
    public void should_keep_shared_album_and_its_songs_when_one_artist_was_deleted() {
        //given
        SongDto song = addSong("Treat You Better");
        AlbumDto album = songifyCrudFacade.addAlbum(
                new AlbumRequestDto(song.id(), "Illuminate", Instant.parse("2016-09-23T00:00:00Z")));
        songifyCrudFacade.addArtist(new ArtistRequestDto("Shawn Mendes"));
        songifyCrudFacade.addArtist(new ArtistRequestDto("Camila Cabello"));
        songifyCrudFacade.addArtistToAlbum(0L, album.id());
        songifyCrudFacade.addArtistToAlbum(1L, album.id());
        //when
        songifyCrudFacade.deleteArtistByIdWithAlbumsAndSongs(0L);
        //then
        AlbumDtoWithSongAndArtist savedAlbum = songifyCrudFacade.findAlbumWithArtistAndSongById(album.id());
        assertThat(savedAlbum.artists()).containsExactly(new ArtistDto("Camila Cabello"));
        assertThat(savedAlbum.songs()).containsExactly(song);
        assertThat(songifyCrudFacade.findAllArtists(Pageable.unpaged()))
                .containsExactly(new ArtistDto("Camila Cabello"));
    }

    @Test
    public void should_delete_album_and_songs_owned_only_by_deleted_artist() {
        //given
        SongDto song = addSong("Stitches");
        AlbumDto album = songifyCrudFacade.addAlbum(
                new AlbumRequestDto(song.id(), "Handwritten", Instant.parse("2015-04-14T00:00:00Z")));
        songifyCrudFacade.addArtist(new ArtistRequestDto("Shawn Mendes"));
        songifyCrudFacade.addArtistToAlbum(0L, album.id());
        //when
        songifyCrudFacade.deleteArtistByIdWithAlbumsAndSongs(0L);
        //then
        assertThat(songRepository.findById(song.id())).isEmpty();
        assertThat(albumRepository.findById(album.id())).isEmpty();
        assertThat(artistRepository.findById(0L)).isEmpty();
    }

    @Test
    public void should_update_artist_name() {
        //given
        songifyCrudFacade.addArtist(new ArtistRequestDto("Old name"));
        //when
        ArtistDto updatedArtist = songifyCrudFacade.updateArtistNameById(0L, "New name");
        //then
        assertThat(updatedArtist).isEqualTo(new ArtistDto("New name"));
        assertThat(songifyCrudFacade.findAllArtists(Pageable.unpaged()))
                .containsExactly(new ArtistDto("New name"));
    }

    @Test
    public void should_delete_artist_without_albums() {
        //given
        songifyCrudFacade.addArtist(new ArtistRequestDto("Shawn Mendes"));
        //when
        songifyCrudFacade.deleteArtistByIdWithAlbumsAndSongs(0L);
        //then
        assertThat(songifyCrudFacade.findAllArtists(Pageable.unpaged())).isEmpty();
    }

    @Test
    public void should_add_artist_with_default_album_song_and_genre() {
        //given
        ArtistRequestDto requestDto = new ArtistRequestDto("Shawn Mendes");
        //when
        songifyCrudFacade.addArtistWithDefaultAlbumAndSong(requestDto);
        //then
        Artist artist = artistRepository.findById(0L).orElseThrow();
        assertThat(artist.getAlbums()).hasSize(1);
        Album album = artist.getAlbums().iterator().next();
        assertThat(album.getArtists()).containsExactly(artist);
        assertThat(album.getSongs()).hasSize(1);
        assertThat(album.getSongs().iterator().next().getGenre().getName()).isEqualTo("default-genre");
    }

    @Test
    public void should_not_add_album_when_song_was_not_found() {
        //given
        AlbumRequestDto requestDto = new AlbumRequestDto(
                100L, "Handwritten", Instant.parse("2015-04-14T00:00:00Z"));
        //when
        Throwable throwable = catchThrowable(() -> songifyCrudFacade.addAlbum(requestDto));
        //then
        assertThat(throwable)
                .isInstanceOf(SongNotFoundException.class)
                .hasMessage("Song with id: 100 not found");
        assertThat(albumRepository.findById(0L)).isEmpty();
    }

    private SongDto addSong(String name) {
        return songifyCrudFacade.addSong(new SongRequestDto(
                name, Instant.parse("2015-04-14T00:00:00Z"), 206L, SongLanguageDto.ENGLISH));
    }
}
