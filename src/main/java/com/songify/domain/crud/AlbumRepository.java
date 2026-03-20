package com.songify.domain.crud;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;

interface AlbumRepository extends Repository<Album, Long> {

    Album save(Album album);

    //Optional<Album> findById(Long id);
    @Query("""
           select a from Album a
           left join fetch a.artists
           left join fetch a.songs
            where a.id = :id
           """)
    Optional<Album> findById(Long id);

    @Query("""
          select a from Album a
          inner join a.artists artists
          where artists.id = :id
            """)
    Set<Album> findByArtistId(@Param("id") Long id);
    @Transactional
    @Modifying
    @Query("delete from Album a where a.id in :ids")
    void deleteByIdIn(Collection<Long> ids);



}
