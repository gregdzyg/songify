package com.songify.infrastructure.crud.artist;


import com.songify.domain.crud.SongifyCrudFacade;
import com.songify.domain.crud.dto.ArtistDto;
import com.songify.domain.crud.dto.ArtistRequestDto;
import com.songify.infrastructure.crud.song.controller.dto.request.ArtistUpdateRequestDto;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/artist")
@RestController
@AllArgsConstructor
public class ArtistController {

    private final SongifyCrudFacade songifyCrudFacade;

    @GetMapping
    ResponseEntity<AllArtistsDto> getAllArtists(Pageable pageable) {
       AllArtistsDto artists =  new AllArtistsDto(songifyCrudFacade.findAllArtists(pageable));
       return ResponseEntity.ok(artists);
    }

    @PostMapping
    ResponseEntity<ArtistDto> postArtist(@RequestBody ArtistRequestDto artistRequestDto) {
        ArtistDto artistDto = songifyCrudFacade.addArtist(artistRequestDto);
        return ResponseEntity.ok(artistDto);
    }

    @PostMapping("/album/song")
    ResponseEntity<ArtistDto> addArtistWithDefaultAlbumAndSong(
            @RequestBody ArtistRequestDto artistRequestDto) {
        return ResponseEntity.ok(songifyCrudFacade.addArtistWithDefaultAlbumAndSong(artistRequestDto));
    }

    @DeleteMapping("/{id}")
     ResponseEntity<Void> deleteArtistByIdWithAlbumsAndSongs(@PathVariable Long id) {
       songifyCrudFacade.deleteArtistByIdWithAlbumsAndSongs(id);
       return ResponseEntity.noContent().build();
    }

    @PutMapping("/{artistId}/{albumId}")
    ResponseEntity<Void> addArtistToAlbum(@PathVariable Long artistId, @PathVariable Long albumId) {
        songifyCrudFacade.addArtistToAlbum(artistId, albumId);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{artistId}")
    ResponseEntity<ArtistDto> updateArtistNameById(@PathVariable Long artistId,
                                                   @Valid
                                                   @RequestBody ArtistUpdateRequestDto requestDto) {

       ArtistDto artistDto = songifyCrudFacade.updateArtistNameById(artistId, requestDto.name());
        return ResponseEntity.ok(artistDto);
    }

}
