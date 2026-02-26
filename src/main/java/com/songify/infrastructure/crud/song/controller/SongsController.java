package com.songify.infrastructure.crud.song.controller;
import com.songify.domain.crud.SongifyCrudFacade;
import com.songify.domain.crud.dto.SongDto;
import com.songify.domain.crud.dto.SongRequestDto;
import com.songify.infrastructure.crud.song.controller.dto.request.UpdateSongRequestDto;
import com.songify.infrastructure.crud.song.controller.dto.response.*;
import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;

import static com.songify.infrastructure.crud.song.controller.SongMapper.*;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@Log4j2
@RequestMapping("/songs")
public class SongsController {

    private final SongifyCrudFacade songifyCrudFacade;


    public SongsController(SongifyCrudFacade songifyCrudFacade) {
       this.songifyCrudFacade = songifyCrudFacade;
    }

    @GetMapping
    public ResponseEntity<GetSongResponseDto> getAllSongs(@PageableDefault(page = 0, size = 10) Pageable pageable) {

        List<SongDto> songs = songifyCrudFacade.findAllSongs(pageable);
        GetSongResponseDto responseDto = mapFromSongDtoToGetSongResponseDto(songs);
        return ResponseEntity.ok(responseDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SingleSongResponseDto> getSongById(@PathVariable Long id,
                                                             @RequestHeader(required = false) String requestId) {
        log.info(requestId);
        SongDto songDto = songifyCrudFacade.findSongById(id);
        SingleSongResponseDto responseDto = mapFromSongDtoToSingleSongResponseDto(songDto);
        return ResponseEntity.ok(responseDto);
    }

    @PostMapping
    public ResponseEntity<CreateSongResponseDto> addSong(@RequestBody @Valid SongRequestDto request) {
        SongDto savedSong = songifyCrudFacade.addSong(request);
        CreateSongResponseDto body = new CreateSongResponseDto(savedSong.id(), savedSong.name());
        return ResponseEntity.ok(body);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<DeleteSongResponseDto> deleteSongById(@PathVariable Long id) {
        songifyCrudFacade.deleteSongById(id);
        return ResponseEntity.ok(mapFromSongToDeleteSongResponseDto(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UpdateSongResponseDto> updateSongById(@PathVariable Long id,
                                                                @RequestBody @Valid UpdateSongRequestDto body) {

        SongDto newSong = mapFromUpdateSongRequestDtoToSongDto(body);
        songifyCrudFacade.updateSongById(id, newSong);
        SongDto fresh = songifyCrudFacade.findSongById(id);
        return ResponseEntity.ok(mapFromSongDtoToUpdateSongResponseDto(fresh));
    }

//    @PatchMapping("/{id}")
//    public ResponseEntity<PartiallyUpdateSongResponseDto> partiallyUpdateSongById(@PathVariable Long id,
//                                                                                  @RequestBody PartiallyUpdateSongRequestDto body){
//
//
//        Song newSong = mapFromPartiallyUpdateSongRequestDtoToSong(body);
//        songCrudFacade.partiallyUpdateById(id, newSong);
//        Song fresh = songCrudFacade.findById(id);
//        return ResponseEntity.ok(SongMapper.mapFromSongToPartiallyUpdateSongResponseDto(fresh));
//    }
}
