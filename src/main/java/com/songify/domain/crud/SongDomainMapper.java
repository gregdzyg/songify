package com.songify.domain.crud;
import com.songify.domain.crud.dto.SongDto;
import org.springframework.stereotype.Component;

@Component
class SongDomainMapper {


    public static SongDto mapFromSongToSOngDto(Song song) {
        return new SongDto(song.getId(), song.getName());
    }


}
