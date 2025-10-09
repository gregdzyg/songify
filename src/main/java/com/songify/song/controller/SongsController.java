package com.songify.song.controller;
import com.songify.song.dto.request.PartiallyUpdateSongRequestDto;
import com.songify.song.dto.request.UpdateSongRequestDto;
import com.songify.song.dto.response.*;
import com.songify.song.dto.request.SongRequestDto;
import com.songify.song.error.SongNotFoundException;
import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@Log4j2
public class SongsController {

    Map<Integer, Song> database = new HashMap<>(Map.of(
            1, new Song("Rock song", "System of a dawn"),
            2, new Song("Metal song", "Metallica"),
            3, new Song("Jazz song", "Saxlove"),
            4, new Song("Rap song", "Cypress Hill"),
            5, new Song("Pop song", "Shawn Mendes")
    ));


    @GetMapping("/songs")
    public ResponseEntity<SongResponseDto> getAllSongs(@RequestParam(required = false) Integer limit) {
        if(limit != null) {
        Map<Integer, Song> songs = database.entrySet().stream()
                .limit(limit).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        return ResponseEntity.ok(new SongResponseDto(songs));
        }
        SongResponseDto responseDto = new SongResponseDto(database);
        return ResponseEntity.ok(responseDto);
    }

    @GetMapping("/songs/{id}")
    public ResponseEntity<SingleSongResponseDto> getSongById(@PathVariable Integer id,
                                                             @RequestHeader(required = false) String requestId) {
        log.info(requestId);
        if (!database.containsKey(id)) {
            throw new SongNotFoundException("Song with id: " + id + " not found");
        }
        String songName = database.get(id).name();
        String artistName = database.get(id).artist();
        SingleSongResponseDto responseDto = new SingleSongResponseDto(songName, artistName);
        return ResponseEntity.ok(responseDto);
    }

    @PostMapping("/songs")
    public ResponseEntity<SingleSongResponseDto> addSong(@RequestBody @Valid SongRequestDto body) {
        String songName = body.name();
        String artistName = body.artist();
        Song song = new Song(songName, artistName);
        log.info("Added new song: {}", song);
        database.put(database.size() + 1, song);
        return ResponseEntity.ok(new SingleSongResponseDto(songName, artistName));
    }

    @DeleteMapping("/songs/{id}")
    public ResponseEntity<DeleteSongResponseDto> deleteSongById(@PathVariable Integer id) {
        if (!database.containsKey(id)) {
            throw new SongNotFoundException("Song with id: " + id + " not found");
        }
        database.remove(id);
        return ResponseEntity.ok(new DeleteSongResponseDto("Deleted song with id: " + id, HttpStatus.OK));
    }

    @PutMapping("songs/{id}")
    public ResponseEntity<UpdateSongResponseDto> updateSongById(@PathVariable Integer id,
                                                                @RequestBody @Valid UpdateSongRequestDto body) {

        if (!database.containsKey(id)) {
            throw new SongNotFoundException("Song with id: " + id + " not found");
        }
        Song songToChange = database.get(id);
        String newSongName = body.name();
        String newSongArtist = body.artist();
        Song newSong = new Song(newSongName, newSongArtist);
        log.info("{} replaced with: {}", songToChange, newSong);
        database.replace(id, songToChange, newSong);
        return ResponseEntity.ok(new UpdateSongResponseDto(newSongName, newSongArtist));
    }

    @PatchMapping("songs/{id}")
    public ResponseEntity<PartiallyUpdateSongResponseDto> partiallyUpdateSongById(@PathVariable Integer id,
                                                                                  @RequestBody PartiallyUpdateSongRequestDto body){
        if (!database.containsKey(id)) {
            throw new SongNotFoundException("Song with id: " + id + " not found");
        }
        Song song = database.get(id);
        Song.SongBuilder builder = Song.builder();
        if (body.name() != null) {
            builder.name(body.name());
        } else {
            builder.name(song.name());
        }
        if (body.artist() != null) {
            builder.artist(body.artist());
        } else {
            builder.artist(song.artist());
        }
        Song newSong = builder.build();
        database.replace(id, song, newSong);
        return ResponseEntity.ok(new PartiallyUpdateSongResponseDto(newSong));
    }
}
