package com.songify.domain.crud;

import com.songify.domain.crud.dto.ArtistDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
class ArtistAdder {

    ArtistRepository artistRepository;

    public ArtistDto addArtist(String name) {
        Artist artist = new Artist(name);
        artistRepository.save(artist);
        return new ArtistDto(artist.getName());
    }
}
