package com.songify.song.infrastructure.controller;
import com.songify.song.domain.service.SongAdder;
import com.songify.song.domain.service.SongDeleter;
import com.songify.song.domain.service.SongRetriever;
import com.songify.song.domain.service.SongUpdater;
import com.songify.song.infrastructure.controller.dto.request.PartiallyUpdateSongRequestDto;
import com.songify.song.infrastructure.controller.dto.request.UpdateSongRequestDto;
import com.songify.song.infrastructure.controller.dto.response.*;
import com.songify.song.infrastructure.controller.dto.request.CreateSongRequestDto;
import com.songify.song.domain.model.Song;
import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;
import static com.songify.song.infrastructure.controller.SongMapper.*;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@Log4j2
@RequestMapping("/songs")
public class SongsController {

    private final SongAdder songAdder;
    private final SongRetriever songRetriever;
    private final SongDeleter songDeleter;
    private final SongUpdater songUpdater;

    public SongsController(SongAdder songAdder, SongRetriever songRetriever, SongDeleter songDeleter, SongUpdater songUpdater) {
        this.songAdder = songAdder;
        this.songRetriever = songRetriever;
        this.songDeleter = songDeleter;
        this.songUpdater = songUpdater;
    }

    @GetMapping
    public ResponseEntity<GetSongResponseDto> getAllSongs(@PageableDefault(page = 0, size = 10) Pageable pageable) {

        List<Song> songs = songRetriever.findAll(pageable);
        GetSongResponseDto responseDto = mapFromSongToGetSongResponseDto(songs);
        return ResponseEntity.ok(responseDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SingleSongResponseDto> getSongById(@PathVariable Long id,
                                                             @RequestHeader(required = false) String requestId) {
        log.info(requestId);
        Song song = songRetriever.findById(id);
        SingleSongResponseDto responseDto = mapFromSongToSingleSongResponseDto(song);
        return ResponseEntity.ok(responseDto);
    }

    @PostMapping
    public ResponseEntity<CreateSongResponseDto> addSong(@RequestBody @Valid CreateSongRequestDto body) {
        Song song = mapFromCreateSongRequestDtoToSong(body);
        Song savedSong = songAdder.addSong(song);
        return ResponseEntity.ok(mapFromSongToCreateSongResponseDto(savedSong));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<DeleteSongResponseDto> deleteSongById(@PathVariable Long id) {
        songDeleter.deleteById(id);
        return ResponseEntity.ok(mapFromSongToDeleteSongResponseDto(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UpdateSongResponseDto> updateSongById(@PathVariable Long id,
                                                                @RequestBody @Valid UpdateSongRequestDto body) {

        Song newSong = mapFromUpdateSongRequestDtoToSong(body);
        songUpdater.updateById(id, newSong);
        Song fresh = songRetriever.findById(id);
        return ResponseEntity.ok(mapFromSongToUpdateSongResponseDto(fresh));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<PartiallyUpdateSongResponseDto> partiallyUpdateSongById(@PathVariable Long id,
                                                                                  @RequestBody PartiallyUpdateSongRequestDto body){


        Song newSong = mapFromPartiallyUpdateSongRequestDtoToSong(body);
        songUpdater.partiallyUpdateById(id, newSong);
        Song fresh = songRetriever.findById(id);
        return ResponseEntity.ok(SongMapper.mapFromSongToPartiallyUpdateSongResponseDto(fresh));
    }
}
