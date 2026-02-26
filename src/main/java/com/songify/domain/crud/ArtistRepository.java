package com.songify.domain.crud;

import com.songify.domain.crud.dto.ArtistDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.Repository;

import java.util.Set;

interface ArtistRepository extends Repository<Artist, Long> {
    Artist save(Artist artist);
    Set<Artist> findAll(Pageable pageable);
}
