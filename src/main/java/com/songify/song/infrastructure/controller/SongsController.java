package com.songify.song.infrastructure.controller;
import com.songify.song.domain.service.SongAdder;
import com.songify.song.domain.service.SongRetriever;
import com.songify.song.infrastructure.controller.dto.request.PartiallyUpdateSongRequestDto;
import com.songify.song.infrastructure.controller.dto.request.UpdateSongRequestDto;
import com.songify.song.infrastructure.controller.dto.response.*;
import com.songify.song.infrastructure.controller.dto.request.CreateSongRequestDto;
import com.songify.song.domain.model.SongNotFoundException;
import com.songify.song.domain.model.Song;
import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@Log4j2
@RequestMapping("/songs")
public class SongsController {

    private final SongAdder songAdder;
    private final SongRetriever songRetriever;

    public SongsController(SongAdder songAdder, SongRetriever songRetriever) {
        this.songAdder = songAdder;
        this.songRetriever = songRetriever;
    }

    @GetMapping
    public ResponseEntity<GetSongResponseDto> getAllSongs(@RequestParam(required = false) Integer limit) {
        if(limit != null) {
        Map<Integer, Song> songs = songRetriever.findAllLimitedBy(limit);
        return ResponseEntity.ok(SongMapper.mapFromSongToGetSongResponseDto(songs));
        }
        GetSongResponseDto responseDto = SongMapper.mapFromSongToGetSongResponseDto(songRetriever.findAll());
        return ResponseEntity.ok(responseDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SingleSongResponseDto> getSongById(@PathVariable Integer id,
                                                             @RequestHeader(required = false) String requestId) {
        log.info(requestId);
        Map<Integer, Song> allSongs = songRetriever.findAll();
        if (!allSongs.containsKey(id)) {
            throw new SongNotFoundException("Song with id: " + id + " not found");
        }
        Song song = allSongs.get(id);
        SingleSongResponseDto responseDto = SongMapper.mapFromSongToSingleSongResponseDto(song);
        return ResponseEntity.ok(responseDto);
    }

    @PostMapping
    public ResponseEntity<CreateSongResponseDto> addSong(@RequestBody @Valid CreateSongRequestDto body) {
        Song song = SongMapper.mapFromCreateSongRequestDtoToSong(body);
        songAdder.addSong(song);
        return ResponseEntity.ok(SongMapper.mapFromSongToCreateSongResponseDto(song));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<DeleteSongResponseDto> deleteSongById(@PathVariable Integer id) {
        Map<Integer, Song> allSongs = songRetriever.findAll();
        if (!allSongs.containsKey(id)) {
            throw new SongNotFoundException("Song with id: " + id + " not found");
        }
        allSongs.remove(id);
        return ResponseEntity.ok(SongMapper.mapFromSongToDeleteSongResponseDto(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UpdateSongResponseDto> updateSongById(@PathVariable Integer id,
                                                                @RequestBody @Valid UpdateSongRequestDto body) {

        Map<Integer, Song> allSongs = songRetriever.findAll();
        if (!allSongs.containsKey(id)) {
            throw new SongNotFoundException("Song with id: " + id + " not found");
        }
        Song songToChange = allSongs.get(id);
        Song newSong = SongMapper.mapFromUpdateSongRequestDtoToSong(body);
        log.info("{} replaced with: {}", songToChange, newSong);
        allSongs.replace(id, songToChange, newSong);
        return ResponseEntity.ok(SongMapper.mapFromSongToUpdateSongResponseDto(newSong));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<PartiallyUpdateSongResponseDto> partiallyUpdateSongById(@PathVariable Integer id,
                                                                                  @RequestBody PartiallyUpdateSongRequestDto body){
        Map<Integer, Song> allSongs = songRetriever.findAll();
        if (!allSongs.containsKey(id)) {
            throw new SongNotFoundException("Song with id: " + id + " not found");
        }
        Song song = allSongs.get(id);
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
        allSongs.replace(id, song, newSong);
        return ResponseEntity.ok(SongMapper.mapFromSongToPartiallyUpdateSongResponseDto(newSong));
    }
}
