package com.songify.song.infrastructure.controller;

import com.songify.song.domain.model.Song;
import com.songify.song.infrastructure.controller.dto.request.CreateSongRequestDto;
import com.songify.song.infrastructure.controller.dto.request.PartiallyUpdateSongRequestDto;
import com.songify.song.infrastructure.controller.dto.request.UpdateSongRequestDto;
import com.songify.song.infrastructure.controller.dto.response.*;
import org.springframework.http.HttpStatus;

import java.util.List;


public class SongMapper {

    public static SongDto mapFromSongToSongDto(Song song) {
        return new SongDto(song.getId(), song.getArtist(), song.getName());
    }

    public static GetSongResponseDto mapFromSongToGetSongResponseDto(List<Song> songs) {
        List<SongDto> songDtos = songs.stream().map(SongMapper::mapFromSongToSongDto).toList();
        return new GetSongResponseDto(songDtos);
    }

    public static SingleSongResponseDto mapFromSongToSingleSongResponseDto(Song song) {
        return new SingleSongResponseDto(song.getName(), song.getArtist());
    }

    public static Song mapFromCreateSongRequestDtoToSong(CreateSongRequestDto requestDto) {
        return new Song(requestDto.name(), requestDto.artist());
    }

    public static CreateSongResponseDto mapFromSongToCreateSongResponseDto(Song song) {
        SongDto songDto = mapFromSongToSongDto(song);
        return new CreateSongResponseDto(songDto.name(), songDto.artist());
    }

    public static DeleteSongResponseDto mapFromSongToDeleteSongResponseDto(Long id) {
        return new DeleteSongResponseDto("Deleted song with id: " + id, HttpStatus.OK);
    }

    public static Song mapFromUpdateSongRequestDtoToSong(UpdateSongRequestDto requestDto) {
        return new Song(requestDto.name(), requestDto.artist());
    }

    public static UpdateSongResponseDto mapFromSongToUpdateSongResponseDto(Song song) {
        return new UpdateSongResponseDto(song.getName(), song.getArtist());
    }

    public static PartiallyUpdateSongResponseDto mapFromSongToPartiallyUpdateSongResponseDto(Song song) {
        return new PartiallyUpdateSongResponseDto(song.getName(), song.getArtist());
    }

    public static Song mapFromPartiallyUpdateSongRequestDtoToSong(PartiallyUpdateSongRequestDto requestDto) {
        return new Song(requestDto.name(), requestDto.artist());
    }

}
