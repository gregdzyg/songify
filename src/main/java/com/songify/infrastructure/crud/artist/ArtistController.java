package com.songify.infrastructure.crud.artist;


import com.songify.domain.crud.SongifyCrudFacade;
import com.songify.domain.crud.dto.ArtistDto;
import com.songify.domain.crud.dto.ArtistRequestDto;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

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

}
