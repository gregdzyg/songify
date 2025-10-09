package com.songify.song.controller;
import com.songify.song.dto.DeleteSongResponseDto;
import com.songify.song.dto.SingleSongResponseDto;
import com.songify.song.dto.SongRequestDto;
import com.songify.song.dto.SongResponseDto;
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

    Map<Integer, String> database = new HashMap<>(Map.of(
            1, "Rock song",
            2, "Metal song",
            3, "Rap song",
            4, "Pop song"));

    @GetMapping("/songs")
    public ResponseEntity<SongResponseDto> getAllSongs(@RequestParam(required = false) Integer limit) {
        if(limit != null) {
        Map<Integer, String> songs = database.entrySet().stream()
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
        SingleSongResponseDto responseDto = new SingleSongResponseDto(database.get(id));
        return ResponseEntity.ok(responseDto);
    }

    @PostMapping("/songs")
    public ResponseEntity<SingleSongResponseDto> addSong(@RequestBody @Valid SongRequestDto body) {
        String songName = body.songName();
        log.info("Added new song: {}", songName);
        database.put(database.size() + 1, songName);
        return ResponseEntity.ok(new SingleSongResponseDto(songName));
    }

    @DeleteMapping("/songs/{id}")
    public ResponseEntity<DeleteSongResponseDto> deleteSongById(@PathVariable Integer id) {
        if (!database.containsKey(id)) {
            throw new SongNotFoundException("Song with id: " + id + " not found");
        }
        database.remove(id);
        return ResponseEntity.ok(new DeleteSongResponseDto("Deleted song with id: " + id, HttpStatus.OK));
    }

}
