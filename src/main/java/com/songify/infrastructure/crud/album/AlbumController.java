package com.songify.infrastructure.crud.album;


import com.songify.domain.crud.SongifyCrudFacade;
import com.songify.domain.crud.dto.AlbumDto;
import com.songify.domain.crud.dto.AlbumDtoWithSongAndArtist;
import com.songify.domain.crud.dto.AlbumRequestDto;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/album")
@AllArgsConstructor
class AlbumController {

    private final SongifyCrudFacade songifyCrudFacade;

    @PostMapping
    public ResponseEntity<AlbumDto> postAlbum(@RequestBody AlbumRequestDto albumRequestDto) {
        AlbumDto albumDto = songifyCrudFacade.addAlbum(albumRequestDto);
        return ResponseEntity.ok(albumDto);
    }

    @GetMapping("/{albumId}")
    public ResponseEntity<AlbumDtoWithSongAndArtist> findAlbumWithArtistAndSongById(@PathVariable Long albumId) {
        AlbumDtoWithSongAndArtist albumDtoWithSongAndArtist = songifyCrudFacade.findAlbumWithArtistAndSongById(albumId);
        return ResponseEntity.ok(albumDtoWithSongAndArtist);
    }
}
