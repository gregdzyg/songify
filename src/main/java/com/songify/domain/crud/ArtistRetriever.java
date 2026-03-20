package com.songify.domain.crud;

import com.songify.domain.crud.dto.ArtistDto;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.Set;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
class ArtistRetriever {

    ArtistRepository artistRepository;

    public Set<ArtistDto> findAllArtists(Pageable pageable) {


        return artistRepository.findAll(pageable).stream().map(artist ->
                new ArtistDto(artist.getName())).collect(Collectors.toSet());
    }

    public Artist findById(Long artistId) {
       return  artistRepository.findById(artistId).orElseThrow(() -> new ArtistNotFoundException(artistId.toString()));
    }
}
