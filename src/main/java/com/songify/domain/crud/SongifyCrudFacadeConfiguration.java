package com.songify.domain.crud;

class SongifyCrudFacadeConfiguration {



    public static SongifyCrudFacade createSongifyCrudFacade(final SongRepository songRepository,
                                                            final GenreRepository genreRepository,
                                                            final ArtistRepository artistRepository,
                                                            final AlbumRepository albumRepository,
                                                            final PersistentContextCleaner persistentContextCleaner) {

        SongAdder songAdder = new SongAdder(songRepository);
        SongRetriever songRetriever = new SongRetriever(songRepository);
        SongDeleter songDeleter = new SongDeleter(songRepository, songRetriever,
                new GenreDeleter(genreRepository), persistentContextCleaner);
        SongUpdater songUpdater = new SongUpdater(songRepository, songRetriever);
        ArtistAdder artistAdder = new ArtistAdder(artistRepository);
        GenreAdder genreAdder = new GenreAdder(genreRepository);
        AlbumAdder albumAdder = new AlbumAdder(songRetriever, albumRepository);
        ArtistRetriever artistRetriever = new ArtistRetriever(artistRepository);
        AlbumRetriever albumRetriever = new AlbumRetriever(albumRepository);
        ArtistDeleter artistDeleter = new ArtistDeleter(new AlbumDeleter(albumRepository),
                songDeleter, artistRepository, artistRetriever, albumRetriever);
        ArtistAssigner artistAssigner = new ArtistAssigner(artistRetriever, albumRetriever);
        ArtistUpdater artistUpdater = new ArtistUpdater(artistRetriever);

        return new SongifyCrudFacade(songAdder, songRetriever, songDeleter, songUpdater,
                artistAdder, genreAdder, albumAdder, artistRetriever, albumRetriever,
                artistDeleter, artistAssigner, artistUpdater);
    }
}
